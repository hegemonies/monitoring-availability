package org.bravo.monitoringavailability.init

import com.influxdb.client.domain.HealthCheck
import com.influxdb.client.kotlin.InfluxDBClientKotlin
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class InfluxdbHealthCheck(
    private val influxDBClient: InfluxDBClientKotlin
) {

    @EventListener(ApplicationReadyEvent::class)
    fun check() {
        val healthCheck = if (influxDBClient.health().status == HealthCheck.StatusEnum.PASS) "ok" else "bad"
        logger.info(
            "Influxdb health is $healthCheck"
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
