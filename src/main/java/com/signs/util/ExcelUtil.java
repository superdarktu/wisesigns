package com.signs.util;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.*;

/*
 * ExcelUtil工具类实现功能:
 * 导出时传入list<T>,即可实现导出为一个excel,其中每个对象Ｔ为Excel中的一条记录.
 * 导入时读取excel,得到的结果是一个list<T>.T是自己定义的对象.
 * 需要导出的实体对象只需简单配置注解就能实现灵活导出,通过注解您可以方便实现下面功能:
 * 1.实体属性配置了注解就能导出到excel中,每个属性都对应一列.
 * 2.列名称可以通过注解配置.
 * 3.导出到哪一列可以通过注解配置.
 * 4.鼠标移动到该列时提示信息可以通过注解配置.
 * 5.用注解设置只能下拉选择不能随意填写功能.
 * 6.用注解设置是否只导出标题而不导出内容,这在导出内容作为模板以供用户填写时比较实用.
 */
public class ExcelUtil<T> {
    private Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     */
    private static int getExcelCol(String col) {
        col = col.toUpperCase();
        //从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

    /**
     * 设置单元格上提示
     *
     * @param sheet         要设置的sheet.
     * @param promptTitle   标题
     * @param promptContent 内容
     * @param firstRow      开始行
     * @param endRow        结束行
     * @param firstCol      开始列
     * @param endCol        结束列
     * @return 设置好的sheet.
     */
    private static Sheet setHSSFPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow, int firstCol, int endCol) {
        //构造constraint对象
        DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("DD1");
        //四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        //数据有效性对象
        HSSFDataValidation data_validation_view = new HSSFDataValidation(regions, constraint);
        data_validation_view.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(data_validation_view);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet    要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow   结束行
     * @param firstCol 开始列
     * @param endCol   结束列
     * @return 设置好的sheet.
     */
    private static Sheet setHSSFValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol) {
        //加载下拉列表内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
        //设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        //数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    public List<T> importExcel(Boolean xlsx, String sheetName, InputStream input) {
        int maxCol = 0;
        List<T> list = new ArrayList<>();
        try {
            Workbook workbook;
            if (xlsx) workbook = new XSSFWorkbook(input);
            else workbook = new HSSFWorkbook(input);
            Sheet sheet;
            if (sheetName != null && !sheetName.trim().equals(""))
                sheet = workbook.getSheet(sheetName);//如果指定sheet名,则取指定sheet中的内容.
            else sheet = workbook.getSheetAt(0); //如果传入的sheet名不存在则默认指向第1个sheet.
            int rows = sheet.getPhysicalNumberOfRows();

            if (rows > 0) {//有数据时才处理
                //Field[] allFields = clazz.getDeclaredFields();//得到类的所有field.
                List<Field> allFields = getMappedFiled(clazz, null);

                Map<Integer, Field> fieldsMap = new HashMap<>();//定义一个map用于存放列的序号和field.
                for (int col = 0; col < allFields.size(); col++) {
                    Field field = allFields.get(col);
                    //将有注解的field存放到map中.
                    if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                        maxCol = Math.max(col, maxCol);
                        //System.out.println(col + "====" + field.getName());
                        field.setAccessible(true);//设置类的私有字段属性可访问.
                        fieldsMap.put(col, field);
                    }
                }
                for (int i = 1; i < rows; i++) {//从第2行开始取数据,默认第一行是表头.
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    //int cellNum = row.getPhysicalNumberOfCells();
                    //int cellNum = row.getLastCellNum();
                    T entity = null;
                    for (int j = 0; j <= maxCol; j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null) {
                            continue;
                        }
                        int cellType = cell.getCellType();
                        String c;
                        if (cellType == Cell.CELL_TYPE_NUMERIC) {
                            Double cellValue = cell.getNumericCellValue();
                            NumberFormat nf = NumberFormat.getInstance();
                            nf.setGroupingUsed(false);
                            c = nf.format(cellValue);
                        } else if (cellType == Cell.CELL_TYPE_BOOLEAN) {
                            c = String.valueOf(cell.getBooleanCellValue());
                        } else {
                            c = cell.getStringCellValue();
                        }
                        if (c == null || c.equals("")) continue;
                        entity = (entity == null ? clazz.newInstance() : entity);//如果不存在实例则新建.
                        //System.out.println(cells[j].getContents());
                        Field field = fieldsMap.get(j);//从map中得到对应列的field.
                        if (field == null) {
                            continue;
                        }
                        //取得类型,并根据对象类型设置值.
                        Class<?> fieldType = field.getType();
                        if (String.class == fieldType) {
                            field.set(entity, String.valueOf(c));
                        } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                            String temp = c;
                            if (c.contains(".")) {
                                temp = c.substring(0, c.indexOf("."));
                            }
                            field.set(entity, Integer.parseInt(temp));
                        } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                            field.set(entity, Long.valueOf(c));
                        } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                            field.set(entity, Float.valueOf(c));
                        } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                            field.set(entity, Short.valueOf(c));
                        } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                            field.set(entity, Double.valueOf(c));
                        } else if (Date.class == fieldType) {
                            field.set(entity, c);
                        } else if (Character.TYPE == fieldType) {
                            if (c.length() > 0) {
                                field.set(entity, c.charAt(0));
                            }
                        }
                    }
                    if (entity != null) {
                        list.add(entity);
                    }
                }
            }

        } catch (IOException | InstantiationException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 导入到excel模板
     */
    public Boolean exportExcelModel(Boolean xlsx, OutputStream output) {
        Workbook workbook;
        if (xlsx) workbook = new XSSFWorkbook();
        else workbook = new HSSFWorkbook();
        List<Field> fields = getMappedFiled(clazz, null);
        Sheet sheet = workbook.createSheet();//产生工作表对象
        workbook.setSheetName(0, "导出模板");
        Cell cell;//产生单元格
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        Row row = sheet.createRow(0);//产生一行
        //写入各个字段的列头名称
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
            cell = row.createCell(i);//创建列
            cell.setCellType(Cell.CELL_TYPE_STRING);//设置列中写入内容为String类型
            cell.setCellValue(attr.name());//写入列名
//           sheet.setColumnWidth(i, attr.name().length() * 400);
            sheet.setColumnWidth(i, 5000);
            //如果设置了提示信息则鼠标放上去提示.
            if (!attr.prompt().trim().equals("")) {
                setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, i, i);//这里默认设了2-101列提示.
            }
            //如果设置了combo属性则本列只能选择不能输入
            if (attr.combo().length > 0) {
                setHSSFValidation(sheet, attr.combo(), 1, 100, i, i);//这里默认设了2-101列只能选择不能输入.
            }
            cell.setCellStyle(style);
        }
        //填充空行
        for (int i = 1; i <= 1000; i++) {
            Row emptyRow = sheet.createRow(i);//产生一行
            for (int col = 0; col < fields.size(); col++) {
                Field field = fields.get(col);
                ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
                cell = emptyRow.createCell(col);//创建列
                cell.setCellStyle(style);
            }
        }
        try {
            output.flush();
            workbook.write(output);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed");
            return false;
        }
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     */
    private boolean exportExcel(Boolean xlsx, List<T> lists[], String sheetNames[], OutputStream output) {
        if (lists.length != sheetNames.length) {
            System.out.println("数组长度不一致");
            return false;
        }

        Workbook workbook;
        if (xlsx) workbook = new XSSFWorkbook();
        else workbook = new HSSFWorkbook();

        for (int ii = 0; ii < lists.length; ii++) {
            List<T> list = lists[ii];
            String sheetName = sheetNames[ii];

            List<Field> fields = getMappedFiled(clazz, null);

            Sheet sheet = workbook.createSheet();//产生工作表对象

            workbook.setSheetName(ii, sheetName);

            Row row;
            Cell cell;//产生单元格
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
            style.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
            row = sheet.createRow(0);//产生一行
            //写入各个字段的列头名称
            for (int col = 0; col < fields.size(); col++) {
                Field field = fields.get(col);
                ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
                cell = row.createCell(col);//创建列
                cell.setCellType(Cell.CELL_TYPE_STRING);//设置列中写入内容为String类型
                cell.setCellValue(attr.name());//写入列名

                //如果设置了提示信息则鼠标放上去提示.
                if (!attr.prompt().trim().equals("")) {
                    setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);//这里默认设了2-101列提示.
                }
                //如果设置了combo属性则本列只能选择不能输入
                if (attr.combo().length > 0) {
                    setHSSFValidation(sheet, attr.combo(), 1, 100, col, col);//这里默认设了2-101列只能选择不能输入.
                }
                cell.setCellStyle(style);
            }

            int startNo = 0;
            int endNo = list.size();
            //写入各条记录,每条记录对应excel表中的一行
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 1 - startNo);
                T vo = list.get(i); //得到导出对象.
                for (int col = 0; col < fields.size(); col++) {
                    Field field = fields.get(col);
                    field.setAccessible(true);//设置实体类私有属性可访问
                    ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
                    try {
                        //根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            cell = row.createCell(col);//创建cell
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(field.get(vo) == null ? "" : String.valueOf(field.get(vo)));//如果数据存在就填入,不存在填入空格.
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            output.flush();
            workbook.write(output);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed ");
            return false;
        }

    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param sheetName 工作表的名称
     * @param output    java输出流
     */
    public boolean exportExcel(Boolean xlsx, List<T> list, String sheetName, OutputStream output) {
        List<T>[] lists = new ArrayList[1];
        lists[0] = list;

        String[] sheetNames = new String[1];
        sheetNames[0] = sheetName;

        return exportExcel(xlsx, lists, sheetNames, output);
    }

    /**
     * 得到实体类所有通过注解映射了数据表的字段
     *
     * @param clazz  实体类
     * @param fields 所有字段
     * @return Map类型所有字段
     */
    private List<Field> getMappedFiled(Class clazz, List<Field> fields) {
        if (fields == null) {
            fields = new ArrayList<>();
        }

        Field[] allFields = clazz.getDeclaredFields();//得到所有定义字段
        //得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                fields.add(field);
            }
        }
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
            getMappedFiled(clazz.getSuperclass(), fields);
        }

        return fields;
    }
}