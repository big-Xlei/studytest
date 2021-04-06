package com.xl.study.studytest.beans;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("permission_info")
public class PermissionInfo {
    private Integer id;
    private Integer userId;
    private String tableName;
    private String columns;
}
