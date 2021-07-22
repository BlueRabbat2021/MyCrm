package com.crm.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ParseExcelTest {
    public static void main(String[] args) throws IOException {
        InputStream is = new FileInputStream("D:\\市场活动.xls");
        // 当创建工作铺时加入输出流可以创建带有数据的工作表
        HSSFWorkbook wb = new HSSFWorkbook(is);
        // 读第一张表，也可以传入表名读取
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell = null;
//        System.out.println(sheet.getLastRowNum());
        // 获取Excel表中的数据
        for(int i = 0; i <= sheet.getLastRowNum();i++){ // lastRowNum是最后一行的行号
            // 读取每一行
            row = sheet.getRow(i);
//            System.out.println(row.getLastCellNum());
            for(int j = 0; j < row.getLastCellNum(); j++){ // lastCellNum是最后一列的列号
                // 读取每一列
                cell = row.getCell(j);
                // 调用方法，将单元格中其他类型的数据转换为String类型
                System.out.print(getCellValue(cell) + "  ");
            }
            System.out.println();
        }

    }
    // 判断单元格类型用对应的方法来获取值，并转成String类型
    public static String getCellValue(HSSFCell cell){
        String ret = "";
        switch (cell.getCellType()){
            case HSSFCell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret = cell.getBooleanCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret = cell.getNumericCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                ret = cell.getCellFormula() + "";
                break;
            default:
                ret = "";
        }
        return ret;
    }
}
