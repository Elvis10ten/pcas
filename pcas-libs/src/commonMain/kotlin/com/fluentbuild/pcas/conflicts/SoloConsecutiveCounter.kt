package com.fluentbuild.pcas.conflicts

class SoloConsecutiveCounter<KeyT> {

	private var key: KeyT? = null
	private var count = 0

	fun count(key: KeyT, predicate: () -> Boolean) {
		if(this.key == key) {
			count++
		} else {
			this.key = key
			count = 0
		}
	}

	operator fun get(key: KeyT) = count

	fun reset() {
		key = null
		count = 0
	}
}