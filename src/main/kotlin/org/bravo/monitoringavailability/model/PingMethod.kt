package org.bravo.monitoringavailability.model

enum class PingMethod(val methodName: String) {
    HTTP("HTTP"),
    ICMP("ICMP")
}
