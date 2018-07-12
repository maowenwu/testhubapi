package cn.huobi.framework.util;

import org.apache.poi.ss.usermodel.*;

/**
 * 生成行
 * Created by Administrator on 2017/5/24.
 */
public abstract class CreateRow<T> {

    /**
     * 设置列宽度
     * @param sheet sheet
     * @param columnCount 列的个数
     */
    public void setColumnWidth(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i ++){
            sheet.setColumnWidth(i, (int)35.7 * 150);
        }
    }
    /**
     * 配置表头单元格样式
     * @param headFont headFont
     * @param headCellStyle headCellStyle
     */
    public void configHeadRowCellStyle(Font headFont, CellStyle headCellStyle) {
        headFont.setFontHeightInPoints((short) 16);
        headFont.setFontName("宋体");
        headFont.setColor(IndexedColors.BLACK.getIndex());
        headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        headCellStyle.setFont(headFont);
        headCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        headCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    }

    /**
     * 配置单元格的样式
     * @param rowFont   rowFont
     * @param oddCellStyle 奇数行
     * @param evenCellStyle 偶数行
     */
    public void configRowCellStyle(Font rowFont, CellStyle oddCellStyle, CellStyle evenCellStyle) {
        rowFont.setFontHeightInPoints((short) 12);
        rowFont.setFontName("宋体");
        rowFont.setColor(IndexedColors.BLACK.getIndex());

        evenCellStyle.setFont(rowFont);
        evenCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        evenCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        evenCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        oddCellStyle.setFont(rowFont);
        oddCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        oddCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        oddCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
    }
    /**
     * 配置标题行的样式
     * @param headRow 标题行
     */
    public void configHeadRowStyle(Row headRow) {

    }
    /**
     * 配置行的样式
     * @param index 第几行
     * @param row 行
     */
    public void configRowStyle(int index, Row row) {

    }

    public abstract void writeRow(Row row, T t);
}
