package org.bravo.monitoringavailability.scheduler

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bravo.monitoringavailability.configuration.properties.HttpPingProperties
import org.bravo.monitoringavailability.configuration.properties.IcmpPingProperties
import org.bravo.monitoringavailability.model.PingMethod
import org.bravo.monitoringavailability.model.PingPoint
import org.bravo.monitoringavailability.repository.MetricRepository
import org.bravo.monitoringavailability.service.HttpPingService
import org.bravo.monitoringavailability.service.IcmpPingService
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class PingSchedulerQuartzJob(
    private val metricsRepository: MetricRepository,
    private val httpPingService: HttpPingService,
    private val httpPingProperties: HttpPingProperties,
    private val icmpPingService: IcmpPingService,
    private val icmpPingProperties: IcmpPingProperties,
    private val coroutineDispatcher: CoroutineDispatcher
) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        logger.debug("Start ping scheduler")

        CoroutineScope(context = coroutineDispatcher).launch {
            executeHttpPing()
        }

        CoroutineScope(context = coroutineDispatcher).launch {
            executeIcmpPing()
        }
    }

    suspend fun executeHttpPing() {
        logger.debug("Execute HTTP ping")

        val (elapsedTime, available) = httpPingService.ping()

        val point = PingPoint(
            host = httpPingProperties.url,
            responseTime = elapsedTime,
            available = available,
            time = Instant.now(),
            method = PingMethod.HTTP.methodName
        )

        if (!metricsRepository.save(point)) {
            logger.debug("Save HTTP point measure is bad")
        }

        logger.debug("Save HTTP point measure is good")
    }

    suspend fun executeIcmpPing() {
        logger.debug("Execute ICMP ping")

        val (elapsedTime, available) = icmpPingService.ping()

        val point = PingPoint(
            host = icmpPingProperties.host,
            responseTime = elapsedTime,
            available = available,
            time = Instant.now(),
            method = PingMethod.ICMP.methodName
        )

        if (!metricsRepository.save(point)) {
            logger.debug("Save ICMP point measure is bad")
        }

        logger.debug("Save ICMP point measure is good")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
