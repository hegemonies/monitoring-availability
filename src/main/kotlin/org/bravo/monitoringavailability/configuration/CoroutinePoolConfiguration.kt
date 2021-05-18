package org.bravo.monitoringavailability.configuration

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class CoroutinePoolConfiguration {

    @Bean
    fun coroutinePool(): CoroutineDispatcher {
        val pool = ThreadPoolTaskExecutor().apply {
            corePoolSize = 2
            maxPoolSize = 16
        }

        pool.afterPropertiesSet()

        return pool.asCoroutineDispatcher()
    }
}
