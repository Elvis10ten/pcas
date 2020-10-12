package com.fluentbuild.pcas.ledger

internal fun Collection<Block>.getBlocksMaxTimestamp() = maxOfOrNull { it.timestamp } ?: NO_TIMESTAMP

internal fun Set<Block>.getOnlyNewBlocks(hostBlocks: Set<Block>): Set<Block> {
	val newBlocks = mutableSetOf<Block>()

	for(hostBlock in hostBlocks) {
		if(this.none { it.isAllFieldsEqual(hostBlock) }) {
			newBlocks += hostBlock
		}
	}

	return newBlocks
}

internal fun Set<Block>.upsert(changedBlocks: Set<Block>) = (this - changedBlocks) + changedBlocks

internal const val NO_TIMESTAMP = 0L