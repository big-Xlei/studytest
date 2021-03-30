package com.xl.study.studytest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xl.study.studytest.beans.extend.OptionInfoExtends;
import com.xl.study.studytest.beans.service.OptionInfoService;
import io.netty.handler.codec.json.JsonObjectDecoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class StudytestApplicationTests {

    @Autowired
    private OptionInfoService optionInfoService;


    @Test
    void contextLoads() {
        List<OptionInfoExtends> optionInfoExtends = optionInfoService.allOptionTree();
        log.info(optionInfoExtends.toString());
        String s = JSON.toJSONString(optionInfoExtends);
        System.out.println(s);
    }



}
