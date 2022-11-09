package com.github.evmetatron.springkotlinchainofresponsibility.chain

class ChainD(
    private val chainInterface: ChainInterface?,
) : ChainAbstract() {
    override fun next(): ChainInterface? = chainInterface
}
