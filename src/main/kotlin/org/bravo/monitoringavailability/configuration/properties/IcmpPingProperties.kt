package org.bravo.monitoringavailability.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "ping.icmp")
class IcmpPingProperties(

    @field:Min(100)
    @field:Max(5000)
    val timeout: Long = 500,

    @field:NotBlank
    val host: String,

    @field:Min(1)
    @field:Max(65353)
    val ttl: Int = 255,

    @field:Min(1)
    @field:Max(256)
    val packetSize: Int = 32,

    @field:Min(1)
    @field:Max(10)
    val countRepeat: Int = 3
)
