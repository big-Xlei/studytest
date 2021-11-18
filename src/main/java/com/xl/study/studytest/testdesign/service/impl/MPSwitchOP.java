package com.xl.study.studytest.testdesign.service.impl;

import com.xl.study.studytest.snmp.SnmpUtils;
import com.xl.study.studytest.testdesign.service.DevOP;
import org.springframework.util.CollectionUtils;

import java.util.Map;

public class MPSwitchOP extends DevOP {

    @Override
    public float getCpuLoad(String ip, String port, String rdCommunity) {

        float result = 0;
        String cpuOID = ".1.3.6.1.4.1.5651.3.20.1.1.3.5.1.10";
        Map<String, String> CPUMap = SnmpUtils.snmpWalk(ip, rdCommunity, port, cpuOID);
        if (!CollectionUtils.isEmpty(CPUMap)) {
            String CPU = CPUMap.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
            result = Float.parseFloat(CPU);
        }
        return result;
    }
}
