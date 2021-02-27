package com.generate.build;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Controller {

    public static void write(String name, String path) {
        StringBuilder sb = new StringBuilder();
        String lowerCase = name.toLowerCase(Locale.ROOT);
        File file = new File(path + ("/" + name + "Controller.java"));
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            basic(name, lowerCase, sb);
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void basic(String name, String lowerCase, StringBuilder sb) {
        sb.append("@RestController\n");
        sb.append("@RequestMapping(\"/").append(lowerCase).append("\")\n");
        sb.append("public class ").append(name).append("Controller {\n\n");
        sb.append("    @Autowired\n");
        sb.append("    private ").append(name).append("Dao ").append(lowerCase).append("Dao;\n\n");
        sb.append("    @Autowired\n");
        sb.append("    private ").append(name).append("Service ").append(lowerCase).append("Service;\n\n");
        page(name, lowerCase, sb);
        list(name, lowerCase, sb);
        get(name, lowerCase, sb);
        insert(name, lowerCase, sb);
        update(name, lowerCase, sb);
        delete(name, lowerCase, sb);
        sb.append("}");
    }

    public static void page(String name, String lowerCase, StringBuilder sb) {
        //GET
        sb.append("    @GetMapping(\"/page\")\n");
        sb.append("    public PageInfo<").append(name).append("> page(HttpServletRequest request) {\n");
        sb.append("        int pageNum = Integer.parseInt(request.getParameter(\"pageNum\"));\n");
        sb.append("        int pageSize = Integer.parseInt(request.getParameter(\"pageSize\"));\n");
        sb.append("        ").append(name).append(" bean = WebUtil.parseObject(request, ").append(name).append(".class);\n");
        sb.append("        return ").append(lowerCase).append("Service.getPage(pageNum, pageSize, bean);\n");
        sb.append("    }\n\n");
        //POST
        sb.append("    @PostMapping(\"/page\")\n");
        sb.append("    public PageInfo<").append(name).append("> page(@RequestBody JSONObject jsonObject) {\n");
        sb.append("        Map<String, Object> map = JSONUtil.parseJSON(jsonObject);\n");
        sb.append("        int pageNum = Integer.parseInt(map.get(\"pageNum\").toString());\n");
        sb.append("        int pageSize = Integer.parseInt(map.get(\"pageSize\").toString());\n");
        sb.append("        ").append(name).append(" bean = JSONUtil.parseObject(jsonObject, ").append(name).append(".class);\n");
        sb.append("        return ").append(lowerCase).append("Service.getPage(pageNum, pageSize, bean);\n");
        sb.append("    }\n\n");
    }

    public static void list(String name, String lowerCase, StringBuilder sb) {
        //GET
        sb.append("    @GetMapping(\"/list\")\n");
        sb.append("    public List<").append(name).append("> list(HttpServletRequest request) {\n");
        sb.append("        ").append(name).append(" bean = WebUtil.parseObject(request, ").append(name).append(".class);\n");
        sb.append("        return ").append(lowerCase).append("Dao.getList(bean);\n");
        sb.append("    }\n\n");
        //POST
        sb.append("    @PostMapping(\"/list\")\n");
        sb.append("    public List<").append(name).append("> list(@RequestBody ").append(name).append(" bean) {\n");
        sb.append("        return ").append(lowerCase).append("Dao.getList(bean);\n");
        sb.append("    }\n\n");
    }

    public static void get(String name, String lowerCase, StringBuilder sb) {
        //GET
        sb.append("    @GetMapping(\"/get").append(name).append("\")\n");
        sb.append("    public ").append(name).append(" getUser(HttpServletRequest request) {\n");
        sb.append("        ").append(name).append(" bean = WebUtil.parseObject(request, ").append(name).append(".class);\n");
        sb.append("        return ").append(lowerCase).append("Dao.get").append(name).append("(bean);\n");
        sb.append("    }\n\n");
        //POST
        sb.append("    @PostMapping(\"/get").append(name).append("\")\n");
        sb.append("    public ").append(name).append(" getUser(@RequestBody ").append(name).append(" bean) {\n");
        sb.append("        return ").append(lowerCase).append("Dao.get").append(name).append("(bean);\n");
        sb.append("    }\n\n");
    }

    public static void insert(String name, String lowerCase, StringBuilder sb) {
        //POST
        sb.append("    @PostMapping(\"/add").append(name).append("\")\n");
        sb.append("    public boolean add").append(name).append("(@RequestBody ").append(name).append(" bean) {\n");
        sb.append("        return ").append(lowerCase).append("Dao.add").append(name).append("(bean) > 0;\n");
        sb.append("    }\n\n");
    }

    public static void update(String name, String lowerCase, StringBuilder sb) {
        //POST
        sb.append("    @PostMapping(\"/update").append(name).append("\")\n");
        sb.append("    public boolean update").append(name).append("(@RequestBody ").append(name).append(" bean) {\n");
        sb.append("        return ").append(lowerCase).append("Dao.update").append(name).append("(bean) > 0;\n");
        sb.append("    }\n\n");
    }

    public static void delete(String name, String lowerCase, StringBuilder sb) {
        //POST
        sb.append("    @PostMapping(\"/delete").append(name).append("\")\n");
        sb.append("    public boolean delete").append(name).append("(@RequestBody ").append(name).append(" bean) {\n");
        sb.append("        return ").append(lowerCase).append("Dao.delete").append(name).append("(bean) > 0;\n");
        sb.append("    }\n\n");
    }

}
