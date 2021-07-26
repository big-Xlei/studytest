package com.xl.study.studytest.beans.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.study.studytest.beans.UserInfo;
import com.xl.study.studytest.beans.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo> {
}
