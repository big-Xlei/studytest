package com.xl.study.studytest.moredbconf;

public class DataSourceContextHolder {
    private static final ThreadLocal contextHolder = new ThreadLocal<>(); //实际上就是开启多个线程，每个线程进行初始化一个数据源
    /**
     * 设置数据源
     * @param dbTypeEnum
     */
    public static void setDbType(DBTypeEnum dbTypeEnum) {
        contextHolder.set(dbTypeEnum.getValue());
    }

    /**
     * 取得当前数据源
     * @return
     */
    public static String getDbType() {
        return (String) contextHolder.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        contextHolder.remove();
    }
}
