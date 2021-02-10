package com.generate.start;


import com.generate.build.*;
import com.generate.service.CodeService;

import java.util.Map;


public class GenerateCodeStart {

    public static void code(String path, Class<?> clazz) {
        Map<String, Object> data = CodeService.getClassInfo(clazz);
//        Controller.write(path, data);
//        Dao.write(path, data);
//        Service.write(path, data);
//        ServiceImpl.write(path, data);
//        Mapper.write(path, data);
//        Vue.write(path, data);
    }

    public static void code(Map<String, String> map, Class<?> clazz) {
        Map<String, Object> data = CodeService.getClassInfo(clazz);
        String pack = map.get("package");
        Controller.write(pack, map.get("controller"), data);
        Dao.write(pack, map.get("dao"), data);
        Service.write(pack, map.get("service"), data);
        ServiceImpl.write(pack, map.get("serviceImpl"), data);
        Mapper.write(pack, map.get("mapper"), data);
        Vue.write(map.get("vue"), data);
    }

}
