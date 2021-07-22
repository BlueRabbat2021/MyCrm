package com.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*
在进行文件导出的时候，需要先将数据库里面的数据先取出来存放在服务器内存中，需要使用java建立Excel表来存储这些数据（这个
表示建立在服务器内存中的），之后才能进行导出操作。
    在java中万物皆对象
    一个Excel文件是一个对象
    一个sheet表示一个对象
    一个单元行是一个对象
    一行中的每一个列是一个对象
    样式也是一个对象
*/
public class CreateExcelTest {
    public static void main(String[] args) {
        //1、创建Excel对象，当使用无参构造方法时，创建的是空的Excel文件,Workbook就是工作铺
        HSSFWorkbook wb = new HSSFWorkbook();
        //2、创建工作表，参数就是工作表名
        HSSFSheet sheet = wb.createSheet("学生列表");
        //3、创建行 0表示第一行
        HSSFRow row= sheet.createRow(0);
        //4、创建列，也就是单元格，0表示第一列
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学号");
        // 第二列
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        // 第三列
        cell = row.createCell(2);
        cell.setCellValue("年龄");

        //5、样式对象
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        // 这里i取1，因为第一行已经设立了学号和姓名栏，从第二行开始存入数据
        for(int i = 1; i <= 5; i++){
            // 第二行开始
            row = sheet.createRow(i);
            // 第一列
            cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue(i);
            // 第二列
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue("tom" + i);
            // 第三列
            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(20 + i);
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream("d:\\student.xls");
            wb.write(os);
            System.out.println("------------------------------------------");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
