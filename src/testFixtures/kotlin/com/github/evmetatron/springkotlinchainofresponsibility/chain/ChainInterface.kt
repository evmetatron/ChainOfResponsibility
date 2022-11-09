package com.github.evmetatron.springkotlinchainofresponsibility.chain

interface ChainInterface {
    fun next(): ChainInterface?
}
