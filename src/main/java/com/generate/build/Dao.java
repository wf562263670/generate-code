package com.generate.build;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Dao {

    public static void write(String path, Map<String, Object> map) {
        String name = map.get("name").toString();
        path += name + "Dao.java";
        File file = new File(path);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            StringBuilder sb = new StringBuilder();
            sb.append("import ").append(map.get("class")).append(";\n\n");
            sb.append("public interface ").append(name).append("Dao {\n\n");
            selectOne(name, sb);
            selectList(name, sb);
            insert(name, sb);
            delete(name, sb);
            sb.append("}");
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void selectOne(String name, StringBuilder sb) {
        sb.append("    ").append(name).append(" get").append(name).append("(").append(name).append(" bean);\n\n");
    }

    public static void selectList(String name, StringBuilder sb) {
        sb.append("    List<").append(name).append("> get").append(name).append("List(").append(name).append(" bean);\n\n");
    }

    public static void insert(String name, StringBuilder sb) {
        sb.append("    int").append(" insert").append(name).append("(").append(name).append(" bean);\n\n");
    }

    public static void delete(String name, StringBuilder sb) {
        sb.append("    int").append(" delete").append(name).append("(").append(name).append(" bean);\n\n");
    }

}
