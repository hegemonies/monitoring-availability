package org.bravo.monitoringavailability.repository

import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.kotlin.InfluxDBClientKotlin
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.toList
import org.bravo.monitoringavailability.configuration.properties.InfluxdbProperties
import org.bravo.monitoringavailability.model.PingMethod
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
                    precision = WritePrecision.NS,
                    bucket = influxdbProperties.bucket,
                    org = influxdbProperties.org
                )
        }.getOrElse { error ->
            logger.error("Can not write point to influxdb: ${error.message}")

            return true
        }

        return true
    }

    suspend fun findByMethod(method: PingMethod): List<PingPoint>? {
        val result = runCatching {
            influxDBClient.getQueryKotlinApi().query(
                query = "from(bucket: \"${influxdbProperties.bucket}\")" +
                        "|> range(start: -1h) " +
                        "|> filter(fn: (r) => (r._measurement == \"${PingPoint.getMeasurementName()}\" " +
                        "and r._field == \"response-time\") " +
                        "|> filter(fn: (r) => (r[\"${PingPoint::method.name}\"] == \"${method.methodName}\")",
                measurementType = PingPoint::class.java
            )
        }.getOrElse { error ->
            logger.error("Can not find points by method: ${error.message}")
            return null
        }

        return result.consumeAsFlow().toList()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
