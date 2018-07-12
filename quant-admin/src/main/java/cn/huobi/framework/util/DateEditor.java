package cn.huobi.framework.util;

import org.springframework.util.StringUtils;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateEditor extends PropertyEditorSupport {

    private static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat TIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DateFormat dateFormat;
    private boolean allowEmpty = true;

    public DateEditor() {
    }

    public DateEditor(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateEditor(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
    }

    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else {
            try {
            	boolean isUtc=false;
                if(this.dateFormat != null)
                    setValue(this.dateFormat.parse(text));
                else {
                	text=text.replace("\"", "");
                	text=text.replace("T", " ");
                	if(text.contains("Z"))
                		isUtc=true;
                	text=text.replace("Z", "");
                    if(text.contains(":"))
                        setValue(TIMEFORMAT.parse(text));
                    else
                        setValue(DATEFORMAT.parse(text));
                    if(isUtc){
	                    Calendar c = Calendar.getInstance();
						c.setTime(((Date)getValue()));
	                    c.add(Calendar.HOUR_OF_DAY, 8);
	                    setValue(c.getTime());
	                    System.out.println(getValue());
                    }
                }
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        DateFormat dateFormat = this.dateFormat;
        if(dateFormat == null)
            dateFormat = TIMEFORMAT;
        return (value != null ? dateFormat.format(value) : "");
    }
}

