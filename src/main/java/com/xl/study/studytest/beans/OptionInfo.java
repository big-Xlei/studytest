package com.xl.study.studytest.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("tb_option_info")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OptionInfo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 选项id(唯一主键)
     */
    @TableId(value = "option_id", type = IdType.AUTO)
    private Integer optionId;

    /**
     * 标签id
     */
    private Integer labelId;

    /**
     * 选项名称
     */
    @NotEmpty(message = "选项名称 不能为空")
    private String optionName;

    /**
     * 选项说明
     */
    @NotEmpty(message = "选项说明 不能为空")
    private String optionDesc;

    /**
     * 选项类别（1-平铺  2-层级）
     */
    private Integer optionType;

    /**
     * 层级选项才有该值
     */
    private String optionClassic;

    /**
     * 选项级别
     */
    private Integer level;

    /**
     * 0-删除 1-有效
     */
    private Integer dataFlag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    private LocalDateTime updateTime;

    private Integer createUserId;

    /**
     * 1-启用，2-禁用
     */
    private Integer optionStatus;

    public void tt(){
        System.out.println("这是OptionInfo");
    }
}
