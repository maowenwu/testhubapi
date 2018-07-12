package cn.huobi.framework.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ListDataExcelExport {

	private String[] cols;
	private String[] colsName;
	private double[] cellWidth;
	private List<Map<String, String>> data;
	private Workbook wb;
	private Sheet sheet;
	private int rowIdx = 0;

	public ListDataExcelExport() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("Sheet1");
	}

	/**
	 * 默认导出最小的
	 * @param size
	 */
	public ListDataExcelExport(int size) {
		if(size == 2){
			wb = new XSSFWorkbook();
		} else {
			wb = new HSSFWorkbook();
		}
		sheet = wb.createSheet("Sheet1");
	}

	public String getFileSuffix() {
		return "xls";
	}

	public String getFileSuffix(int size) {
		if(size == 2){
			return ".xlsx";
		} else {
			return ".xls";
		}
	}

	public void export(String[] cols, String[] colsName,double[] cellWidth, List<Map<String, String>> data, OutputStream outputStream)
			throws IOException {
		this.cols = cols;
		this.data = data;
		this.colsName = colsName;
		this.cellWidth = cellWidth;
		builTitle();
		writeData();
		wb.write(outputStream);
		outputStream.flush();
	}
	
	public void export(String[] cols, String[] colsName, List<Map<String, String>> data, OutputStream outputStream) throws IOException {
		export(cols, colsName, null, data, outputStream);
	}

	/**
	 * 构建表格表头
	 * 
	 * @throws IOException
	 */
	private void builTitle() throws IOException {
		Cell cell = null;
		Row row = null;
		int cols = this.colsName.length;
		CellStyle headStyle = createHeaderStyle(wb);
		row = sheet.createRow(rowIdx);
		row.setHeightInPoints((short) 25);
		for (short i = 0; i < cols; i++) {
			double _cellWidth  = 6000 ;
			if (cellWidth != null) {
				_cellWidth = cellWidth[i];
			}
			sheet.setColumnWidth(i, (short) _cellWidth);
			cell = row.createCell(i);
			cell.setCellStyle(headStyle);
			// 定义单元格为字符串类型,不设置默认为“常规”
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(this.colsName[i]);
		}
	}

	private void writeData() throws IOException {
		Object cellValue;
		CellStyle bodyStyle = createBodyStyle(wb);
		for (int i = 0; i < data.size(); i++) {
			// 创建行
			Row row = sheet.createRow(++rowIdx);
			for (short j = 0; j < this.cols.length; j++) {
				Cell cell = row.createCell(j);
				// 定义单元格为字符串类型,不设置默认为“常规”
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(bodyStyle);
				cellValue = data.get(i).get(this.cols[j]);
				cell.setCellValue(cellValue == null ? "" : cellValue.toString());
			}
		}
	}

	/**
	 * 构建表格列头样式
	 * 
	 * @param wb
	 * @return
	 */
	private CellStyle createHeaderStyle(Workbook wb) {

		// 设置字体
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 12); // 字体高度
		font.setColor(Font.COLOR_NORMAL); // 字体颜色
		font.setFontName("黑体"); // 字体
		font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 宽度

		// 设置单元格样式
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 水平布局：居中
		// 边框
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);

		cellStyle.setWrapText(true);
		return cellStyle;

	}

	/**
	 * 构建表格列样式
	 * 
	 * @param wb
	 * @return
	 */
	private CellStyle createBodyStyle(Workbook wb) {

		// 设置单元格样式
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT); // 水平布局：居中
		// 边框
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);

		cellStyle.setWrapText(true);
		return cellStyle;
	}

}
