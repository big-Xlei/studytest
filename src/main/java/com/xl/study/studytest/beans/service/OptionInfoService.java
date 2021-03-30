package com.xl.study.studytest.beans.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.study.studytest.beans.OptionInfo;
import com.xl.study.studytest.beans.extend.OptionInfoExtends;
import com.xl.study.studytest.beans.mapper.OptionInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionInfoService extends ServiceImpl<OptionInfoMapper, OptionInfo> {

    public List<OptionInfoExtends> allOptionTree(){
        return this.baseMapper.allOptionTree();
    }
}
