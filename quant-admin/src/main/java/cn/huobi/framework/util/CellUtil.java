package cn.huobi.framework.util;

import org.apache.poi.ss.usermodel.Cell;

/**
 * cell的工具类，整理部分公共方法
 * @author tans
 * @date 2017-12-5
 */
public class CellUtil {

    public static String getCellValue(Cell cell) {
        if(cell!=null){
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case Cell.CELL_TYPE_STRING:
                    return cell.getStringCellValue();
                case Cell.CELL_TYPE_BLANK:
                    return "";
                case Cell.CELL_TYPE_BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case Cell.CELL_TYPE_FORMULA:
                    return cell.getStringCellValue();
            }
        }
        return null;
    }
}
