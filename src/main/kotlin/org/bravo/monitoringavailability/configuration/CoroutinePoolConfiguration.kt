package org.bravo.monitoringavailability.configuration

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class CoroutinePoolConfiguration {

    @ObsoleteCoroutinesApi
    @Bean
    fun coroutinePool(): CoroutineDispatcher =
        newFixedThreadPoolContext(4, "c-pool")
}
