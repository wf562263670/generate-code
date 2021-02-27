package com.generate.build;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class ServiceImpl {

    public static void write(String name, String path) {
        StringBuilder sb = new StringBuilder();
        String lowerCase = name.toLowerCase(Locale.ROOT);
        File file = new File(path + ("/" + name + "Service.java"));
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            basic(name, lowerCase, sb);
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void basic(String name, String lowerCase, StringBuilder sb) {
        sb.append("@Service\n");
        sb.append("public class ").append(name).append("ServiceImpl implements ").append(name).append("Service {\n\n");
        sb.append("    @Autowired\n");
        sb.append("    private ").append(name).append("Dao ").append(lowerCase).append("Dao;\n\n");
        page(name, lowerCase, sb);
        insert(name, lowerCase, sb);
        update(name, lowerCase, sb);
        delete(name, lowerCase, sb);
        sb.append("}");
    }

    public static void page(String name, String lowerCase, StringBuilder sb) {
        sb.append("    @Override\n");
        sb.append("    public PageInfo<").append(name).append("> getPage(int pageNum, int pageSize, ").append(name).append(" bean) {\n");
        sb.append("        PageHelper.startPage(pageNum, pageSize);\n");
        sb.append("        List<").append(name).append("> list = ").append(lowerCase).append("Dao.getList(bean);\n");
        sb.append("        return new PageInfo<>(list);\n");
        sb.append("    }\n\n");
    }

    public static void insert(String name, String lowerCase, StringBuilder sb) {
        sb.append("    @Override\n");
        sb.append("    @Transactional(rollbackFor = Exception.class)\n");
        sb.append("    public boolean insert").append(name).append("(").append(name).append(" bean) {\n");
        sb.append("        return ").append(lowerCase).append("Dao.insert").append(lowerCase).append("(bean) > 0;\n");
        sb.append("    }\n\n");
    }

    public static void update(String name, String lowerCase, StringBuilder sb) {
        sb.append("    @Override\n");
        sb.append("    @Transactional(rollbackFor = Exception.class)\n");
        sb.append("    public boolean update").append(name).append("(").append(name).append(" bean) {\n");
        sb.append("        return ").append(lowerCase).append("Dao.update").append(lowerCase).append("(bean) > 0;\n");
        sb.append("    }\n\n");
    }

    public static void delete(String name, String lowerCase, StringBuilder sb) {
        sb.append("    @Override\n");
        sb.append("    @Transactional(rollbackFor = Exception.class)\n");
        sb.append("    public boolean delete").append(name).append("(").append(name).append(" bean) {\n");
        sb.append("        return ").append(lowerCase).append("Dao.delete").append(lowerCase).append("(bean) > 0;\n");
        sb.append("    }\n\n");
        sb.append("    @Override\n");
        sb.append("    @Transactional(rollbackFor = Exception.class)\n");
        sb.append("    public boolean deleteList").append("(List<").append(name).append("> list) {\n");
        sb.append("        int count = ").append(lowerCase).append("Dao.deleteList(list);\n");
        sb.append("        return list.size() == count;\n");
        sb.append("    }\n\n");
    }

    public static void main(String[] args) {
        String path = "F:\\Git\\generate-code\\src\\main\\java\\com\\generate\\test";
        String name = "User";
        write(name, path);
    }
}
