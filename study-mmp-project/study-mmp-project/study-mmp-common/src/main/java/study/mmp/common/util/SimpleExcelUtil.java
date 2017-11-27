package study.mmp.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import study.mmp.common.annotaion.ExcelField;



@Component
public class SimpleExcelUtil {

	static FastDateFormat fileNameFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");
    static FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public static void writeToHttpServeltResponse(List<?> contents, HttpServletResponse response) throws IOException {

        writeToHttpServeltResponse(contents, fileNameFormat.format(new Date()), response);
    }
    
    public static void writeToHttpServeltResponse(List<?> contents, String fileName, HttpServletResponse response) throws IOException {
    	SXSSFWorkbook wb = makeWorkbook(contents);
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName + "_" + formatter.format(date) + ".xlsx", "UTF-8").replaceAll("\\+", "%20") + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        write(wb, response.getOutputStream());
    }
    
    public static void writeToOutputStrem(List<?> contents, File file) throws IOException {
    	if (!file.isFile()) {
    		throw new RuntimeException("Not found file");
    	}
    	OutputStream os = new FileOutputStream(file);
    	writeToOutputStrem(contents, os);
    }
    
    public static void writeToOutputStrem(List<?> contents, OutputStream output) throws IOException {
    	SXSSFWorkbook wb = makeWorkbook(contents);
        write(wb, output);
    }
    
    
    
    
    
    private static SXSSFWorkbook makeWorkbook(List<?> contents) {
    	SXSSFWorkbook wb = new SXSSFWorkbook(200);
        if (contents.size() == 0) {
            return wb;
        }
        CellStyle style = getHeaderStyle(wb);
        
        Sheet sheet = wb.createSheet();
        
        Set<Map.Entry<Integer, String>> headers = getExcelHeaders(contents.get(0));
        setHeaderCell(sheet, headers, style);

        for (int i = 0; i < contents.size(); i++) {
            Set<Map.Entry<Integer, Object>> dataSet = getRowData(i, contents);
            setContentCell(sheet, i, dataSet);
        }
        
        for (int i = 0; i < headers.size(); i++) {
        	   sheet.autoSizeColumn(i);
               int width = sheet.getColumnWidth(i) * 3 / 2;
               width = width < 2100 ? 2100 : width;
               width = width > 255 * 256 ? 255 * 256 : width;
               sheet.setColumnWidth(i, width);
        }
        
        return wb;
    }
    
    
    private static CellStyle getHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }
    
    private static void setHeaderCell(Sheet sheet, Set<Map.Entry<Integer, String>> headerSet, CellStyle style) {
        
        Row headerRow = sheet.createRow(0);
        for (Map.Entry<Integer, String> headers : headerSet) {
            Cell cell = headerRow.createCell(headers.getKey());
            cell.setCellStyle(style);
            cell.setCellValue(headers.getValue());
        }
    }
    
    
    private static void setContentCell(Sheet sheet, int index, Set<Map.Entry<Integer, Object>> dataSet) {
        Row row = sheet.createRow(index + 1);
        for (Map.Entry<Integer, Object> data : dataSet) {
            Cell cell = row.createCell(data.getKey());
            setSellValue(cell, data.getValue());
        }
    }

    private static void setSellValue(Cell cell , Object value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (value != null && value.getClass() == Date.class) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(format.format((Date) value));
          
        } else if (value != null && (value.getClass() == Integer.class || value.getClass() == int.class)) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            Integer v = (Integer) value;
            cell.setCellValue(v.doubleValue());
            
        } else if (value != null && (value.getClass() == Boolean.class || value.getClass() == boolean.class)) {
            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
            cell.setCellValue((Boolean) value);
        } else if (value != null) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(value.toString());  
        }
    }
    
    private static Set<Map.Entry<Integer, String>> getExcelHeaders(Object firstRow) {
        Map<Integer, String> headers = new HashMap<Integer, String>();
        List<Field> fieldList = Arrays.asList(firstRow.getClass().getDeclaredFields());

        for (Field field : fieldList) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (excelField == null) {
                continue;
            }
            field.setAccessible(true);
            int order = excelField.order();
            String title = excelField.title();
            headers.put(order, title);
        }
        //log.debug("excel header info ={}", headers.toString());
        return headers.entrySet();
    }
    
    private static Set<Map.Entry<Integer, Object>> getRowData(int index, List<?> contents) {
        Object row = contents.get(index);
        Map<Integer, Object> datas = new HashMap<>();
        List<Field> fieldList = Arrays.asList(row.getClass().getDeclaredFields());

        for (Field field : fieldList) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (excelField == null) {
                continue;
            }
            field.setAccessible(true);
            int order = excelField.order();
           // try { 
                Object value = ReflectionUtils.getField(field, row);
                datas.put(order, value);
           //  }catch(IllegalAccessException | InstantiationException iie) {
             //    datas.put(order, "");
           //  }
        }
        //log.debug("excel index = {}", (index + 1));
        return datas.entrySet();
    }
    
    
    private static void write(Workbook wb, OutputStream output) throws IOException {
        wb.write(output);
        output.flush();
        output.close();
        wb.close();
    }
}
