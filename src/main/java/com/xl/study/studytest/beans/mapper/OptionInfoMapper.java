package com.xl.study.studytest.beans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.study.studytest.beans.OptionInfo;
import com.xl.study.studytest.beans.extend.OptionInfoExtends;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OptionInfoMapper extends BaseMapper<OptionInfo> {

    /**
     * 查询树
     * @return
     */
    @Select("SELECT * FROM `tb_option_info` where option_classic is null and option_type =2 and option_id = 79")
    @Results(id = "allOptionTree",value = {
            @Result(property = "optionId",column = "option_id"),
            @Result(property = "labelId",column = "label_id"),
            @Result(property = "optionName",column = "option_name"),
            @Result(property = "optionDesc",column = "option_desc"),
            @Result(property = "optionType",column = "option_type"),
            @Result(property = "optionClassic",column = "option_classic"),
            @Result(property = "level",column = "level"),
            @Result(property = "dataFlag",column = "data_flag"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "updateTime",column = "update_time"),
            @Result(property = "createUserId",column = "create_user_id"),
            @Result(property = "optionStatus",column = "option_status"),
            @Result(property = "lists",column = "option_id",many = @Many(select = "com.xl.study.studytest.beans.mapper.OptionInfoMapper.sonOptionTree"))
    })
    List<OptionInfoExtends> allOptionTree();

    @Select("SELECT * FROM `tb_option_info` where option_classic = ${option_id} and option_type =2")
    @Results(id = "sonOptionTree",value = {
            @Result(property = "optionId",column = "option_id"),
            @Result(property = "labelId",column = "label_id"),
            @Result(property = "optionName",column = "option_name"),
            @Result(property = "optionDesc",column = "option_desc"),
            @Result(property = "optionType",column = "option_type"),
            @Result(property = "optionClassic",column = "option_classic"),
            @Result(property = "level",column = "level"),
            @Result(property = "dataFlag",column = "data_flag"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "updateTime",column = "update_time"),
            @Result(property = "createUserId",column = "create_user_id"),
            @Result(property = "optionStatus",column = "option_status"),
            @Result(property = "lists",column = "option_id",many = @Many(select = "com.xl.study.studytest.beans.mapper.OptionInfoMapper.sonOptionTree"))
    })
    List<OptionInfoExtends> sonOptionTree(@Param("option_id") Integer option_id);
}
