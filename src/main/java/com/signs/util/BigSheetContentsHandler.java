package com.signs.util;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;
import tk.mybatis.mapper.util.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BigSheetContentsHandler<T> implements XSSFSheetXMLHandler.SheetContentsHandler {

    protected boolean flag;
    private Class<T> clazz;
    private int cellNo = 0;
    protected  T model;
    private List<Field> allFields;
    private Integer rowNums = 0;

    public BigSheetContentsHandler(Class<T> clazz){

        this.clazz = clazz;
        allFields = Arrays.asList(clazz.getDeclaredFields()) ;
    }

    @Override
    public void startRow(int i) {
        rowNums = i;
        cellNo = 0;
        flag = false;
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endRow(int i) {
    }

    @Override
    public void cell(String s, String s1, XSSFComment xssfComment) {

        Field field = allFields.get(cellNo);
        field.setAccessible(true);
        try {
            if(field.isAnnotationPresent(ExcelNull.class) && StringUtil.isEmpty(s1)){
                field.set(model, null);
            }else {
                Class<?> fieldType = field.getType();
                if (String.class == fieldType) {
                    field.set(model, String.valueOf(s1));
                } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                    field.set(model, Integer.parseInt(s1));
                } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                    field.set(model, Long.valueOf(s1));
                } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                    field.set(model, Float.valueOf(s1));
                } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                    field.set(model, Short.valueOf(s1));
                } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                    field.set(model, Double.valueOf(s1));
                } else if (Date.class == fieldType) {
                    field.set(model, s1);
                }
            }
        } catch (Exception e) {
            flag = true;
        }
        cellNo++;
    }

    @Override
    public void headerFooter(String s, boolean b, String s1) {

    }

}
