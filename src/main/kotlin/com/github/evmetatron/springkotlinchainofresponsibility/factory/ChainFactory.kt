package com.github.evmetatron.springkotlinchainofresponsibility.factory

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class ChainFactory(val basePackage: String) : ApplicationContextAware {
    lateinit var applicationContext: ConfigurableApplicationContext

    inline fun <reified T : Any> createChain(chains: List<KClass<*>> = emptyList()): T? {
        var prev: T? = null

        val provider = ClassPathScanningCandidateComponentProvider(false)
        provider.addIncludeFilter(AssignableTypeFilter(T::class.java))

        val allChains = provider.findCandidateComponents(basePackage)
            .map { component ->
                Class.forName(component.beanClassName).kotlin
            }

        val prepareChains = (chains + allChains).distinct().reversed()

        prepareChains.forEach { clazz ->
            clazz.primaryConstructor?.parameters
                ?.map { parameter ->
                    if (parameter.type.jvmErasure.java == T::class.java) {
                        prev
                    } else if (ApplicationContext::class.isSubclassOf(parameter.type.jvmErasure)) {
                        applicationContext
                    } else {
                        applicationContext.autowireCapableBeanFactory.getBean(parameter.type.jvmErasure.java)
                    }
                }
                ?.apply {
                    @Suppress("SpreadOperator")
                    prev = clazz.primaryConstructor?.call(*this.toTypedArray()) as T?
                }
        }

        return prev
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext as ConfigurableApplicationContext
    }
}
