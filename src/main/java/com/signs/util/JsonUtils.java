package com.signs.util;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {
    /**
     * 将结果封装到response里返回json类型
     *
     * @param request  请求参数
     * @param response 回调参数
     * @param result   返回结果字符串
     */
    public static void returnJSON(HttpServletRequest request, HttpServletResponse response, String result) {
        response.setContentType("text/html;charset=UTF-8");
        result = StringUtils.isNotEmpty(result) ? result : "{\"data\":\"填充对象为空\"}";
        String jsonCallback = request.getParameter("callback");
        if (StringUtils.isNotEmpty(jsonCallback)) {
            result = jsonCallback + "(" + result + ")";
        }
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * object类型转换成json字符串
     *
     * @param object 任意object类型
     * @return 转换后的json字符串
     */
    public static String objectToJSON(Object object) {
        return objectToJSON(object, null);
    }

    public static String objectToJSON(Object object, String format) {
        String result;
        if (object != null) {
            if (object instanceof Object[] || object instanceof List) {
                result = JSONArray.fromObject(object, cfg(format)).toString();
            } else if (object instanceof String) {
                result = "{\"data\":\"" + object + "\"}";
            } else {
                result = JSONObject.fromObject(object, cfg(format)).toString();
            }
        } else {
            result = "{\"data\":\"填充对象为空\"}";
        }
        return result;
    }

    /**
     * 将字符串根据分隔符分隔成list
     *
     * @param splitStr    待分隔字符串
     * @param splitSymbol 分隔符
     * @return 字符数组
     */
    public static List<String> splitStrToList(String splitStr, String splitSymbol) {
        return splitStrToList(splitStr, splitSymbol, "String");
    }

    /**
     * 将字符串根据分隔符分隔成list
     *
     * @param splitStr    待分隔字符串
     * @param splitSymbol 分隔符
     * @return 字符数组
     */
    public static <T extends Object> List<T> splitStrToList(String splitStr, String splitSymbol, String type) {
        if (StringUtils.isEmpty(splitSymbol)) return null;
        if ("|".equals(splitSymbol)) splitSymbol = "\\|";
        List<T> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(splitStr)) {
            String[] x = splitStr.split(splitSymbol, -1);
            for (String s : x) {
                if (StringUtils.isNotEmpty(s)) {
                    T value;
                    if ("BigDecimal".equals(type)) {
                        value = (T) new BigDecimal(s);
                    } else if ("Integer".equals(type) || "Float".equals(type) || "Double".equals(type)) {
                        value = null;
                        try {
                            value = (T) NumberFormat.getInstance().parse(s);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if ("Date".equals(type)) {
                        value = (T) DateUtils.strToDate(s, "Date");
                    } else {
                        value = (T) s;
                    }
                    result.add(value);
                } else {
                    result.add(null);
                }
            }
        }
        return result;
    }

    /**
     * 字符串列表转换成逗号分隔的字符串
     *
     * @param strList 字符串列表
     * @return 逗号分隔的字符串
     */
    public static String strListToStr(List<String> strList) {
        return strListToStr(strList, ",");
    }

    /**
     * 字符串列表转换成对应分隔符分隔的字符串
     *
     * @param strList     字符串列表
     * @param splitSymbol 分隔符
     * @return 对应分隔符分隔的字符串
     */
    public static String strListToStr(List<String> strList, String splitSymbol) {
        String result = "";
        for (String temp : strList) {
            result += temp + splitSymbol;
        }
        if (!result.isEmpty()) result = result.substring(0, result.length() - 1);
        return result;
    }

    /**
     * 将json字符串转成对应实体list
     *
     * @param jsonStr   待转换的json字符串
     * @param beanClass 要转的对应实体类class
     * @param <T>       要转的对应实体类
     * @return 实体数组
     */
    public static <T> List<T> jsonStrToList(String jsonStr, Class<T> beanClass) {
        List<T> result = new ArrayList<>();
        try {
            if (StringUtils.isNotEmpty(jsonStr)) {
                //把json字符串转换成对象
                JSONArray json = JSONArray.fromObject(jsonStr);
                result = cast(JSONArray.toCollection(json, beanClass));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将json字符串转成List<Map>
     *
     * @param jsonStr 待转换的json字符串
     * @return Map数组
     */
    public static List<Map<String, Object>> jsonStrToMapList(String jsonStr) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            if (StringUtils.isNotEmpty(jsonStr)) {
                //把json字符串转换成对象
                JSONArray json = JSONArray.fromObject(jsonStr);
                result = cast(json);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> String listToSplitStr(List<T> list) {
        return listToSplitStr(list, null, null);
    }

    public static <T> String listToSplitStr(List<T> list, String column) {
        return listToSplitStr(list, column, null);
    }

    /**
     * 实体列表转成对应字段字符串
     *
     * @param list        实体列表
     * @param column      要转换的字段，null则是全部字段
     * @param splitSymbol 分隔符，默认是|
     * @param <T>         实体类型
     * @return 分隔拼接后的字段字符串
     */
    public static <T> String listToSplitStr(List<T> list, String column, String splitSymbol) {
        if (splitSymbol == null) splitSymbol = "|";
        String result = "";
        if (list == null) return result;
        for (T model : list) {
            Field[] fields = model.getClass().getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);//可以访问私有变量
                if (column != null && !column.equals(f.getName())) continue;
                try {
                    Object val = f.get(model);// 得到此属性的值
                    if (f.getType().toString().equals("class java.util.Date")) {
                        val = DateUtils.dateToStr((Date) val);
                        if (val.toString().indexOf("00:00:00") > 0) val = val.toString().split(" ")[0];//去掉时分秒
                    }
                    if (result.isEmpty()) result = val + "";
                    else result += splitSymbol + val;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 实体类型相互转换
     *
     * @param object    要转换的实体类
     * @param beanClass 转成的class
     * @param <T>       转成的实体类型
     * @return
     */
    public static <T> T beanToBean(Object object, Class<T> beanClass) {
        JSONObject obj = JSONObject.fromObject(object, cfg(null));
        return (T) JSONObject.toBean(obj, beanClass);
    }

    /**
     * json字符串转换成对应的类
     *
     * @param jsonStr   要转换的json字符串
     * @param beanClass 转成的class
     * @param <T>       转成的实体类型
     * @return
     */
    public static <T> T jsonStrToBean(String jsonStr, Class<T> beanClass) {
        JSONObject obj = JSONObject.fromObject(jsonStr, cfg(null));
        return (T) JSONObject.toBean(obj, beanClass);
    }

    /**
     * list类型相互转换
     *
     * @param list      要转换的list
     * @param beanClass 转成的class
     * @param <T>       转成的list类型
     * @return
     */
    public static <T> List<T> listToList(List list, Class<T> beanClass) {
        String str = JsonUtils.objectToJSON(list);
        return JsonUtils.jsonStrToList(str, beanClass);
    }

    /**
     * 重新设置时间格式
     *
     * @param format 转换的格式
     * @return config配置
     */
    private static JsonConfig cfg(String format) {
        JsonConfig cfg = new JsonConfig();
        if (StringUtils.isNotEmpty(format)) {
            cfg.registerJsonValueProcessor(Date.class, new JsonValueProcessorImpl(format));
            cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl(format));
        } else {
            cfg.registerJsonValueProcessor(Date.class, new JsonValueProcessorImpl("yyyy-MM-dd HH:mm:ss"));
            cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl("yyyy-MM-dd HH:mm:ss"));
        }
        return cfg;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object obj) {
        return (T) obj;
    }

    /**
     * 判断字符串是否是数字
     *
     * @param str 待判断字符串
     * @return 是或否
     */
    public static boolean isNumeric(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 驼峰转下划线
     *
     * @param obj 待转换json
     * @return 转换后json
     */
    public static JSONObject objToUnderline(JSONObject obj) {
        return objToCamelUnderline(obj, "Underline");
    }

    /**
     * 下划线转驼峰
     *
     * @param obj 待转换json
     * @return 转换后json
     */
    public static JSONObject objToCamel(JSONObject obj) {
        return objToCamelUnderline(obj, "Camel");
    }

    /**
     * 驼峰下划线互转
     *
     * @param obj  待转换json
     * @param type 要转换的类型
     * @return 转换后json
     */
    private static JSONObject objToCamelUnderline(JSONObject obj, String type) {
        if (obj == null) return obj;
        JSONObject result = new JSONObject();
        Iterator it = obj.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = obj.getString(key);
            if ("Underline".equals(type)) {
                result.put(camelToUnderline(key), value);
            } else {
                result.put(underlineToCamel(key), value);
            }
        }
        return result;
    }

    /**
     * 驼峰转下划线
     *
     * @param param 要转换的字符串
     * @return 转换后字符串
     */
    private static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param param 要转换的字符串
     * @return 转换后字符串
     */
    private static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = Pattern.compile("_").matcher(param);
        int i = 0;
        while (mc.find()) {
            int position = mc.end() - (i++);
            sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
        }
        return sb.toString();
    }

    /**
     * JSONObject转Map
     *
     * @param obj 要转换的JSONObject
     * @return 转换后的Map
     */
    public static Map<String, String> objToMap(JSONObject obj) {
        Map<String, String> map = new HashMap<>();
        if (obj != null) {
            Iterator it = obj.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = obj.getString(key);
                map.put(key, value);
            }
        }
        return map;
    }

    //时间格式内部类
    private static class JsonValueProcessorImpl implements JsonValueProcessor {

        private String format = "yyyy-MM-dd HH:mm:ss";

        JsonValueProcessorImpl(String format) {
            this.format = format;
        }

        public Object processArrayValue(Object value, JsonConfig jsonConfig) {
            String[] obj = {};
            if (value instanceof Date[]) {
                SimpleDateFormat sf = new SimpleDateFormat(format);
                Date[] dates = (Date[]) value;
                obj = new String[dates.length];
                for (int i = 0; i < dates.length; i++) {
                    obj[i] = sf.format(dates[i]);
                }
            }
            return obj;
        }

        public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
            if (value instanceof Date) {
                return new SimpleDateFormat(format).format((Date) value);
            }
            if (value != null) {
                return value.toString();
            } else {
                return null;
            }

        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }
    }
}
