package com.generate.build;

import com.generate.mapping.CamelMapping;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Controller {

    public static void write(String pack, String path, Map<String, Object> map) {
        String name = map.get("name").toString();
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        path += name + "Controller.java";
        File file = new File(path);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            StringBuilder sb = new StringBuilder();
            sb.append("package ").append(pack).append(".controller;\n\n");
            sb.append("import com.alibaba.fastjson.JSONObject;\n");
            sb.append("import ").append(map.get("class")).append(";\n\n");
            sb.append("@RestController\n");
            sb.append("@RequestMapping(\"/").append(camel).append("\")\n");
            sb.append("public class ").append(name).append("Controller {\n\n");
            sb.append("    @Autowired\n");
            sb.append("    private ").append(name).append("Dao ").append(camel).append("Dao;\n\n");
            sb.append("    @Autowired\n");
            sb.append("    private ").append(name).append("Service ").append(camel).append("Service;\n\n");
            page(name, sb);
            select(name, sb);
            insert(name, sb);
            update(name, sb);
            delete(name, sb);
            sb.append("}");
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void page(String name, StringBuilder sb) {
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        sb.append("    @GetMapping(\"/get").append(name).append("ByPage\")\n");
        sb.append("    public PageInfo<").append(name).append("> get").append(name).append("ByPage(HttpServletRequest request){\n");
        sb.append("        int pageNum = Integer.parseInt(request.getParameter(\"pageNum\"));\n");
        sb.append("        int pageSize = Integer.parseInt(request.getParameter(\"pageSize\"));\n");
        sb.append("        ").append(name).append(" bean = WebUtil.parseObject(request,").append(name).append(".class);\n");
        sb.append("        return ").append(camel).append("Service.get").append(name).append("ByPage(pageNum,pageSize,bean);\n");
        sb.append("    }\n\n");
        sb.append("    @PostMapping(\"/get").append(name).append("ByPage\")\n");
        sb.append("    public PageInfo<").append(name).append("> get").append(name).append("ByPage(@RequestBody JSONObject jsonObject){\n");
        sb.append("        Map<String, Object> map = JSONUtil.parseJSON(jsonObject);\n");
        sb.append("        int pageNum = Integer.parseInt(map.get(\"pageNum\").toString());\n");
        sb.append("        int pageSize = Integer.parseInt(map.get(\"pageSize\").toString());\n");
        sb.append("        ").append(name).append(" bean = JSONUtil.parseObject(jsonObject,").append(name).append(".class);\n");
        sb.append("        return ").append(camel).append("Service.get").append(name).append("ByPage(pageNum,pageSize,bean);\n");
        sb.append("    }\n\n");
    }

    public static void select(String name, StringBuilder sb) {
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        sb.append("    @GetMapping(\"/get").append(name).append("\")\n");
        sb.append("    public ").append(name).append(" get").append(name).append("(HttpServletRequest request){\n");
        sb.append("        ").append(name).append(" bean = WebUtil.parseObject(request,").append(name).append(".class);\n");
        sb.append("        return ").append(camel).append("Dao.get").append(name).append("(bean);\n");
        sb.append("    }\n\n");
        sb.append("    @PostMapping(\"/get").append(name).append("\")\n");
        sb.append("    public ").append(name).append(" get").append(name).append("(@RequestBody ").append(name).append(" bean){\n");
        sb.append("        return ").append(camel).append("Dao.get").append(name).append("(bean);\n");
        sb.append("    }\n\n");
        sb.append("    @GetMapping(\"/getList\")\n");
        sb.append("    public List<").append(name).append("> getList(HttpServletRequest request){\n");
        sb.append("        ").append(name).append(" bean = WebUtil.parseObject(request,").append(name).append(".class);\n");
        sb.append("        return ").append(camel).append("Dao.get").append(name).append("List(bean);\n");
        sb.append("    }\n\n");
        sb.append("    @PostMapping(\"/getList\")\n");
        sb.append("    public List<").append(name).append("> getList(@RequestBody ").append(name).append(" bean){\n");
        sb.append("        return ").append(camel).append("Dao.get").append(name).append("List(bean);\n");
        sb.append("    }\n\n");
    }

    public static void insert(String name, StringBuilder sb) {
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        sb.append("    @GetMapping(\"/insert").append(name).append("\")\n");
        sb.append("    public boolean insert").append(name).append("(HttpServletRequest request){\n");
        sb.append("        ").append(name).append(" bean = WebUtil.parseObject(request,").append(name).append(".class);\n");
        sb.append("        return ").append(camel).append("Dao.insert").append(name).append("(bean) > 0;\n");
        sb.append("    }\n\n");
        sb.append("    @PostMapping(\"/insert").append(name).append("\")\n");
        sb.append("    public boolean insert").append(name).append("(@RequestBody ").append(name).append(" bean){\n");
        sb.append("        return ").append(camel).append("Dao.insert").append(name).append("(bean) > 0;\n");
        sb.append("    }\n\n");
    }

    public static void update(String name, StringBuilder sb) {
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        sb.append("    @GetMapping(\"/update").append(name).append("\")\n");
        sb.append("    public boolean update").append(name).append("(HttpServletRequest request){\n");
        sb.append("        ").append(name).append(" bean = WebUtil.parseObject(request,").append(name).append(".class);\n");
        sb.append("        return ").append(camel).append("Dao.update").append(name).append("(bean) > 0;\n");
        sb.append("    }\n\n");
        sb.append("    @PostMapping(\"/update").append(name).append("\")\n");
        sb.append("    public boolean update").append(name).append("(@RequestBody ").append(name).append(" bean){\n");
        sb.append("        return ").append(camel).append("Dao.update").append(name).append("(bean) > 0;\n");
        sb.append("    }\n\n");
    }

    public static void delete(String name, StringBuilder sb) {
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        sb.append("    @GetMapping(\"/delete").append(name).append("\")\n");
        sb.append("    public boolean delete").append(name).append("(HttpServletRequest request){\n");
        sb.append("        ").append(name).append(" bean = WebUtil.parseObject(request,").append(name).append(".class);\n");
        sb.append("        return ").append(camel).append("Dao.delete").append(name).append("(bean) > 0;\n");
        sb.append("    }\n\n");
        sb.append("    @PostMapping(\"/delete").append(name).append("\")\n");
        sb.append("    public boolean delete").append(name).append("(@RequestBody ").append(name).append(" bean){\n");
        sb.append("        return ").append(camel).append("Dao.delete").append(name).append("(bean) > 0;\n");
        sb.append("    }\n\n");
        sb.append("    @GetMapping(\"/delete").append(name).append("ById\")\n");
        sb.append("    public boolean delete").append(name).append("ById(HttpServletRequest request){\n");
        sb.append("        ").append(name).append(" bean = WebUtil.parseObject(request,").append(name).append(".class);\n");
        sb.append("        return ").append(camel).append("Dao.delete").append(name).append("ById(bean) > 0;\n");
        sb.append("    }\n\n");
        sb.append("    @PostMapping(\"/delete").append(name).append("ById\")\n");
        sb.append("    public boolean delete").append(name).append("ById(@RequestBody ").append(name).append(" bean){\n");
        sb.append("        return ").append(camel).append("Dao.delete").append(name).append("ById(bean) > 0;\n");
        sb.append("    }\n\n");
        sb.append("    @PostMapping(\"/delete").append(name).append("List\")\n");
        sb.append("    public boolean delete").append(name).append("List(@RequestBody List<").append(name).append("> list){\n");
        sb.append("        return ").append(camel).append("Dao.delete").append(name).append("List(list) > 0;\n");
        sb.append("    }\n\n");
    }

}
