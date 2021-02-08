package com.generate.test;

import com.generate.annotation.Column;
import com.generate.annotation.Table;
import lombok.Data;

@Data
@Table(name = "sys_user")
public class User {

    @Column(remark = "编号")
    private String id;

    @Column(remark = "姓名")
    private String userName;

    @Column(remark = "账号")
    private String loginName;

    @Column(remark = "密码")
    private String password;

}
