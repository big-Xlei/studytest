package com.xl.study.studytest;

import com.alibaba.fastjson.JSON;
import com.xl.study.studytest.beans.PermissionInfo;
import com.xl.study.studytest.beans.extend.OptionInfoExtends;
import com.xl.study.studytest.beans.mapper.PermissionInfoMapper;
import com.xl.study.studytest.beans.service.OptionInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class StudytestApplicationTests {

    @Autowired
    private Target target;
    @Autowired
    private Snmp snmp;

    @Autowired
    private OptionInfoService optionInfoService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private PermissionInfoMapper permissionInfoMapper;


    @Test
    void contextLoads() {
        List<OptionInfoExtends> optionInfoExtends = optionInfoService.allOptionTree();
        log.info(optionInfoExtends.toString());
        String s = JSON.toJSONString(optionInfoExtends);
        System.out.println(s);
    }

    //mybatisStream流查询
    @Test
    public void mybatisStreamTest(){
        try(
               SqlSession sqlSession = sqlSessionFactory.openSession()
        ) {
            PermissionInfoMapper mapper = sqlSession.getMapper(PermissionInfoMapper.class);
            Cursor<PermissionInfo> permissionInfos = mapper.getPermissionInfos();
            for (PermissionInfo permissionInfo : permissionInfos) {
                System.out.println(permissionInfo.toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //mybatis批量插入
    @Test
    public void mybatisStreamInsertTest(){
        long beginTime = System.currentTimeMillis();

        ArrayList<PermissionInfo> listsss = new ArrayList<>();
        for (int i = 0; i <200000 ; i++) {
            PermissionInfo build = PermissionInfo.builder()
                    .id(i+4)
                    .userId(1)
                    .tableName("tableName" + i)
                    .columns(String.valueOf(i))
                    .build();
            listsss.add(build);
        }
        int size = listsss.size()/10000;

        for (int i = 0; i < size; i++) {

            permissionInfoMapper.putPermissionInfos(listsss.stream().skip(i*10000).limit(10000).collect(Collectors.toList()));
        }
        long endTime = System.currentTimeMillis();

        System.out.println("花费的时间是" +(endTime-beginTime));
    }



}
