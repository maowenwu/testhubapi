package cn.huobi.boss.quartz;  
  
import javax.annotation.Resource;

import org.quartz.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/** 
 * @Description: 定时任务管理类 
 *  
 * @ClassName: QuartzManager 
 */  
public class QuartzManager {  
	@Resource  
    private Scheduler scheduler ;  
      
    public Scheduler getScheduler() {  
        return scheduler;  
    }  
  
    public void setScheduler(Scheduler scheduler) {  
        this.scheduler = scheduler;  
    }  

    /**
     *添加一个定时任务
     */
    public  void addJob(AutoJobDTO job,Class classz) throws SchedulerException  {
          //这里获取任务信息数据
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJob_name(), job.getJob_group());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if(trigger==null){
                //不存在，创建一个
                JobDetail jobDetail = newJob(classz).withIdentity(job.getJob_name(), job.getJob_group()).build();

                jobDetail.getJobDataMap().put("scheduleJob", job);
                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = cronSchedule(job.getJob_time());
                //按新的cronExpression表达式构建一个新的trigger
                trigger = newTrigger().withIdentity(job.getJob_name(), job.getJob_group()).withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail,trigger);

            }else{
                // Trigger已存在，那么更新相应的定时设置
                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = cronSchedule(job.getJob_time());
                //按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
    }
    /**
     * 只涮新定时周期
     */
    public void  rinseTheNew(AutoJobDTO job) throws SchedulerException  {
        //这里获取任务信息数据
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJob_name(), job.getJob_group());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(trigger!=null){
            // Trigger已存在，那么更新相应的定时设置
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = cronSchedule(job.getJob_time());
            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }
    /** 
     * 启动所有定时任务 
     */  
    public  void startJobs() {  
        try {
            scheduler.startDelayed(60);
            scheduler.start();  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException(e);  
        }  
    }

    public void addJob(Trigger trigger, Class clazz) throws SchedulerException {
        TriggerKey key = trigger.getKey();
        Trigger OldTrigger = scheduler.getTrigger(trigger.getKey());
        if (OldTrigger == null) {
            JobKey jobKey = JobKey.jobKey(key.getName(), key.getGroup());
            JobDetail job = newJob(clazz)
                    .withIdentity(jobKey)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } else {
            scheduler.rescheduleJob(key,trigger);
        }
    }
    /**
     * 移除一个任务
     * liuks
     */
    public  void removeJob(AutoJobDTO job){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJob_name(), job.getJob_group());
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if(trigger!=null){
                scheduler.pauseTrigger(triggerKey);// 停止触发器
                scheduler.unscheduleJob(triggerKey);// 移除触发器
                JobKey jobkey=new JobKey(job.getJob_name(), job.getJob_group());
                scheduler.deleteJob(jobkey);// 删除任务
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Trigger bulidSimpleTirgger(String name, String group,int delayed){
       return newTrigger()
               .withIdentity(name, group)
               .withSchedule(simpleSchedule()
                       .withIntervalInSeconds(delayed)
                       .repeatForever())
               .startNow()
               .build();
    }

    public Trigger bulidCronTirgger(String name, String group,String expression){
        return newTrigger()
                .withIdentity(name, group)
                .withSchedule(cronSchedule(expression))
                .startNow()
                .build();
    }

}  