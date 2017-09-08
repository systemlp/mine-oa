package com.mine.oa.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class BeanUtil {

    /**
     *
     * 功能描述:校验object中指定属性是否为空 <br>
     * 〈功能详细描述〉
     *
     * @param object 待校验bean
     * @param flag true:校验attrNames中的属性；false:校验除attrNames外的属性。attrNames为空时该属性失效
     * @param attrNames 属性名
     * @return 第一个为空的属性名,否则返回null
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String checkEmpty(Object object, boolean flag, String... attrNames) {
        if (object == null) {
            throw new NullPointerException("待校验Bean为空");
        }
        try {
            Class<?> clazz = object.getClass();
            // 得到类中的所有属性集合
            Field[] fs = clazz.getDeclaredFields();
            if (ArrayUtils.isNotEmpty(attrNames)) {
                for (String attr : attrNames) {
                    if (StringUtils.isNotBlank(attr)) {
                        for (Field field : fs) {
                            field.setAccessible(true); // 设置些属性是可以访问的
                            if (flag && StringUtils.equalsIgnoreCase(attr, field.getName())) {
                                Object val = field.get(object);// 得到此属性的值
                                if (val == null)
                                    return field.getName();
                                if (val instanceof String) {
                                    if (StringUtils.isBlank(val.toString()))
                                        return attr;
                                }
                            } else if (!flag && !StringUtils.equalsIgnoreCase(attr, field.getName())) {
                                Object val = field.get(object);// 得到此属性的值
                                if (val == null || (val instanceof String && StringUtils.isBlank(val.toString())))
                                    return field.getName();
                            }
                        }
                    }
                }
            } else {
                for (Field field : fs) {
                    field.setAccessible(true); // 设置些属性是可以访问的
                    Object val;// 得到此属性的值
                    val = field.get(object);
                    if (val == null)
                        return field.getName();
                    if (val instanceof String) {
                        if (StringUtils.isBlank(val.toString()))
                            return field.getName();
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
