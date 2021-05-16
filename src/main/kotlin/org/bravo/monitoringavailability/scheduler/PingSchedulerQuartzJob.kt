package org.bravo.monitoringavailability.scheduler

import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class PingSchedulerQuartzJob : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        logger.info("Hello")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
