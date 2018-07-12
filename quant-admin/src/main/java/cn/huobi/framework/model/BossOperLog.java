package cn.huobi.framework.model;

import java.io.Serializable;
import java.util.Date;

/**
 * boss操作日志
 * @author YeXiaoMing
 * @date 2016年12月13日上午11:21:09
 */
public class BossOperLog implements Serializable {

	private static final long serialVersionUID = 6136557820607183872L;

	//日志编号
	private Integer id;
	
	//操作用户
	private Integer user_id;
	
	//操作人名称
	private String user_name;
	
	//菜单代码
	private String oper_code;
	
	//请求方法
	private String request_method;
	
	//请求参数
	private String request_params;
	
	//返回结果
	private String return_result;
	
	//方法描述
	private String method_desc;
	
	//请求ip
	private String oper_ip;
	
	//操作状态(1：成功0：失败)
	private Integer oper_status;
	
	//操作时间
	private Date oper_time;
	
	private Date oper_start_time;
	
	private Date oper_end_time;
	
	private String menu_name;
	
	private Integer parent_menu_id;
	
	private Integer menu_id;
	
	private Integer oper_id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getRequest_method() {
		return request_method;
	}

	public void setRequest_method(String request_method) {
		this.request_method = request_method;
	}

	public String getRequest_params() {
		return request_params;
	}

	public void setRequest_params(String request_params) {
		this.request_params = request_params;
	}

	public String getReturn_result() {
		return return_result;
	}

	public void setReturn_result(String return_result) {
		this.return_result = return_result;
	}

	public String getMethod_desc() {
		return method_desc;
	}

	public void setMethod_desc(String method_desc) {
		this.method_desc = method_desc;
	}

	public String getOper_ip() {
		return oper_ip;
	}

	public void setOper_ip(String oper_ip) {
		this.oper_ip = oper_ip;
	}

	public Integer getOper_status() {
		return oper_status;
	}

	public void setOper_status(Integer oper_status) {
		this.oper_status = oper_status;
	}

	public Date getOper_time() {
		return oper_time;
	}

	public void setOper_time(Date oper_time) {
		this.oper_time = oper_time;
	}

	public String getOper_code() {
		return oper_code;
	}

	public void setOper_code(String oper_code) {
		this.oper_code = oper_code;
	}

	public Date getOper_start_time() {
		return oper_start_time;
	}

	public void setOper_start_time(Date oper_start_time) {
		this.oper_start_time = oper_start_time;
	}

	public Date getOper_end_time() {
		return oper_end_time;
	}

	public void setOper_end_time(Date oper_end_time) {
		this.oper_end_time = oper_end_time;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public Integer getParent_menu_id() {
		return parent_menu_id;
	}

	public void setParent_menu_id(Integer parent_menu_id) {
		this.parent_menu_id = parent_menu_id;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	public Integer getOper_id() {
		return oper_id;
	}

	public void setOper_id(Integer oper_id) {
		this.oper_id = oper_id;
	}

	
	
}