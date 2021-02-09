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
            insert(name, map, sb);
            update(name, map, sb);
            delete(name, map, sb);
            sb.append("</mapper>");
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void select(String name, Map<String, Object> map, StringBuilder sb) {
        String tableName = map.get("tableName").toString();
        String camel = CamelMapping.parseCamel(tableName);
        String clazzPath = map.get("class").toString();
        sb.append("    <select id=\"get").append(name).append("\" parameterType=\"").append(clazzPath).append("\" resultType=\"").append(clazzPath).append("\">\n");
        sb.append("        SELECT ");
        field(map, sb);
        sb.append(" FROM `").append(camel).append("`\n");
        where(map, sb);
        sb.append("    </select>\n\n");
        sb.append("    <select id=\"get").append(name).append("List\" parameterType=\"").append(clazzPath).append("\" resultType=\"").append(clazzPath).append("\">\n");
        sb.append("        SELECT ");
        field(map, sb);
        sb.append(" FROM `").append(camel).append("`\n");
        where(map, sb);
        sb.append("    </select>\n\n");
    }

    public static void update(String name, Map<String, Object> map, StringBuilder sb) {
        String tableName = map.get("tableName").toString();
        String camel = CamelMapping.parseCamel(tableName);
        String clazzPath = map.get("class").toString();
        sb.append("    <update id=\"update").append(name).append("\" parameterType=\"").append(clazzPath).append("\">\n");
        sb.append("        UPDATE `").append(camel).append("`\n");
        sb.append("        <set>\n");
        ifElseForUpdate(map, sb);
        sb.append("        </set>\n");
        sb.append("        WHERE\n");
        ifElseForUpdateValue(map, sb);
        sb.append("    </update>\n\n");
    }

    public static void insert(String name, Map<String, Object> map, StringBuilder sb) {
        String tableName = map.get("tableName").toString();
        String camel = CamelMapping.parseCamel(tableName);
        String clazzPath = map.get("class").toString();
        sb.append("    <insert id=\"insert").append(name).append("\" parameterType=\"").append(clazzPath).append("\">\n");
        sb.append("        INSERT INTO `").append(camel).append("`(\n");
        ifElseForInsert(map, sb);
        sb.append("        )\n        VALUES (\n");
        ifElseForInsertValue(map, sb);
        sb.append("        )\n");
        sb.append("    </insert>\n\n");
    }

    public static void delete(String name, Map<String, Object> map, StringBuilder sb) {
        String tableName = map.get("tableName").toString();
        String camel = CamelMapping.parseCamel(tableName);
        String clazzPath = map.get("class").toString();
        sb.append("    <delete id=\"delete").append(name).append("\" parameterType=\"").append(clazzPath).append("\">\n");
        sb.append("        DELETE FROM `").append(camel).append("`\n");
        sb.append("        WHERE\n");
        ifElse(map, sb);
        sb.append("    </delete>\n\n");
        sb.append("    <delete id=\"delete").append(name).append("ById\" parameterType=\"").append(clazzPath).append("\">\n");
        sb.append("        DELETE FROM `").append(camel).append("`\n");
        sb.append("        WHERE\n");
        ifElseForDeleteValue(map, sb);
        sb.append("    </delete>\n\n");
        sb.append("    <delete id=\"delete").append(name).append("List\" parameterType=\"").append(clazzPath).append("\">\n");
        sb.append("        DELETE FROM `").append(camel).append("`\n");
        sb.append("        WHERE ");
        ifElseForDeleteList(map, sb);
        sb.append("    </delete>\n\n");
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

    public static void where(Map<String, Object> map, StringBuilder sb) {
        sb.append("        <where>\n");
        ifElse(map, sb);
        sb.append("        </where>\n");
    }

    public static void ifElse(Map<String, Object> map, StringBuilder sb) {
        Field[] fields = (Field[]) map.get("fields");
        String name, camel;
        Field field;
        for (int i = 0, length = fields.length; i < length; i++) {
            field = fields[i];
            name = field.getName();
            camel = CamelMapping.parseCamel(name);
            sb.append("            <if test=\"").append(name).append("!=null and ").append(name).append("!=''\">\n");
            sb.append("                ");
            if (i > 0) {
                sb.append(" AND ");
            }
            sb.append(camel).append(" = #{").append(name).append("}\n            </if>\n");
        }
    }

    public static void ifElseForInsert(Map<String, Object> map, StringBuilder sb) {
        Field[] fields = (Field[]) map.get("fields");
        String name, camel;
        Field field;
        for (int i = 0, length = fields.length; i < length; i++) {
            field = fields[i];
            name = field.getName();
            camel = CamelMapping.parseCamel(name);
            sb.append("        <if test=\"").append(name).append("!=null and ").append(name).append("!=''\">\n");
            sb.append("            ");
            if (i > 0) {
                sb.append(",");
            }
            sb.append(camel);
            sb.append("\n        </if>\n");
        }
    }

    public static void ifElseForInsertValue(Map<String, Object> map, StringBuilder sb) {
        Field[] fields = (Field[]) map.get("fields");
        String name;
        Field field;
        for (int i = 0, length = fields.length; i < length; i++) {
            field = fields[i];
            name = field.getName();
            sb.append("        <if test=\"").append(name).append("!=null and ").append(name).append("!=''\">\n");
            sb.append("            ");
            if (i > 0) {
                sb.append(",");
            }
            sb.append("#{").append(name).append("}");
            sb.append("\n        </if>\n");
        }
    }

    public static void ifElseForUpdate(Map<String, Object> map, StringBuilder sb) {
        Field[] fields = (Field[]) map.get("fields");
        String name, camel;
        for (Field field : fields) {
            name = field.getName();
            camel = CamelMapping.parseCamel(name);
            sb.append("            <if test=\"").append(name).append("!=null and ").append(name).append("!=''\">\n")
                    .append("                ").append(camel).append(" = #{").append(name).append("},\n            </if>\n");
        }
    }

    public static void ifElseForUpdateValue(Map<String, Object> map, StringBuilder sb) {
        Field[] fields = (Field[]) map.get("fields");
        String name, camel;
        for (Field field : fields) {
            name = field.getName();
            camel = CamelMapping.parseCamel(name);
            sb.append("            <if test=\"").append(name).append("!=null and ").append(name).append("!=''\">\n")
                    .append("                ").append(camel).append(" = #{").append(name).append("}\n            </if>\n");
            break;
        }
    }

    public static void ifElseForDeleteValue(Map<String, Object> map, StringBuilder sb) {
        Field[] fields = (Field[]) map.get("fields");
        String name, camel;
        for (Field field : fields) {
            name = field.getName();
            camel = CamelMapping.parseCamel(name);
            sb.append("            <if test=\"").append(name).append("!=null and ").append(name).append("!=''\">\n");
            sb.append("                ");
            sb.append(camel).append(" = #{").append(name).append("}\n            </if>\n");
            break;
        }
    }

    public static void ifElseForDeleteList(Map<String, Object> map, StringBuilder sb) {
        Field[] fields = (Field[]) map.get("fields");
        String name, camel;
        for (Field field : fields) {
            name = field.getName();
            camel = CamelMapping.parseCamel(name);
            sb.append(camel).append(" in\n");
            break;
        }
        sb.append("        <foreach item=\"item\" collection=\"list\" open=\"(\" separator=\",\" close=\")\">\n");
        for (Field field : fields) {
            name = field.getName();
            sb.append("            #{item.").append(name).append("}\n");
            break;
        }
        sb.append("        </foreach>\n");
    }

}
