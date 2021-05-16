package org.bravo.monitoringavailability

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class MonitoringavailabilityApplication

fun main(args: Array<String>) {
    runApplication<MonitoringavailabilityApplication>(*args)
}
