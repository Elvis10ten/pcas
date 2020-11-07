package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.io.transport.MessageReceiver
import java.io.IOException
import java.io.InterruptedIOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import kotlin.Exception

internal class SocketWrapper<SocketT: DatagramSocket>(
	private val parceler: Parceler,
	private val bufferPool: BufferObjectPool,
	private val runner: ThreadRunner
) {

	private val log = getLog()
	private var socket: SocketT? = null

	@Throws(IOException::class)
	fun init(newSocket: SocketT, modifier: SocketT.() -> Unit = {}) {
		require(socket == null) { "SocketWrapper already initialized" }
		log.debug { "Initializing SocketWrapper($newSocket)" }

		try {
			modifier(newSocket)
			socket = newSocket
		} catch(e: Exception) {
			log.error(e) { "Error initializing socket" }
			newSocket.closeQuietly(log)
		}
	}

	fun close(cleanup: SocketT.() -> Unit = {}) {
		socket?.apply {
			log.debug { "Closing SocketWrapper($this)" }
			runner.cancelAll()
			cleanup(this)
			closeQuietly(log)
		}

		socket = null
	}

	@Throws(IOException::class)
	fun startReceiving(receiver: MessageReceiver) {
		socket.requireOpenAndBounded()
		runner.runOnIo { receiveBlocking(receiver) }
	}

	private fun receiveBlocking(receiver: MessageReceiver) {
		while(socket.canReceive()) {
			try {
				runner.runOnMain { log.debug { "Receiving..." } }
				val packet = getReceivePacket()
				socket!!.receive(packet)

				parceler.unparcel(packet.data, packet.length) { message, messageSize ->
					// todo: bufferPool.recycle(packet.data)
					runner.runOnMain {
						receiver.onReceived(message, messageSize)
						bufferPool.recycle(message)
					}
				}
			} catch (e: Exception) {
				runner.runOnMain { log.error(e) { "Error receiving parcel" } }
				if(e is InterruptedException || e is InterruptedIOException) {
					break
				}
			}
		}
	}

	@Throws(IOException::class)
	fun send(message: ByteArray, messageSize: Int, address: Address.Ipv4, port: Port) {
		socket.requireOpenAndBounded()
		log.debug { "Sending message  to $address:$port" }
		runner.runOnIo { sendBlocking(message, messageSize, address, port) }
	}

	private fun sendBlocking(message: ByteArray, messageSize: Int, address: Address.Ipv4, port: Port) {
		try {
			getSendPacket(message, messageSize, address, port).let {
				socket!!.send(it)
				// todo: bufferPool.recycle(it.data)
			}
		} catch (e: Exception) {
			runner.runOnMain { log.error(e) { "Error sending parcel" } }
		}
	}

	@Throws(IOException::class, NullPointerException::class)
	fun getPort() = socket!!.requirePort()

	private fun getSendPacket(message: ByteArray, messageSize: Int, address: Address.Ipv4, port: Port): DatagramPacket {
		return parceler.parcel(message, messageSize) { parcel, parcelSize ->
			DatagramPacket(parcel, parcelSize, address.inetAddress, port)
		}
	}

	private fun getReceivePacket(): DatagramPacket {
		return bufferPool.allocate().let { DatagramPacket(it, it.size) }
	}
}