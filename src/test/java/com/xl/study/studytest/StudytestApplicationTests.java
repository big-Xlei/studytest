package com.xl.study.studytest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xl.study.studytest.beans.PermissionInfo;
import com.xl.study.studytest.beans.extend.OptionInfoExtends;
import com.xl.study.studytest.beans.mapper.PermissionInfoMapper;
import com.xl.study.studytest.beans.service.OptionInfoService;
import io.netty.handler.codec.json.JsonObjectDecoder;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@SpringBootTest
@Slf4j
class StudytestApplicationTests {

    @Autowired
    private OptionInfoService optionInfoService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    @Test
    void contextLoads() {
        List<OptionInfoExtends> optionInfoExtends = optionInfoService.allOptionTree();
        log.info(optionInfoExtends.toString());
        String s = JSON.toJSONString(optionInfoExtends);
        System.out.println(s);
    }

    @Test
    public void mybatisStreamTest(){
        try(
               SqlSession sqlSession = sqlSessionFactory.openSession()
        ) {
            final PermissionInfoMapper mapper = sqlSession.getMapper(PermissionInfoMapper.class);
            final Cursor<PermissionInfo> permissionInfos = mapper.getPermissionInfos();
            for (PermissionInfo permissionInfo : permissionInfos) {
                System.out.println(permissionInfo.toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
