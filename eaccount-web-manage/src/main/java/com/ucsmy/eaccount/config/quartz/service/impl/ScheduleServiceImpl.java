package com.ucsmy.eaccount.config.quartz.service.impl;

import com.ucsmy.eaccount.config.quartz.service.ScheduleService;
import com.ucsmy.eaccount.config.quartz.util.ScheduleStatus;
import com.ucsmy.eaccount.config.quartz.util.ScheduleTaskNetwork;
import com.ucsmy.eaccount.manage.dao.ManageIpScheduleTaskDao;
import com.ucsmy.eaccount.manage.entity.ManageIpScheduleTask;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ManageIpScheduleTaskDao manageIpScheduleTaskDao;

    private Scheduler scheduler = null;

    /**
     * 暴露接口，唯一提供可调用的接口
     */
    @Override
    public void proceedSchedule() {

        try {
            this.proceed();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

    }

    //
    // 以下方法皆为 私有方法，请勿调用
    //

    private void proceed() throws Exception {

        List<ManageIpScheduleTask> list = manageIpScheduleTaskDao.find();

        if(null == list || list.isEmpty()) {
            return;
        }

        Set<String> currentScheduleCodeSet = this.getCurrentScheduleCodeSet();

        this.proceed(list, currentScheduleCodeSet);
    }


    /**
     * 细节调用
     * @param list
     * @param currentScheduleCodeSet
     * @throws Exception
     */
    private void proceed(List<ManageIpScheduleTask> list, Set<String> currentScheduleCodeSet) throws Exception {
        for(ManageIpScheduleTask scheduleTask : list) {
            if(null == scheduleTask) {
                continue;
            }

            if(ScheduleStatus.START.getValue().equals(scheduleTask.getStatus())) {//启用任务
                if(!this.isTaskCodeExist(scheduleTask.getTaskCode(), currentScheduleCodeSet)) {//未在进程中
                    this.startTask(scheduleTask);
                }

            } else if (ScheduleStatus.STOP.getValue().equals(scheduleTask.getStatus())) {//停用任务
                if(this.isTaskCodeExist(scheduleTask.getTaskCode(), currentScheduleCodeSet)) {//已在进程中
                    this.stopTask(scheduleTask);
                }
            }
        }
    }


    /**
     * 启用任务
     * @param scheduleTask
     * @throws Exception
     */
    private void startTask(ManageIpScheduleTask scheduleTask) throws Exception {

        if(!this.validate(scheduleTask)) {
            return;
        }

        ScheduleNaming naming = new ScheduleNaming(scheduleTask);
        Class<? extends Job> clazz = this.getJobClass(naming.getClassName());

        JobDetail jobDetail = this.getJobDetail(clazz, naming.getJobName());
        Trigger trigger = this.getTrigger(naming.getTriggerName(), naming.getScheduleConfiguration());

        this.scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 停用任务
     * @param scheduleTask
     * @throws Exception
     */
    private void stopTask(ManageIpScheduleTask scheduleTask) throws Exception {

        if(!this.validate(scheduleTask)) {
            return;
        }

        ScheduleNaming naming = new ScheduleNaming(scheduleTask);

        this.scheduler.unscheduleJob(TriggerKey.triggerKey(naming.getTriggerName(), Scheduler.DEFAULT_GROUP));
        this.scheduler.deleteJob(JobKey.jobKey(naming.getJobName(), Scheduler.DEFAULT_GROUP));
    }


    /**
     * 验证参数
     * @param schedule
     * @return
     */
    private boolean validate(ManageIpScheduleTask schedule) {
        if(null == schedule) {
            return false;
        } else if (null == schedule.getTaskCode()
                || null == schedule.getTaskName()
                || null == schedule.getTaskConf()
                || null == schedule.getTaskClass()
                || null == schedule.getTaskServerIp()) {
            return false;
        }

        if (!isLocalIp(schedule.getTaskServerIp())) {
            return false;
        }

        return true;
    }

    /**
     * 必须返回 继承 Job 的 Class
     * @param className
     * @return
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private Class<? extends Job> getJobClass(String className) throws ClassNotFoundException {
        return (Class<? extends Job>) Class.forName(className);
    }


    /**
     * 获取 jobDetail
     * @param clazz
     * @param jobName
     * @return
     */
    private JobDetail getJobDetail(Class<? extends Job> clazz, String jobName) {
        JobDetail jobDetail = newJob(clazz)
                .withIdentity(jobName, Scheduler.DEFAULT_GROUP)
                .build()
                ;

        return jobDetail;
    }


    /**
     * 获取 trigger
     * @param triggerName
     * @param conf
     * @return
     */
    private Trigger getTrigger(String triggerName, String conf) {
        CronTrigger trigger = newTrigger()
                .withIdentity(triggerName, Scheduler.DEFAULT_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(conf))
                .startNow()
                .build();

        return trigger;
    }


    /**
     * 判断当前传入 ip 是否是 本机的 ip
     * @param ip
     * @return
     */
    private boolean isLocalIp(String ip) {

        if(null != ip && ip.length() >= 0) {
            return ScheduleTaskNetwork.isExist(ip);
        }

        return false;
    }


    private boolean isTaskCodeExist(String taskCode, Set<String> set) {

        return set.contains(taskCode);
    }


    private Set<String> getCurrentScheduleCodeSet() throws Exception{

        Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(Scheduler.DEFAULT_GROUP));
        Set<String> scheduleCodeSet = new HashSet<String>();
        for(JobKey jobKey : jobKeySet) {
            scheduleCodeSet.add(jobKey.getName());
        }

        return scheduleCodeSet;

    }


    /**
     * 内部类，用来统一定义名称
     */
    class ScheduleNaming {
        private String taskCode = "taskCode";
        private String className;
        private String triggerName;
        private String jobName;
        private String scheduleConfiguration;

        ScheduleNaming(ManageIpScheduleTask schedule) {
            if(null != schedule.getTaskCode()) {
                this.taskCode = schedule.getTaskCode();
            }
            this.className = schedule.getTaskClass();
            this.triggerName = this.taskCode;
            this.jobName = this.taskCode;
            this.scheduleConfiguration = schedule.getTaskConf();
        }

        public String getClassName() {
            return className;
        }

        public String getTriggerName() {
            return triggerName;
        }

        public String getJobName() {
            return jobName;
        }

        public String getScheduleConfiguration() {
            return scheduleConfiguration;
        }
    }


    @Override
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }



}
