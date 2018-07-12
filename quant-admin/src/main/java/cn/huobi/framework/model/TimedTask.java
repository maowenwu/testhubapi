package cn.huobi.framework.model;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/8/008.
 * @author  liuks
 * 定时任务监控实体
 * 对应表 timed_task
 */
public class TimedTask {
    private int id;//id

    private String taskName;//定时任务名称

    private String taskGroup;//定时任务组

    private String programName;//程序名

    private String description;//程序描述

    private String taskStatus;//当前状态：NONE,NORMAL,PAUSED,COMPLETE,ERROR,BLOCKED

    private int  enabledState;//启动状态:0未启动,1启动

    private String expression;//表达式

    private Date retrievalTime;//检索时间

    private Date abnormalTime;//异常时间

    private Integer earlyWarningThreshold;//预警阀值,单位:分钟

    private Date createTime;//创建时间

    private Date lastTime;//上次执行时间

    private Date nextTime;//预计下次执行时间

    private String remarks;//运营备注

    private int  warningState;//是否预警:0否,1是

    private int errorWarningState;//服务停止预警开关:0否,1是

    private int overtimeWarningState;//超时预警开关:0否,1是

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getEnabledState() {
        return enabledState;
    }

    public void setEnabledState(int enabledState) {
        this.enabledState = enabledState;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Date getRetrievalTime() {
        return retrievalTime;
    }

    public void setRetrievalTime(Date retrievalTime) {
        this.retrievalTime = retrievalTime;
    }

    public Date getAbnormalTime() {
        return abnormalTime;
    }

    public void setAbnormalTime(Date abnormalTime) {
        this.abnormalTime = abnormalTime;
    }

    public Integer getEarlyWarningThreshold() {
        return earlyWarningThreshold;
    }

    public void setEarlyWarningThreshold(Integer earlyWarningThreshold) {
        this.earlyWarningThreshold = earlyWarningThreshold;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getWarningState() {
        return warningState;
    }

    public void setWarningState(int warningState) {
        this.warningState = warningState;
    }

    public int getErrorWarningState() {
        return errorWarningState;
    }

    public void setErrorWarningState(int errorWarningState) {
        this.errorWarningState = errorWarningState;
    }

    public int getOvertimeWarningState() {
        return overtimeWarningState;
    }

    public void setOvertimeWarningState(int overtimeWarningState) {
        this.overtimeWarningState = overtimeWarningState;
    }
}
