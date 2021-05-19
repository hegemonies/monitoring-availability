package org.bravo.monitoringavailability.configuration.properties

import com.influxdb.LogLevel
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.validation.constraints.NotBlank

@ConstructorBinding
@ConfigurationProperties(prefix = "influxdb")
data class InfluxdbProperties(

    @field:NotBlank
    val url: String,

    @field:NotBlank
    val username: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val bucket: String,

    @field:NotBlank
    val org: String,

    val logLevel: String = LogLevel.NONE.name
)
