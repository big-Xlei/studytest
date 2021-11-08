package com.xl.study.studytest.testdesign;

import com.xl.study.studytest.testdesign.service.DevOP;

public class SnmpServiceUtil {

    public static float getCpuLoad(String ip, String port, String rdCommunity, DevOP devOP){
        float cpuLoad = devOP.getCpuLoad(ip, rdCommunity, port);
        return cpuLoad;
    }
}
