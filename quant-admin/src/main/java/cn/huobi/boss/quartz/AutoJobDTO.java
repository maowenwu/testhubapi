package cn.huobi.boss.quartz;

public class AutoJobDTO {
	private String job_id;    
    private String job_name;      
    private String job_group;  
    private String job_time;
    private Long job_long_time;
	public String getJob_id() {
		return job_id;
	}
	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}
	public String getJob_name() {
		return job_name;
	}
	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}
	public String getJob_group() {
		return job_group;
	}
	public void setJob_group(String job_group) {
		this.job_group = job_group;
	}
	public String getJob_time() {
		return job_time;
	}
	public void setJob_time(String job_time) {
		this.job_time = job_time;
	}
	public Long getJob_long_time() {
		return job_long_time;
	}
	public void setJob_long_time(Long job_long_time) {
		this.job_long_time = job_long_time;
	}
    
    
}
