package cn.huobi.framework.exception;

/**
 * Created by Administrator on 2017/12/28/028.
 * @author liuks
 * 时间异常类
 */
public class DateException  extends RuntimeException{
    private String code;//code码
    private String msg;//异常信息

    public DateException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMessage(){
        return "错误码:"+code+",异常信息:"+msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
