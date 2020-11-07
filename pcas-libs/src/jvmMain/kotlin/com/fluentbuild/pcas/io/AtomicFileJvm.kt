package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.logs.getLog
import java.io.*

/**
 * A helper class for performing atomic operations on a file by writing to a new file and renaming it into the
 * place of the original file after the write has successfully completed.
 * The new file created when writing will be the same file path with ".new" appended.
 *
 * Atomic file guarantees file integrity by ensuring that a file has been completely written and
 * sync'd to disk before renaming it to the original file.
 *
 * Atomic file does not confer any file locking semantics. Do not use this class when the file may
 * be accessed or modified concurrently by multiple threads or processes. The caller is responsible
 * for ensuring appropriate mutual exclusion invariants whenever it accesses the file.
 *
 * Source: https://android.googlesource.com/platform/frameworks/support/+/a9ac247af2afd4115c3eb6d16c05bc92737d6305/compat/src/main/java/androidx/core/util/AtomicFile.java
 */
class AtomicFileJvm(private val baseFile: File): AtomicFile {

	private val log = getLog()
	private val newFile = File(baseFile.path + ".new")

	/**
	 * Delete the atomic file.  This deletes both the base and new files.
	 */
	fun delete() {
		baseFile.delete()
		newFile.delete()
	}

	/**
	 * Start a new write operation on the file.  This returns a FileOutputStream to which you can write the new file data.
	 * The existing file is replaced with the new data.
	 * You *must not* directly close the given FileOutputStream; instead call either [finishWrite] or [failWrite].
	 *
	 * Note that if another thread is currently performing a write, this will simply replace whatever that thread is writing
	 * with the new file being written by this thread, and when the other thread finishes the write the new write
	 * operation will no longer be safe (or will be lost).  You must do your own threading protection for
	 * access to AtomicFile.
	 */
	@Throws(IOException::class)
	fun startWrite(): FileOutputStream {
		return try {
			FileOutputStream(newFile)
		} catch (e: FileNotFoundException) {
			if (!newFile.parentFile.mkdirs()) {
				throw IOException("Failed to create directory for $newFile")
			}

			try {
				FileOutputStream(newFile)
			} catch (e2: FileNotFoundException) {
				throw IOException("Failed to create new file $newFile", e2)
			}
		}
	}

	/**
	 * Call when you have successfully finished writing to the stream returned by [startWrite].
	 * This will close, sync, and commit the new data.
	 * The next attempt to read the atomic file will return the new file stream.
	 */
	fun finishWrite(stream: FileOutputStream) {
		if (!sync(stream)) {
			log.error { "Failed to sync file output stream" }
		}

		try {
			stream.close()
		} catch (e: IOException) {
			log.error(e) { "Failed to close file output stream" }
		}

		rename(newFile, baseFile)
	}

	override fun write(data: ByteArray) {
		startWrite().apply {
			write(data)
			finishWrite(this)
		}
	}

	/**
	 * Call when you have failed for some reason at writing to the stream returned by [startWrite].
	 * This will close the current write stream, and delete the new file.
	 */
	fun failWrite(stream: FileOutputStream) {
		if (!sync(stream)) {
			log.error { "Failed to sync file output stream" }
		}

		try {
			stream.close()
		} catch (e: IOException) {
			log.error(e) { "Failed to close file output stream" }
		}

		if (!newFile.delete()) {
			log.error { "Failed to delete new file $newFile" }
		}
	}

	/**
	 * Open the atomic file for reading. You should call close() on the FileInputStream when you are
	 * done reading from it.
	 *
	 * You must do your own threading protection for access to AtomicFile.
	 */
	@Throws(FileNotFoundException::class)
	fun openRead(): FileInputStream {
		// It was okay to call openRead() between startWrite() and finishWrite() for the first time
		// (because there is no backup file), where openRead() would open the file being written,
		// which makes no sense, but finishWrite() would still persist the write properly. For all
		// subsequent writes, if openRead() was called in between, it would see a backup file and
		// delete the file being written, the same behavior as our new implementation. So we only
		// need a special case for the first write, and don't delete the new file in this case so
		// that finishWrite() can still work.
		if (newFile.exists() && baseFile.exists()) {
			if (!newFile.delete()) {
				log.error { "Failed to delete outdated new file $newFile" }
			}
		}

		return FileInputStream(baseFile)
	}

	override fun read(): ByteArray {
		return openRead().use { it.readBytes() }
	}

	override val isExist get() = baseFile.exists()

	private fun sync(stream: FileOutputStream): Boolean {
		return try {
			stream.fd.sync()
			true
		} catch (e: IOException) {
			false
		}
	}

	private fun rename(source: File, target: File) {
		// We used to delete the target file before rename, but that isn't atomic, and the rename()
		// syscall should atomically replace the target file. However in the case where the target
		// file is a directory, a simple rename() won't work. We need to delete the file in this
		// case because there are callers who erroneously called mBaseName.mkdirs() (instead of
		// mBaseName.getParentFile().mkdirs()) before creating the AtomicFile, and it worked
		// regardless, so this deletion became some kind of API.
		if (target.isDirectory) {
			if (!target.delete()) {
				log.error { "Failed to delete file which is a directory $target" }
			}
		}

		if (!source.renameTo(target)) {
			log.error { "Failed to rename $source to $target" }
		}
	}
}