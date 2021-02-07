package com.generate.build;

import com.generate.mapping.CamelMapping;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Mapper {

    public static void write(String path, Map<String, Object> map) {
        String name = map.get("name").toString();
        path += name + "Mapper.xml";
        File file = new File(path);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");
            sb.append("<mapper namespace=\"\">\n\n");
            select(name, map, sb);
            sb.append("</mapper>");
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void select(String name, Map<String, Object> map, StringBuilder sb) {
        String tableName = map.get("tableName").toString();
        String clazzPath = map.get("class").toString();
        sb.append("    <select id=\"get").append(name).append("\" parameterType=\"").append(clazzPath).append("\" resultType=\"").append(clazzPath).append("\">\n");
        sb.append("        SELECT ");
        field(map, sb);
        sb.append(" FROM `").append(CamelMapping.parseCamel(tableName)).append("`\n");
        ifElse(map, sb);
        sb.append("    </select>\n\n");
        sb.append("    <select id=\"get").append(name).append("List\" parameterType=\"").append(clazzPath).append("\" resultType=\"").append(clazzPath).append("\">\n");
        sb.append("        SELECT ");
        field(map, sb);
        sb.append(" FROM `").append(tableName).append("`\n");
        ifElse(map, sb);
        sb.append("    </select>\n\n");
    }

    public static void field(Map<String, Object> map, StringBuilder sb) {
        Field[] fields = (Field[]) map.get("fields");
        Field field;
        String name;
        for (int i = 0, length = fields.length; i < length; i++) {
            field = fields[i];
            name = field.getName();
            sb.append(CamelMapping.parseCamel(name));
            if (i < length - 1) sb.append(",");
        }
    }

    public static void ifElse(Map<String, Object> map, StringBuilder sb) {
        Field[] fields = (Field[]) map.get("fields");
        String name, camel;
        sb.append("        <where>\n");
        for (Field field : fields) {
            name = field.getName();
            camel = CamelMapping.parseCamel(name);
            sb.append("            <if test=\"").append(name).append("!=null and ").append(name).append("!=''\">\n").append("                ").append(name).append(" = #{").append(camel).append("}\n            </if>\n");
        }
        sb.append("        </where>\n");
    }
}
