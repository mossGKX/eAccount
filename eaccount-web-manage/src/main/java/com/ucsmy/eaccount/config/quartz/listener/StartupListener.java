package com.ucsmy.eaccount.config.quartz.listener;

import com.ucsmy.core.utils.StringUtils;
import com.ucsmy.eaccount.config.quartz.service.ScheduleService;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 定时任务扫描Listener
 * @author  gaokx
 */
@WebListener
@ConfigurationProperties(prefix = "quartz")
public class StartupListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupListener.class);

    private String scan;

    private ScheduleService scheduleService;

    private static final String SCHEDULE_SERVICE_NAME = "scheduleService";
    private static final String SCHEDULER_NAME = "scheduler";

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            this.initial(servletContextEvent);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String getScan() {
        return scan;
    }

    public void setScan(String scan) {
        this.scan = scan;
    }

    /**
     * 用来启动定时器的
     * @param servletContextEvent
     */
    private void initial(ServletContextEvent servletContextEvent) {
        Boolean isScan;
        if (StringUtils.isEmpty(this.getScan())) {
            isScan = true;
        } else {
            isScan = Boolean.parseBoolean(this.getScan());
        }
        if (isScan) {
            ServletContext context = servletContextEvent.getServletContext();
            ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
            this.scheduleService = (ScheduleService) applicationContext.getBean(SCHEDULE_SERVICE_NAME);
            Scheduler scheduler = (Scheduler) applicationContext.getBean(SCHEDULER_NAME);
            this.scheduleService.setScheduler(scheduler);
            this.scheduleService.proceedSchedule();
        }
    }

}
