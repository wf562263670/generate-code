package com.generate.build;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Dao {

    public static void write(String name, String path) {
        StringBuilder sb = new StringBuilder();
        File file = new File(path + ("/" + name + "Dao.java"));
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            basic(name, sb);
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void basic(String name, StringBuilder sb) {
        sb.append("public interface ").append(name).append("Dao {\n\n");
        list(name, sb);
        get(name, sb);
        insert(name, sb);
        update(name, sb);
        delete(name, sb);
        sb.append("}");
    }

    public static void list(String name, StringBuilder sb) {
        sb.append("    List<").append(name).append("> getList();\n\n");
    }

    public static void get(String name, StringBuilder sb) {
        sb.append("    ").append(name).append(" get").append(name).append("(").append(name).append(" bean);\n\n");
    }

    public static void insert(String name, StringBuilder sb) {
        sb.append("    int add").append(name).append("(").append(name).append(" bean);\n\n");
    }

    public static void update(String name, StringBuilder sb) {
        sb.append("    int update").append(name).append("(").append(name).append(" bean);\n\n");
    }

    public static void delete(String name, StringBuilder sb) {
        sb.append("    int delete").append(name).append("(").append(name).append(" bean);\n\n");
        sb.append("    int deleteList").append("(List<").append(name).append("> list);\n\n");
    }

}
