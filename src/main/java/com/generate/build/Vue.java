package com.generate.build;

import com.generate.annotation.Column;
import com.generate.mapping.CamelMapping;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Vue {

    public static void write(String path, Map<String, Object> map) {
        String name = map.get("name").toString();
        path += name + ".vue";
        File file = new File(path);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            StringBuilder sb = new StringBuilder();
            sb.append("<template><div>");
            query(sb, map);
            sb.append("<div style=\"margin-top: -10px;margin-bottom: -10px\">\n" +
                    "<el-form>\n" +
                    "<el-form-item>\n" +
                    "<el-button @click=\"insertDialog=true\" type=\"primary\" size=\"mini\" icon=\"el-icon-plus\">添加</el-button>\n" +
                    "<el-button @click=\"deleteList\" type=\"danger\" size=\"mini\" icon=\"el-icon-delete\">删除</el-button>\n" +
                    "</el-form-item>\n" +
                    "</el-form>\n" +
                    "</div>");
            table(sb, map);
            insertDialog(sb, map);
            updateDialog(sb, map);
            sb.append("</div></template>");
            sb.append("<script>\nexport default {");
            data(sb, map);
            created(sb, map);
            methods(sb, map);
            sb.append("}\n</script>");
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void query(StringBuilder sb, Map<String, Object> map) {
        Field[] fields = (Field[]) map.get("fields");
        Column column;
        String name;
        sb.append("<div>");
        sb.append("<el-form :model=\"query\" ref=\"query\" :inline=\"true\">");
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class);
                name = field.getName();
                sb.append("<el-form-item label=\"").append(column.remark()).append("\" prop=\"").append(name).append("\">\n").append("<el-input v-model=\"query.").append(name).append("\" placeholder=\"请输入").append(column.remark()).append("\" @keyup.enter.native=\"search\" size=\"mini\" clearable style=\"width: 90%\"/>\n")
                        .append("</el-form-item>");
            }
        }
        sb.append("<el-form-item>\n" +
                "<el-button @click=\"search(1)\" size=\"mini\" type=\"primary\" icon=\"el-icon-search\">查询</el-button>\n" +
                "<el-button @click=\"reset('query')\" size=\"mini\" type=\"primary\" icon=\"el-icon-search\">重置</el-button>\n" +
                "</el-form-item>");
        sb.append("</el-form>");
        sb.append("</div>");
    }

    public static void table(StringBuilder sb, Map<String, Object> map) {
        Field[] fields = (Field[]) map.get("fields");
        Column column;
        String name, remark;
        sb.append("<div>");
        sb.append("<el-table @selection-change=\"selectionChange\" :data=\"tableData\" style=\"width: 100%\" v-loading=\"loading\"  :header-cell-style=\"{background:'#F8F8F9',color:'black',padding:'9px'}\" :cell-style=\"{padding:'7px'}\">");
        sb.append("<el-table-column align=\"center\" type=\"selection\" width=\"55\"/>");
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class);
                remark = column.remark();
                if ("".equals(remark)) continue;
                name = field.getName();
                sb.append("<el-table-column align=\"center\" prop=\"").append(name).append("\" label=\"").append(column.remark()).append("\"/>");
            }
        }
        sb.append("<el-table-column align=\"center\" label=\"操作\">\n" +
                "<template slot-scope=\"scope\">\n" +
                "<el-button @click=\"update(scope.row)\" type=\"text\" size=\"mini\" icon=\"el-icon-edit\">修改</el-button>\n" +
                "<el-button @click=\"remove(scope.row)\" type=\"text\" style=\"color: red\" size=\"mini\" icon=\"el-icon-delete\">删除</el-button>\n" +
                "</template>\n" +
                "</el-table-column>");
        sb.append("</el-table>");
        sb.append("<div align=\"center\" style=\"margin-top: 10px\">\n" +
                "<el-pagination background layout=\"prev, pager, next\" :total=\"total\" :page-size=\"query.pageSize\" @current-change=\"search\"/>\n" +
                "</div>");
        sb.append("</div>");
    }

    public static void insertDialog(StringBuilder sb, Map<String, Object> map) {
        String name = map.get("name").toString();
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        Field[] fields = (Field[]) map.get("fields");
        Column column;
        String fieldName, remark;
        sb.append("<div>");
        sb.append("<el-dialog title=\"添加\" :visible.sync=\"insertDialog\" top=\"5vh\" width=\"30%\" @close=\"").append(camel).append("={}\" :close-on-click-modal=\"false\">\n");
        sb.append("<el-form :model=\"").append(camel).append("\" :rules=\"rules\" ref=\"insert\" label-width=\"100px\">");
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class);
                fieldName = field.getName();
                if (column.isAutoIncrement() || !"createTime".equals(fieldName) && !"updateTime".equals(fieldName)) {
                    remark = column.remark();
                    sb.append("<el-form-item label=\"").append(remark).append("\" prop=\"").append(fieldName).append("\">\n").append("<el-input v-model=\"").append(camel).append(".").append(fieldName).append("\" placeholder=\"请输入").append(remark).append("\" size=\"small\" style=\"width: 75%\"/>\n").append("</el-form-item>");
                }
            }
        }
        sb.append("</el-form>");
        sb.append("<div align=\"center\">\n" +
                "<el-button @click=\"add\" type=\"primary\" size=\"small\">添加</el-button>\n" +
                "<el-button @click=\"reset('insert');insertDialog=false\" size=\"small\">取消</el-button>\n" +
                "</div>");
        sb.append("</el-dialog>");
        sb.append("</div>");
    }

    public static void updateDialog(StringBuilder sb, Map<String, Object> map) {
        String name = map.get("name").toString();
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        Field[] fields = (Field[]) map.get("fields");
        Column column;
        String fieldName, remark;
        sb.append("<div>");
        sb.append("<el-dialog title=\"修改\" :visible.sync=\"updateDialog\" top=\"5vh\" width=\"30%\" @close=\"").append(camel).append("={}\" :close-on-click-modal=\"false\">\n");
        sb.append("<el-form :model=\"").append(camel).append("\" :rules=\"rules\" ref=\"update\" label-width=\"100px\">");
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class);
                fieldName = field.getName();
                if (column.isAutoIncrement() || !"createTime".equals(fieldName) && !"updateTime".equals(fieldName)) {
                    remark = column.remark();
                    sb.append("<el-form-item label=\"").append(remark).append("\" prop=\"").append(fieldName).append("\">\n").append("<el-input v-model=\"").append(camel).append(".").append(fieldName).append("\" placeholder=\"请输入").append(remark).append("\" size=\"small\" style=\"width: 75%\"/>\n").append("</el-form-item>");
                }
            }
        }
        sb.append("</el-form>");
        sb.append("<div align=\"center\">\n" +
                "<el-button @click=\"save\" type=\"primary\" size=\"small\">修改</el-button>\n" +
                "<el-button @click=\"reset('update');updateDialog=false\" size=\"small\">取消</el-button>\n" +
                "</div>");
        sb.append("</el-dialog>");
        sb.append("</div>");
    }

    public static void data(StringBuilder sb, Map<String, Object> map) {
        String name = map.get("name").toString();
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        Field[] fields = (Field[]) map.get("fields");
        Column column;
        String fieldName, remark;
        sb.append("data() {return {");
        sb.append("query: {\n" + "pageNum: 1,\n" + "pageSize: 7\n" + "},\n" + "total: 0,\n").append(camel).append(": {},\n")
                .append("loading: true,\ninsertDialog: false,\nupdateDialog :false,\n")
                .append("tableData:[],\nmultipleSelection: [],\n").append("rules:{");
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class);
                fieldName = field.getName();
                if (!column.isNull() && !"createTime".equals(fieldName) && !"updateTime".equals(fieldName)) {
                    remark = column.remark();
                    sb.append(fieldName).append(":[{required: true, message: '请输入").append(remark).append("', trigger: 'blur'}],");
                }
            }
        }
        sb.append("},\n");
        sb.append("}},\n");
    }

    public static void created(StringBuilder sb, Map<String, Object> map) {
        sb.append("created() {");
        sb.append("this.search(1);\n");
        sb.append("},\n");
    }

    public static void methods(StringBuilder sb, Map<String, Object> map) {
        String name = map.get("name").toString();
        String camel = CamelMapping.toLowerCaseFirstOne(name);
        sb.append("methods: {");
        sb.append("search(index) {\n" + "this.loading = true;this.query.pageNum = index;\n" + "axios.post('/").append(camel).append("/get").append(name).append("ByPage', this.query).then(res => {\n")
                .append("this.tableData = res.data.data.list;this.total = res.data.data.total;this.loading = false;\n").append("});\n").append("},\n");
        sb.append("reset(formName) {\n" +
                "this.$refs[formName].resetFields();\n" +
                "this.search(1);\n" +
                "},\n");
        sb.append("remove(data) {\n" + "this.$confirm('此操作将永久删除, 是否继续?', '提示', {\n" + "confirmButtonText: '确定',\n" + "cancelButtonText: '取消',\n" + "type: 'warning'\n" + "}).then(() => {\n" + "axios.post('/").append(camel).append("/delete").append(name).append("ById', data).then(res => {\n").append("if (res) {\n").append("this.$message.success('删除成功');this.search(this.query.pageNum);\n").append("} else {\n").append("this.$message.error('删除失败');\n").append("}\n").append("}).catch(error => {\n").append("this.$message.warning('服务器出现错误');\n").append("console.log(error);\n").append("});").append("}).catch(() => {\n").append("this.$message.info('取消删除');\n").append("});\n").append("},\n");
        sb.append("update(data) {      this.").append(camel).append(" = data;\n").append("      this.updateDialog = true;},\n");
        sb.append("add() {\n" + "this.$refs['insert'].validate((valid) => {\n" + "if (valid) {\n" + "axios.post('/").append(camel).append("/insert").append(name).append("', this.").append(camel).append(").then(res => {\n").append("if (res.data.data) {\n").append("this.$message.success('添加成功');\n").append("this.insertDialog = false;\n").append("this.search(1);\n").append("} else {\n").append("this.$message.error('添加失败');\n").append("}\n").append("}).catch(error => {\n").append("this.$message.warning('服务器出现错误');\n").append("console.log(error);\n").append(" });\n").append("}\n").append(" });\n").append("},\n");
        sb.append("save() {\n" + "this.$refs['update'].validate((valid) => {\n" + "if (valid) {\n" + "axios.post('/").append(camel).append("/update").append(name).append("', this.").append(camel).append(").then(res => {\n").append("if (res.data.data) {\n").append("this.$message.success('修改成功');\n").append("this.updateDialog = false;\n").append("this.search(1);\n").append("} else {\n").append("this.$message.error('修改失败');\n").append("}\n").append("}).catch(error => {\n").append("this.$message.warning('服务器出现错误');\n").append("console.log(error);\n").append(" });\n").append("}\n").append(" });\n").append("},\n");
        sb.append("deleteList() {\n" + "this.$confirm('此操作将永久删除, 是否继续?', '提示', {\n" + "confirmButtonText: '确定',\n" + "cancelButtonText: '取消',\n" + "type: 'warning'\n" + "}).then(() => {\n" + "const list = this.multipleSelection;\n" + "axios.post('/").append(camel).append("/delete").append(name).append("List', list).then(res => {\n").append("if (res.data.data) {\n").append("this.search(this.query.pageNum);\n").append("}\n").append("})\n").append("}).catch(() => {\n").append("this.$message.info('取消删除');\n").append("});\n").append("},\n");
        sb.append("selectionChange(val) {\nthis.multipleSelection = val;\n},\n");
        sb.append("},\n");
    }

}
