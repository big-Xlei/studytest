package com.xl.study.studytest.moredbconf;

public enum DBTypeEnum {
    mysql("mysql"),
    pgsql("pgsql");

    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
