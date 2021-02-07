package com.generate.start;


import com.generate.build.*;
import com.generate.service.ExecuteService;

import java.util.Map;


public class GenerateCodeStart {

    public static void code(String path, Class<?> clazz) {
        Map<String, Object> data = ExecuteService.getClassInfo(clazz);
        Controller.write(path, data);
        Dao.write(path, data);
        Service.write(path, data);
        ServiceImpl.write(path, data);
        Mapper.write(path, data);
    }

}
