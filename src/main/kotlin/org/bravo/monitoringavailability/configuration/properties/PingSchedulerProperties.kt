package org.bravo.monitoringavailability.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "scheduler")
data class PingSchedulerProperties(
    val pingInterval: Duration = Duration.ofMillis(2000)
)
