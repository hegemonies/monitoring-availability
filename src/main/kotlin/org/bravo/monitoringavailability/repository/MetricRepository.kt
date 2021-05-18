package org.bravo.monitoringavailability.repository

import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.kotlin.InfluxDBClientKotlin
import org.bravo.monitoringavailability.configuration.properties.InfluxdbProperties
import org.bravo.monitoringavailability.model.PingPoint
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class MetricRepository(
    private val influxDBClient: InfluxDBClientKotlin,
    private val influxdbProperties: InfluxdbProperties
) {

    suspend fun save(point: PingPoint): Boolean {
        runCatching {
            influxDBClient.getWriteKotlinApi()
                .writeMeasurement(
                    measurement = point,
                    precision = WritePrecision.MS,
                    bucket = influxdbProperties.bucket,
                    org = influxdbProperties.org
                )
        }.getOrElse { error ->
            logger.error("Can not write point to influxdb: ${error.message}")

            return true
        }

        return true
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
