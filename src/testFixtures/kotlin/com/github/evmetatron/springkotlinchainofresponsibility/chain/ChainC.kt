package com.github.evmetatron.springkotlinchainofresponsibility.chain

class ChainC(
    private val chainInterface: ChainInterface?,
) : ChainInnerInterface {
    override fun next(): ChainInterface? = chainInterface
}
