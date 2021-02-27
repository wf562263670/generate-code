package com.generate.build;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Service {

    public static void write(String name, String path) {
        StringBuilder sb = new StringBuilder();
        File file = new File(path + ("/" + name + "Service.java"));
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            basic(name, sb);
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void basic(String name, StringBuilder sb) {
        sb.append("public interface ").append(name).append("Service {\n\n");
        page(name, sb);
        insert(name, sb);
        update(name, sb);
        delete(name, sb);
        sb.append("}");
    }

    public static void page(String name, StringBuilder sb) {
        sb.append("    PageInfo<").append(name).append("> getPage(int pageNum, int pageSize, ").append(name).append(" bean);\n\n");
    }

    public static void insert(String name, StringBuilder sb) {
        sb.append("    boolean add").append(name).append("(").append(name).append(" bean);\n\n");
    }

    public static void update(String name, StringBuilder sb) {
        sb.append("    boolean update").append(name).append("(").append(name).append(" bean);\n\n");
    }

    public static void delete(String name, StringBuilder sb) {
        sb.append("    boolean delete").append(name).append("(").append(name).append(" bean);\n\n");
        sb.append("    boolean deleteList").append("(List<").append(name).append("> list);\n\n");
    }

    public static void main(String[] args) {
        String path = "F:\\Git\\generate-code\\src\\main\\java\\com\\generate\\test";
        String name = "User";
        write(name, path);
    }
}
