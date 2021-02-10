package com.generate.build;

import com.generate.mapping.CamelMapping;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ServiceImpl {

    public static void write(String pack, String path, Map<String, Object> map) {
        String name = map.get("name").toString();
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        path += name + "ServiceImpl.java";
        File file = new File(path);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            StringBuilder sb = new StringBuilder();
            sb.append("package ").append(pack).append(".service.impl;\n\n");
            sb.append("import ").append(map.get("class")).append(";\n\n");
            sb.append("@Service\n");
            sb.append("public class ").append(name).append("ServiceImpl  implements ").append(name).append("Service{\n\n");
            sb.append("    @Autowired\n");
            sb.append("    private ").append(name).append("Dao ").append(camel).append("Dao;\n\n");
            page(name, camel, sb);
            sb.append("}");
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void page(String name, String camel, StringBuilder sb) {
        sb.append("    @Override\n");
        sb.append("    public PageInfo<").append(name).append("> get").append(name).append("ByPage(int pageNum,int pageSize,").append(name).append(" bean){\n");
        sb.append("        PageHelper.startPage(pageNum, pageSize);\n");
        sb.append("        List<").append(name).append("> list = ").append(camel).append("Dao.get").append(name).append("List(bean);\n");
        sb.append("        return new PageInfo<>(list);\n");
        sb.append("    }\n");
    }
}
