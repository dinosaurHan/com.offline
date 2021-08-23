package com.ofl.promotion.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/15 20:53
 */
public class ExcelUtils {

    private final static String excel2003L =".xls";    //2003- 版本的excel
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel


    /**
     * 导入Excel转为实体类
     * @param fileName
     * @param file
     * @param t
     * @return
     * @throws Exception
     */
    public  static <T> List handelExcel(String fileName, MultipartFile file, Class<T> t)throws Exception {

        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new Exception();
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);

        //获取表头列
        Row row = sheet.getRow(0);
        //表头字段放入titleList中
        List<String> titleList = new ArrayList<String>();
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (row.getCell(i).getStringCellValue().equals("一级")){
                titleList.add("oneLevel");
            }
            if (row.getCell(i).getStringCellValue().equals("二级（非必填）")){
                titleList.add("twoLevel");
            }
            if (row.getCell(i).getStringCellValue().equals("三级（非必填）")){
                titleList.add("threeLevel");
            }
            if (row.getCell(i).getStringCellValue().equals("四级（非必填）")){
                titleList.add("fourLevel");
            }
            if (row.getCell(i).getStringCellValue().equals("门店")){
                titleList.add("storeName");
            }
            if (row.getCell(i).getStringCellValue().equals("导购姓名")){
                titleList.add("guideName");
            }
            if (row.getCell(i).getStringCellValue().equals("电话")){
                titleList.add("guidePhone");
            }
        }
        //获得该类的所有属性
        Field[] fields = t.getDeclaredFields();

        List<T> list = new ArrayList<T>();

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            T tIns = t.newInstance();
            boolean nullrow = false;

            for (Field field : fields) {

                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), t);
                if (!"id".equals(field.getName())) {
                    int num = titleList.indexOf(field.getName());
                    if (num != -1) {
                        //获得set方法
                        Method method = pd.getWriteMethod();
                        Row nowrow = sheet.getRow(r);
                        if (nowrow != null) {
                            Cell cell = nowrow.getCell(num);
                            if (cell != null) {
                                cell.setCellType(CellType.STRING);
                                method.invoke(tIns, cell.getStringCellValue());
                            } else {
                                method.invoke(tIns, "");
                            }
                        } else {
                            nullrow = true;
                            break;
                        }

                    }
                }
            }
            if (nullrow == true) {
                break;
            }
            list.add(tIns);
        }

        return list;
    }

    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     * @param in,fileName
     * @return
     * @throws IOException
     */
    public static List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception {
        List<List<Object>> list = null;

        //创建Excel工作薄
        Workbook work = getWorkbook(in,fileName);
        if(null == work){
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        list = new ArrayList<List<Object>>();
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if(sheet==null){continue;}

            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j <=sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if(row==null||row.getFirstCellNum()==j){continue;}

                //遍历所有的列
                List<Object> li = new ArrayList<Object>();
                for (int k = row.getFirstCellNum(); k <row.getLastCellNum(); k++) {
                    cell = row.getCell(k);
                    if(cell==null){continue;}

                    li.add(getCellValue(cell));
                }
                list.add(li);
            }
        }
        return list;

    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }


    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell){
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                    value = sdf.format(cell.getDateCellValue());
                }else{
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

}
