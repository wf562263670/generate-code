package com.generate.start;


import com.generate.build.*;

import java.util.Map;


public class GenerateCodeStart {

    public static void code(String path, Map<String, Object> data) {
        Controller.write(path, data);
        Dao.write(path, data);
        Service.write(path, data);
        ServiceImpl.write(path, data);
        Mapper.write(path, data);
    }

}
