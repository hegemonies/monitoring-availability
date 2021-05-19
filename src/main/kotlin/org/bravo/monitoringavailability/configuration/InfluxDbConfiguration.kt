package org.bravo.monitoringavailability.configuration

import com.influxdb.LogLevel
import com.influxdb.client.kotlin.InfluxDBClientKotlin
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import org.bravo.monitoringavailability.configuration.properties.InfluxdbProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InfluxDbConfiguration(
    private val influxdbProperties: InfluxdbProperties
) {

    @Bean
    fun influxdbClient(): InfluxDBClientKotlin {
        return InfluxDBClientKotlinFactory.create(
            url = influxdbProperties.url,
            username = influxdbProperties.username,
            password = influxdbProperties.password.toCharArray()
        ).enableGzip().setLogLevel(LogLevel.valueOf(influxdbProperties.logLevel))
    }
}
