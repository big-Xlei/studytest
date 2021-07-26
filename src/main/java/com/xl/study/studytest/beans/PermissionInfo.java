package com.xl.study.studytest.beans;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("permission_info")
@AllArgsConstructor
@NoArgsConstructor
public class PermissionInfo {
    private Integer id;
    private Integer userId;
    private String tableName;
    private String columns;
}
