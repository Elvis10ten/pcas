package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.async.Debouncer
import com.fluentbuild.pcas.async.ThreadRunner

internal abstract class AsyncModule {

	abstract val threadRunnerProvider: () -> ThreadRunner

	val debouncerProvider = { delayMillis: Int -> Debouncer(threadRunnerProvider(), delayMillis) }

	abstract fun release()
}