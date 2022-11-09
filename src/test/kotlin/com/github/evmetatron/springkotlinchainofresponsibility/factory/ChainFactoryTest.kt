package com.github.evmetatron.springkotlinchainofresponsibility.factory

import com.github.evmetatron.springkotlinchainofresponsibility.chain.ChainA
import com.github.evmetatron.springkotlinchainofresponsibility.chain.ChainB
import com.github.evmetatron.springkotlinchainofresponsibility.chain.ChainC
import com.github.evmetatron.springkotlinchainofresponsibility.chain.ChainD
import com.github.evmetatron.springkotlinchainofresponsibility.chain.ChainInterface
import com.github.evmetatron.springkotlinchainofresponsibility.chain.ExampleDependency
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ConfigurableApplicationContext

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class ChainFactoryTest {
    @MockK
    private lateinit var applicationContext: ConfigurableApplicationContext

    private lateinit var chainFactory: ChainFactory

    @BeforeAll
    fun beforeAll() {
        chainFactory = ChainFactory("com.github.evmetatron.springkotlinchainofresponsibility.chain")

        val exampleDependency = ExampleDependency()

        every {
            applicationContext.autowireCapableBeanFactory.getBean(ExampleDependency::class.java)
        } returns exampleDependency

        chainFactory.setApplicationContext(applicationContext)
    }
    @Test
    fun `test chains with sorts`() {
        val chains: ChainInterface? = chainFactory.createChain(
            listOf(
                ChainB::class,
                ChainD::class,
                ChainA::class,
            ),
        )

        var chain = chains

        chain!!::class shouldBe ChainB::class

        (chain as ChainB).getText("It is ChainB") shouldBe "It is ChainB"

        chain = chain.next()

        chain!!::class shouldBe ChainD::class

        chain = chain.next()

        chain!!::class shouldBe ChainA::class

        (chain as ChainA).getText("It is ChainB") shouldBe "It is ChainB"

        chain = chain.next()

        chain!!::class shouldBe ChainC::class

        chain = chain.next()

        chain shouldBe null
    }
}
