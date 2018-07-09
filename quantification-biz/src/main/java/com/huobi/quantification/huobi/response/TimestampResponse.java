package com.huobi.quantification.huobi.response;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shaoxiaofeng
 * @since 2018/6/29
 */

public class TimestampResponse {

    /**
     * status : ok
     * data : 1494900087029
     */

    private String status;
    private long data;
    private String dateTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        Date date = new Date(data);
        return sdf.format(date);
    }
}
