package com.fluentbuild.pcas.utils

class LimitedQueue<ElementT>(private val limit: Int) {

	private val queue = ArrayDeque<ElementT>(limit)

	fun push(element: ElementT) {
		queue.addLast(element)
		if(queue.size >= limit) {
			queue.removeFirst()
		}
	}

	fun getElements(): Collection<ElementT> = queue
}