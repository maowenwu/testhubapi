package cn.huobi.framework.util;

/**
 * Excel解析数据信息
 * Created by Administrator on 2017/5/24.
 */
public class ExcelErrorMsgBean{
    private int row;
    private int line;
    private String cellName;
    private String message;
    private static String lineNames = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public ExcelErrorMsgBean(int row, int line, String message) {
        this.row = row;
        this.line = line;
        this.message = message;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCellName(){
        this.cellName = getLineName(line) + this.row;
        return this.cellName;
    }

    private String getLineName(int index){
        if (index < 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        while (index >= lineNames.length()){
            int temp = index % lineNames.length();
            sb.append(lineNames.charAt(temp));
            index = index / lineNames.length();
        }
        sb.append(lineNames.charAt(index));
        return sb.reverse().toString();
    }

    @Override
    public String toString() {
        return "ExcelErrorMsgBean{" +
                "row=" + row +
                ", line=" + line +
                ", cellName='" + cellName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
