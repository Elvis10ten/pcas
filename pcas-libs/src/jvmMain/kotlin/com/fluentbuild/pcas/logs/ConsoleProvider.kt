package com.fluentbuild.pcas.logs

import java.util.ArrayDeque

object ConsoleProvider {

	val messages = ArrayDeque<String>(MAX_CONSOLE_MESSAGE_SIZE)

	init {
		ConsolePublisher.observer = ::addMessage
	}

	private fun addMessage(message: String) {
		synchronized(messages) {
			messages.offer(message)
			if(messages.size >= MAX_CONSOLE_MESSAGE_SIZE) {
				messages.removeFirst()
			}
		}
	}
}