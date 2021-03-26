package com.xl.study.studytest.beans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.study.studytest.beans.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
