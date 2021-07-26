package com.xl.study.studytest.beans.extend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xl.study.studytest.beans.OptionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OptionInfoExtends extends OptionInfo {
    @JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
    private List lists;
}
