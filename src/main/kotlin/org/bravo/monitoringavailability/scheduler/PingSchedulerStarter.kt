package org.bravo.monitoringavailability.scheduler

import org.bravo.monitoringavailability.configuration.properties.PingSchedulerProperties
import org.quartz.CalendarIntervalScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component

@Component
class PingSchedulerStarter(
    private val schedulerFactory: SchedulerFactoryBean,
    private val pingSchedulerProperties: PingSchedulerProperties
) {

    @EventListener(ApplicationReadyEvent::class)
    fun start() {
        val job = JobBuilder.newJob(PingSchedulerQuartzJob::class.java)
            .withIdentity("ping scheduler quartz job")
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .startNow()
            .withSchedule(
                CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                    .withIntervalInSeconds(pingSchedulerProperties.pingInterval.toSeconds().toInt())
            )
            .forJob(job)
            .build()

        schedulerFactory.scheduler.scheduleJob(job, trigger)
    }
}
