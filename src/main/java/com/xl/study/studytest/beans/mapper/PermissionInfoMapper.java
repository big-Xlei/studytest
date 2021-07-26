package com.xl.study.studytest.beans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.study.studytest.beans.PermissionInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;

@Mapper
public interface PermissionInfoMapper extends BaseMapper<PermissionInfo> {
    Cursor<PermissionInfo> getPermissionInfos();
    void putPermissionInfos(@Param("list") List<PermissionInfo> list);

}
