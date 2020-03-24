package com.jcohao.user.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Table(name = "tb_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4, max = 30, message = "用户名需在 4-30 位之间")
    private String username;    // 用户名

    @JsonIgnore                 // 不能把与密码相关的信息返回
    @Length(min = 6, max = 30, message = "用户名需在 6-30 位之间")
    private String password;    // 密码

    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    private String phone;       // 电话

    private Date created;       // 创建时间

    @JsonIgnore
    private String salt;        // 密码加密值
}
