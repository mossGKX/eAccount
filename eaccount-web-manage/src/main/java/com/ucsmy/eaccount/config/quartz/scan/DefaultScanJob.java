package com.ucsmy.eaccount.config.quartz.scan;


import com.ucsmy.eaccount.config.quartz.service.ScheduleService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  @author  gaokx
 *
 */
public class DefaultScanJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultScanJob.class);

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        LOGGER.debug("starting default scan job...");
        try {
            this.scheduleService.proceedSchedule();
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }

    }

}
