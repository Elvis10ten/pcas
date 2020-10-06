package com.fluentbuild.pcas.utils

class LimitedQueue<ElementT>(private val limit: Int) {

	private val elements = ArrayDeque<ElementT>(limit)

	fun push(element: ElementT) {
		elements.addLast(element)
		if(elements.size >= limit) {
			elements.removeFirst()
		}
	}

	fun getElements(): Collection<ElementT> = elements
}