package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.async.ThreadRunnerJvm
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

internal open class AsyncModuleJvm: AsyncModule() {

    protected val threadPool = ThreadPoolExecutor(
        CORE_POOL_SIZE,
        MAX_POOL_SIZE,
        KEEP_ALIVE_TIME_SECONDS,
        TimeUnit.SECONDS,
        LinkedBlockingQueue(WORK_QUEUE_CAPACITY)
    )

    override val threadRunnerProvider = { ThreadRunnerJvm(threadPool) }

    override fun release() {
        threadPool.shutdownNow()
    }

    companion object {

        /**
         * Needed to handle the following IO bound "independent" tasks:
         * 1. Sending ledger messages (Short running depending on Socket)
         * 2. Receiving ledger messages (Long running: Always)
         * 3. Sending stream messages (Short running depending on Socket)
         * 4. Receiving stream messages (Long running: Always)
         * 5. Handling stream data (Long running: Only when required)
         * 6. Capturing stream data (Long running: Only when required)
         *
         * +2 for buffer. Assuming most Android 26+ devices are quad core, this gives 2 threads / processor.
         */
        private const val MINIMUM_REQUIRED_THREAD_SIZE = 6 + 2

        // The amount of time an idle thread waits before terminating
        private const val KEEP_ALIVE_TIME_SECONDS = 4L
        private const val CORE_POOL_SIZE = MINIMUM_REQUIRED_THREAD_SIZE
        private const val MAX_POOL_SIZE = CORE_POOL_SIZE * 2
        private const val WORK_QUEUE_CAPACITY = 128
    }
}