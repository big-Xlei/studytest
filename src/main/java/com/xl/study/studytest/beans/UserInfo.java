package com.xl.study.studytest.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@TableName("user_info")
@Data
@Builder
public class UserInfo {
    private String userName;
    private String password;
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String role;
}
