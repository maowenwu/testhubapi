package cn.huobi.framework.dao;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.TimedTask;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/8/008.
 * @author  liuks
 * 定时任务监控dao
 */
public interface TimedTaskDao {

    @SelectProvider(type=TimedTaskDao.SqlProvider.class,method="selectAllList")
    @ResultType(TimedTask.class)
    List<TimedTask> selectAllList(@Param("tim")TimedTask tim,@Param("page")Page<TimedTask> page);

    @Insert(
         "INSERT INTO timed_task" +
                 "(task_name,task_group,task_status,enabled_state,expression," +
                 " retrieval_time,abnormal_time,last_time,next_time, " +
                 " create_time,warning_state,error_warning_state,overtime_warning_state) " +
                 "VALUES " +
                 "(#{tim.taskName},#{tim.taskGroup},#{tim.taskStatus},#{tim.enabledState},#{tim.expression}," +
                 " #{tim.retrievalTime},#{tim.abnormalTime},#{tim.lastTime}, " +
                 " #{tim.nextTime},NOW(),0,0,0)"
    )
    int insertTimedTask(@Param("tim")TimedTask tim);

    @Update(
          "update timed_task set task_status=#{tim.taskStatus},expression=#{tim.expression}, " +
                  " retrieval_time=#{tim.retrievalTime},enabled_state=#{tim.enabledState}, " +
                  " last_time=#{tim.lastTime},next_time=#{tim.nextTime} " +
                  " where task_name=#{tim.taskName} and " +
                  " task_group=#{tim.taskGroup} "
    )
    int updateTimedTask(@Param("tim")TimedTask tim);

    @Update(
            "update timed_task set task_status=#{tim.taskStatus},expression=#{tim.expression}, " +
                    " retrieval_time=#{tim.retrievalTime},enabled_state=#{tim.enabledState}, " +
                    " last_time=#{tim.lastTime},next_time=#{tim.nextTime}, " +
                    "abnormal_time=#{tim.abnormalTime} " +
                    "where task_name=#{tim.taskName} and task_group=#{tim.taskGroup} "
    )
    int updateTimedTaskAbnormalTime(@Param("tim")TimedTask tim);

    @Select(
            "select * from timed_task where task_name=#{taskName} and task_group=#{taskGroup}"
    )
    TimedTask getTimedTask(@Param("taskName")String taskName,@Param("taskGroup")String taskGroup);

    @Update(
            "update timed_task set enabled_state=0 where 1=1 "
    )
    void clearTimedTaskState();

    @Update(
            "update timed_task set enabled_state=0 where task_name=#{taskName} and task_group=#{taskGroup}"
    )
    int updateEnabledState(@Param("taskName")String taskName,@Param("taskGroup")String taskGroup);

    @Select(
            " select * from timed_task where id=#{id} "
    )
    TimedTask getdetailTimedTask(@Param("id")int id);

    @Update(
            "update timed_task " +
                    " set warning_state=#{tim.warningState},error_warning_state=#{tim.errorWarningState}, " +
                    " overtime_warning_state=#{tim.overtimeWarningState},early_warning_threshold=#{tim.earlyWarningThreshold}, " +
                    " program_name=#{tim.programName},description=#{tim.description},remarks=#{tim.remarks} " +
                    " where id=#{tim.id}  "
    )
    int saveDetailTimedTask(@Param("tim")TimedTask tim);

    @Select(
            " select * from timed_task where warning_state=#{warningState}"
    )
    List<TimedTask> getTimedTaskByWarningState(@Param("warningState")int warningState);

    @Select(
            " select * from timed_task where warning_state=1 and error_warning_state=1 "
    )
    List<TimedTask> getTimedTaskList();

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param){
            final TimedTask tim = (TimedTask) param.get("tim");
            return new SQL(){{
                SELECT("*");
                FROM(" timed_task ");
                if(tim.getId()>0){
                    WHERE(" id=#{tim.id}");
                }
                if(StringUtils.isNotBlank(tim.getTaskName())) {
                    WHERE(" task_name=#{tim.taskName}");
                }
                if(StringUtils.isNotBlank(tim.getTaskGroup())) {
                    WHERE(" task_group=#{tim.taskGroup}");
                }
                if(StringUtils.isNotBlank(tim.getTaskStatus())) {
                    WHERE(" task_status=#{tim.taskStatus}");
                }
                if(tim.getEnabledState()>=0){
                    WHERE(" enabled_state=#{tim.enabledState}");
                }
                ORDER_BY("id asc");
            }}.toString();
        }
    }
}
