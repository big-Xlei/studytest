package com.xl.study.studytest.snmp;

import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class SnmpServiceUtil {

    /**
     * CPU利用率
     * @param ip
     * @param port
     * @param rdCommunity
     * @return
     */
    public static float getCpuLoad(String ip, String port, String rdCommunity) {

        float result = 0;
        String cpuOID = ".1.3.6.1.2.1.25.3.3.1.2";
        Map<String, String> CPUMap = SnmpUtils.snmpWalk(ip, rdCommunity, port, cpuOID);
        if (!CollectionUtils.isEmpty(CPUMap)) {
            String CPU = CPUMap.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
            result = Float.parseFloat(CPU);
        }
        return result;
    }

    /**
     * 内存总量(G)
     * @param ip
     * @param port
     * @param rdCommunity
     * @return
     */
    public static float getMemSize(String ip, String port, String rdCommunity) {

        String armIndex = "";
        String diskDescOid = ".1.3.6.1.2.1.25.2.3.1.3";
        String diskAllocationOid = ".1.3.6.1.2.1.25.2.3.1.4";
        String diskStorageSizeOid = ".1.3.6.1.2.1.25.2.3.1.5";
        Map<String, String> diskDesc = SnmpUtils.snmpWalk(ip, rdCommunity, port, diskDescOid);
        for (Map.Entry<String, String> oidIndexAndDesc : diskDesc.entrySet()) {
            if (oidIndexAndDesc.getValue().toLowerCase(Locale.ROOT).equals("physical memory")) {
                String oidIndex = oidIndexAndDesc.getKey();
                armIndex = oidIndex.substring(oidIndex.lastIndexOf("."));
            }
        }
        Long allOcation = 0L;
        String armDiskAllocationOid = diskAllocationOid + armIndex;
        List<Map<String, String>> armAllocation = SnmpUtils.snmpGet(ip, rdCommunity, port, armDiskAllocationOid);
        if (!CollectionUtils.isEmpty(armAllocation)) {
            Map<String, String> oidAndAllocationMap = armAllocation.get(0);
            allOcation = Long.parseLong(oidAndAllocationMap.get(armDiskAllocationOid.substring(armDiskAllocationOid.indexOf("."))));
        }

        Long storageSize = 0L;
        String armDiskStorageSizeOid = diskStorageSizeOid + armIndex;
        List<Map<String, String>> armStorageSize = SnmpUtils.snmpGet(ip, rdCommunity, port, armDiskStorageSizeOid);
        if (!CollectionUtils.isEmpty(armStorageSize)) {
            Map<String, String> oidAndStorageSizeMap = armStorageSize.get(0);
            storageSize = Long.parseLong(oidAndStorageSizeMap.get(armDiskStorageSizeOid.substring(armDiskStorageSizeOid.indexOf("."))));
        }
        float result = new BigDecimal((float) (allOcation * storageSize) / 1024 / 1024 / 1024).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return result;
    }

    /**
     * 内存使用量(G)
     * @param ip
     * @param port
     * @param rdCommunity
     * @return
     */
    public static float getMemUseSize(String ip, String port, String rdCommunity) {

        String armIndex = "";
        float result = 0;
        String diskDescOid = ".1.3.6.1.2.1.25.2.3.1.3";
        String diskAllocationOid = ".1.3.6.1.2.1.25.2.3.1.4";
        String diskStorageUsedOid = ".1.3.6.1.2.1.25.2.3.1.6";
        Map<String, String> diskDesc = SnmpUtils.snmpWalk(ip, rdCommunity, port, diskDescOid);
        if (CollectionUtils.isEmpty(diskDesc)) {
            return result;
        }
        for (Map.Entry<String, String> oidIndexAndDesc : diskDesc.entrySet()) {
            if (oidIndexAndDesc.getValue().toLowerCase(Locale.ROOT).equals("physical memory")) {
                String oidIndex = oidIndexAndDesc.getKey();
                armIndex = oidIndex.substring(oidIndex.lastIndexOf("."));
            }
        }
        Long allOcation = 0L;
        String armDiskAllocationOid = diskAllocationOid + armIndex;
        List<Map<String, String>> armAllocation = SnmpUtils.snmpGet(ip, rdCommunity, port, armDiskAllocationOid);
        if (!CollectionUtils.isEmpty(armAllocation)) {
            Map<String, String> oidAndAllocationMap = armAllocation.get(0);
            allOcation = Long.parseLong(oidAndAllocationMap.get(armDiskAllocationOid.substring(armDiskAllocationOid.indexOf("."))));
        }

        Long storageUse = 0L;
        String armDiskStorageUseOid = diskStorageUsedOid + armIndex;
        List<Map<String, String>> armStorageUse = SnmpUtils.snmpGet(ip, rdCommunity, port, armDiskStorageUseOid);
        if (!CollectionUtils.isEmpty(armStorageUse)) {
            Map<String, String> oidAndStorageUseMap = armStorageUse.get(0);
            storageUse = Long.parseLong(oidAndStorageUseMap.get(armDiskStorageUseOid.substring(armDiskStorageUseOid.indexOf("."))));
        }
        result = new BigDecimal((float) (allOcation * storageUse) / 1024 / 1024 / 1024).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return result;
    }

    /**
     * 网卡入流量 (字节)
     * @param ip
     * @param port
     * @param rdCommunity
     * @return
     */
    public static Map<Integer, Long> getIfInOctets(String ip, String port, String rdCommunity){
        String netInOctetsOid = ".1.3.6.1.2.1.2.2.1.10";
        Map<String, String> OidAndOctetsMap = SnmpUtils.snmpWalk(ip, rdCommunity, port, netInOctetsOid);
        Map<Integer, Long> resultMap = OidAndOctetsMap.entrySet().stream().collect(Collectors.toMap(entry -> {
            String key = entry.getKey();
            Integer resultKey = Integer.parseInt(key.substring(key.lastIndexOf(".") + 1));
            return resultKey;
        }, entry -> {
            long resultValue = Long.parseLong(entry.getValue());
            return resultValue;
        }));
        return resultMap;
    }

    /**
     * 单个网卡入流量 (字节)
     * @param ip
     * @param port
     * @param rdCommunity
     * @param index 网卡index
     * @return
     */
    public static Long getIfInOctets(String ip, String port, String rdCommunity, Integer index){
        String netInOctetsOid = ".1.3.6.1.2.1.2.2.1.10";
        String armNetInOctetsOid = netInOctetsOid+"."+index;
        long result = 0;
        List<Map<String, String>> inOctetsList = SnmpUtils.snmpGet(ip, rdCommunity, port, armNetInOctetsOid);
        if (!CollectionUtils.isEmpty(inOctetsList)) {
            Map<String, String> inOctetsMap = inOctetsList.get(0);
            result = Long.parseLong(inOctetsMap.get(armNetInOctetsOid));

        }
        return result;
    }

    /**
     * 网卡出流量 (字节)
     * @param ip
     * @param port
     * @param rdCommunity
     * @return
     */
    public static Map<Integer, Long> getIfOutOctets(String ip, String port, String rdCommunity){

        String netOutOctetsOid = ".1.3.6.1.2.1.2.2.1.16";
        Map<String, String> OidAndOctetsMap = SnmpUtils.snmpWalk(ip, rdCommunity, port, netOutOctetsOid);
        Map<Integer, Long> resultMap = OidAndOctetsMap.entrySet().stream().collect(Collectors.toMap(entry -> {
            String key = entry.getKey();
            Integer resultKey = Integer.parseInt(key.substring(key.lastIndexOf(".") + 1));
            return resultKey;
        }, entry -> {
            long resultValue = Long.parseLong(entry.getValue());
            return resultValue;
        }));
        return resultMap;
    }

    /**
     * 单个网卡出流量 (字节)
     * @param ip
     * @param port
     * @param rdCommunity
     * @param index 网卡index
     * @return
     */
    public static Long getIfOutOctets(String ip, String port, String rdCommunity, Integer index){

        String netOutOctetsOid = ".1.3.6.1.2.1.2.2.1.16";
        String armNetOutOctetsOid = netOutOctetsOid+"."+index;
        long result = 0;
        List<Map<String, String>> inOctetsList = SnmpUtils.snmpGet(ip, rdCommunity, port, armNetOutOctetsOid);
        if (!CollectionUtils.isEmpty(inOctetsList)) {
            Map<String, String> inOctetsMap = inOctetsList.get(0);
            result = Long.parseLong(inOctetsMap.get(armNetOutOctetsOid));
        }
        return result;
    }

    /**
     * 网卡Speed (bit)
     * @param ip
     * @param port
     * @param rdCommunity
     * @return
     */
    public static Map<Integer, Long> getIfSpeed(String ip, String port, String rdCommunity){

        String netSpeedOid = ".1.3.6.1.2.1.2.2.1.5";
        Map<String, String> netSpeedMap = SnmpUtils.snmpWalk(ip, rdCommunity, port, netSpeedOid);
        Map<Integer, Long> resultMap = netSpeedMap.entrySet().stream().collect(Collectors.toMap(entry -> {
            String key = entry.getKey();
            Integer resultKey = Integer.parseInt(key.substring(key.lastIndexOf(".") + 1));
            return resultKey;
        }, entry -> {
            long resultValue = Long.parseLong(entry.getValue());
            return resultValue;
        }));
        return resultMap;

    }

    /**
     * 单个网卡Speed (bit)
     * @param ip
     * @param port
     * @param rdCommunity
     * @param index 网卡index
     * @return
     */
    public static Long getIfSpeed(String ip, String port, String rdCommunity, Integer index){
        String netSpeedOid = ".1.3.6.1.2.1.2.2.1.5";
        String armNetSpeedOid = netSpeedOid+"."+index;
        long result = 0;
        List<Map<String, String>> inOctetsList = SnmpUtils.snmpGet(ip, rdCommunity, port, armNetSpeedOid);
        if (!CollectionUtils.isEmpty(inOctetsList)) {
            Map<String, String> inOctetsMap = inOctetsList.get(0);
            result = Long.parseLong(inOctetsMap.get(armNetSpeedOid));
        }
        return result;
    }
}
