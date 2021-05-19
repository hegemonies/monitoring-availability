package org.bravo.monitoringavailability.service

import org.bravo.monitoringavailability.configuration.properties.IcmpPingProperties
import org.icmp4j.IcmpPingRequest
import org.icmp4j.IcmpPingUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.system.measureTimeMillis

@Service
class IcmpPingService(
    private val icmpPingProperties: IcmpPingProperties
) {

    fun ping(): Pair<Long, Boolean> =
        pingTo(icmpPingProperties.host)

    fun pingTo(ip: String): Pair<Long, Boolean> {
        val request = createRequest(ip)

        var available = false

        val elapsed = measureTimeMillis {
            available = executePing(request)
        }

        return elapsed to available
    }

    private fun createRequest(ip: String): IcmpPingRequest =
        IcmpPingUtil.createIcmpPingRequest().apply {
            timeout = icmpPingProperties.timeout
            ttl = icmpPingProperties.ttl
            host = ip
            packetSize = icmpPingProperties.packetSize
        }

    private fun executePing(request: IcmpPingRequest): Boolean {
        repeat(icmpPingProperties.countRepeat) {
            val response = runCatching {
                IcmpPingUtil.executePingRequest(request)
            }.getOrElse { error ->
                logger.warn("Can not ping to ${request.host}: ${error.message}")
                return false
            }

            if (response.successFlag) {
                return true
            }
        }

        return false
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
