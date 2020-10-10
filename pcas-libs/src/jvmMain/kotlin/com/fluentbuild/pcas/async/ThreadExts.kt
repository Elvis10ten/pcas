package com.fluentbuild.pcas.async

internal fun requireNotInterrupted() {
	if(Thread.currentThread().isInterrupted) {
		throw InterruptedException("Thread is interrupted")
	}
}