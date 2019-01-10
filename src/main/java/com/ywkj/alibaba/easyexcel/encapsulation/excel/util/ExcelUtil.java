package com.ywkj.alibaba.easyexcel.encapsulation.excel.util;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Font;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.TableStyle;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.core.ExcelListener;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.core.ExcelWriterFactory;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.exception.ExcelException;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.pojo.ReadExcelResult;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.style.ExcelStyleModel;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * Created by Fq on 2019/01/07.
 * Excel操作类
 */
public class ExcelUtil {
    /**
     * 读取 Excel(所有 sheet)
     *
     * @param excel    文件
     * @return Excel 数据 list
     */
    public static <T> ReadExcelResult<T> readExcel(MultipartFile excel, Class<T> t) {
        ExcelListener<T> excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return null;
        }
        for (Sheet sheet : reader.getSheets()) {
            if (t != null) {
                sheet.setClazz((Class<? extends BaseRowModel>) t);
            }
            reader.read(sheet);
        }
        return new ReadExcelResult(excelListener.getSuccessList(),excelListener.getFailList());
    }
    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel       文件
     * @param t    实体类映射，继承 BaseRowModel 类
     * @param sheetNo     sheet 的序号 从1开始
     * @param headLineNum 表头行数，默认为1
     * @return Excel 数据 list
     */
    public static <T> ReadExcelResult<T> readExcel(MultipartFile excel, Class<T> t, int sheetNo, int headLineNum) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return null;
        }
        reader.read(new Sheet(sheetNo, headLineNum, (Class<? extends BaseRowModel>) t));
        return new ReadExcelResult(excelListener.getSuccessList(),excelListener.getFailList());
    }


    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel    文件
     * @param t 实体类映射，继承 BaseRowModel 类
     * @param sheetNo  sheet 的序号 从1开始
     * @return Excel 数据 list
     */
    public static <T> ReadExcelResult<T> readExcel(MultipartFile excel, Class<T> t, int sheetNo) {
        return readExcel(excel, t, sheetNo, 1);
    }













    /**
     * 导出 Excel ：一个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     */
    public static <T> void writeExcel(HttpServletResponse response, List<T> list,
                                      String fileName, String sheetName, Class<T> c) {

        ExcelWriter writer = new ExcelWriter(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, (Class<? extends BaseRowModel>) c);
        sheet.setSheetName(sheetName);
        writer.write((List<? extends BaseRowModel>) list, sheet);
        writer.finish();
    }


    public static <T> void writeExcel(HttpServletResponse response, List<T> list,
                                      String fileName, String sheetName, Class<T> c, ExcelStyleModel excelStyleModel) {

        ExcelWriter writer = new ExcelWriter(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, (Class<? extends BaseRowModel>) c);
        sheet.setSheetName(sheetName);
        setSheetHeadStyle(excelStyleModel,sheet);
        writer.write((List<? extends BaseRowModel>) list, sheet);
        writer.finish();
    }

    /**
     * 导出 Excel ：多个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     */
    public static <T> ExcelWriterFactory writeExcelWithSheets(HttpServletResponse response, List<T> list,
                                                              String fileName, String sheetName, Class<T> c) {
        ExcelWriterFactory writer = new ExcelWriterFactory(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, (Class<? extends BaseRowModel>) c);
        sheet.setSheetName(sheetName);
        writer.write((List<? extends BaseRowModel>) list, sheet);
        return writer;
    }


    /**
     * 导出文件时为Writer生成OutputStream
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) {
        //创建本地文件
        String filePath = fileName + ".xlsx";
        File dbfFile = new File(filePath);
        try {
            if (!dbfFile.exists() || dbfFile.isDirectory()) {
                dbfFile.createNewFile();
            }
            fileName = new String(filePath.getBytes(), "ISO-8859-1");
            response.addHeader("Content-Disposition", "filename=" + fileName);
            return response.getOutputStream();
        } catch (IOException e) {
            throw new ExcelException("创建文件失败！");
        }
    }

    /**
     * 返回 ExcelReader
     *
     * @param excel         需要解析的 Excel 文件
     * @param excelListener new ExcelListener()
     */
    private static ExcelReader getReader(MultipartFile excel,
                                         ExcelListener excelListener) {
        String filename = excel.getOriginalFilename();
        if (filename == null || (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx"))) {
            throw new ExcelException("文件格式错误！");
        }
        InputStream inputStream;
        try {
            inputStream = excel.getInputStream();
            return new ExcelReader(inputStream, null, excelListener, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @descrp:设置table的head的样式
     * @auther: sevenDay
     * @date: 2019/1/7 13:11
     * @param excelStyleModel :
     * @param sheet :
     * @return : void
     */
    public static void setSheetHeadStyle(ExcelStyleModel excelStyleModel, Sheet sheet){
        if (Objects.isNull(excelStyleModel)){
            return;
        }
        TableStyle tableStyle = new TableStyle();
        Font font = new Font();
        font.setFontName(excelStyleModel.getFontName());
        font.setFontHeightInPoints(excelStyleModel.getFontHeight());
        font.setBold(excelStyleModel.isBold());
        tableStyle.setTableContentBackGroundColor(IndexedColors.fromInt(excelStyleModel.getBackColor()));
        tableStyle.setTableContentFont(font);
        sheet.setTableStyle(tableStyle);
    }

}
