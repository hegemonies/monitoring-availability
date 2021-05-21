package org.bravo.monitoringavailability.model

import com.influxdb.annotations.Column
import com.influxdb.annotations.Measurement
import java.time.Instant

@Measurement(name = "ping")
data class PingPoint(
    @Column(tag = true)
    val host: String,

    @Column(name = "response-time")
    val responseTime: Long,

    @Column
    val available: Boolean,

    @Column(timestamp = true)
    val time: Instant,

    @Column(tag = true)
    val method: String
) {

    companion object {
        fun getMeasurementName() = "ping"
    }
}
