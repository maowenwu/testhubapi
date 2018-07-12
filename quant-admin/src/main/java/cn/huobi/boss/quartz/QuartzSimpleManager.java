package cn.huobi.boss.quartz;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.SimpleTriggerImpl;

/**
 * @Description: 定时任务管理类
 *  定时为每隔多少分钟执行一次
 * @ClassName: QuartzManager
 */
public class QuartzSimpleManager {
    @Resource
    private Scheduler scheduler ;

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 添加一个定时任务 
     */
    public  void addJob(AutoJobDTO job,Class classz) throws SchedulerException  {
        //这里获取任务信息数据
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJob_name(), job.getJob_group());
        SimpleTriggerImpl simpleTrigger = (SimpleTriggerImpl) scheduler.getTrigger(triggerKey);
        if(simpleTrigger==null){
            //不存在，创建一个
            JobDetail jobDetail = JobBuilder.newJob(classz).withIdentity(job.getJob_name(), job.getJob_group()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);
            //表达式调度构建器
            //按新的cronExpression表达式构建一个新的trigger
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatMinutelyForever(job.getJob_long_time().intValue());
            simpleTrigger = (SimpleTriggerImpl) TriggerBuilder.newTrigger().withIdentity(job.getJob_name(), job.getJob_group()).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail,simpleTrigger);
        }else{
            // Trigger已存在，那么更新相应的定时设置
            //表达式调度构建器
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatMinutelyForever(job.getJob_long_time().intValue());
            //按新的cronExpression表达式重新构建trigger
            simpleTrigger = (SimpleTriggerImpl) simpleTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, simpleTrigger);
        }
    }
    /**
     * 启动所有定时任务 
     */
    public  void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}  