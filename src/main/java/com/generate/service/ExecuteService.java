package com.generate.service;

import com.generate.annotation.Table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ExecuteService {

    public static Map<String, Object> getClassInfo(Class<?> clazz) {
        Map<String, Object> data = new HashMap<>();
        String name = null;
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            name = table.name();
            if ("".equals(name)) {
                name = clazz.getSimpleName();
            }
        }
        data.put("name", clazz.getSimpleName());
        data.put("class", clazz.getName());
        data.put("tableName", name);
        Field[] fields = clazz.getDeclaredFields();
        data.put("fields", fields);
        return data;
    }

}
