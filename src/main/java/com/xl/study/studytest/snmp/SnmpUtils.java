package com.xl.study.studytest.snmp;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.junit.Test;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/**
    * @date 2021/9/3
    * @author xionglei
    * @description 还没有实现
    * TODO
    */
public class SnmpUtils {

    public static CommunityTarget createDefault(String ip, String community, String port) {
        if (StringUtils.isBlank(ip)) {
            throw new NullPointerException("ip is null.");
        }
        if (StringUtils.isBlank(community)) {
            throw new NullPointerException("community is null.");
        }
        Address address = GenericAddress.parse("udp:" + ip + "/" + port);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setVersion(SnmpConstants.version2c);
        target.setTimeout(2000);
        target.setRetries(1);
        return target;
    }

    public static List<Map<String, String>> snmpGet(String ip, String community, String port, String oid) {
        if (StringUtils.isBlank(ip)) {
            throw new NullPointerException("ip is null.");
        }

        if (StringUtils.isBlank(community)) {
            throw new NullPointerException("community is null.");
        }

        List<Map<String, String>> list = null;
        CommunityTarget target = createDefault(ip, community, port);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);

            PDU response = respEvent.getResponse();
            if (null != response && response.size() > 0) {
                list = new ArrayList<Map<String, String>>();
                for (VariableBinding vb : response.getVariableBindings()) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(oid, vb.toValueString());
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSnmp(snmp);
        }
        return list;
    }

    public static void snmpGetList(String ip, String community, String port, List<String> oidList) {
        if (StringUtils.isBlank(ip)) {
            throw new NullPointerException("ip is null.");
        }

        if (StringUtils.isBlank(community)) {
            throw new NullPointerException("community is null.");
        }

        if (null == oidList || oidList.isEmpty()) {
            throw new NullPointerException("oidList is null.");
        }

        CommunityTarget target = createDefault(ip, community, port);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();

            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            PDU response = respEvent.getResponse();
            if (null != response && response.size() > 0) {
                for (VariableBinding vb : response.getVariableBindings()) {
                    System.out.println(vb.getOid() + " = " + vb.getVariable());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeSnmp(snmp);
        }
    }

    public static void snmpAsynGetList(String ip, String community, String port, List<String> oidList) {
        if (StringUtils.isBlank(ip)) {
            throw new NullPointerException("ip is null.");
        }

        if (StringUtils.isBlank(community)) {
            throw new NullPointerException("community is null.");
        }

        if (null == oidList || oidList.isEmpty()) {
            throw new NullPointerException("oidList is null.");
        }

        CommunityTarget target = createDefault(ip, community, port);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();

            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            pdu.setType(PDU.GET);

            /*异步获取*/
            final CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                @Override
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    PDU response = event.getResponse();
                    if (null != response && response.size() > 0) {
                        for (VariableBinding vb : response.getVariableBindings()) {
                            System.out.println(vb.getOid() + " = " + vb.getVariable());
                        }
                        latch.countDown();
                    }
                }
            };

            pdu.setType(PDU.GET);
            snmp.send(pdu, target, null, listener);
            boolean wait = latch.await(30, TimeUnit.SECONDS);
            snmp.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeSnmp(snmp);
        }
    }

    public static Map<String, String> snmpWalk(String ip, String community, String port, String targetOid) {
        CommunityTarget target = createDefault(ip, community, port);
        TransportMapping transport = null;
        Snmp snmp = null;
        Map<String, String> map = new HashMap();
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();

            PDU pdu = new PDU();
            OID targetOID = new OID(targetOid);
            pdu.add(new VariableBinding(targetOID));

            boolean finished = false;
            while (!finished) {
                VariableBinding vb = null;
                ResponseEvent respEvent = snmp.getNext(pdu, target);

                PDU response = respEvent.getResponse();

                if (null == response) {
                    System.out.println("responsePDU == null");
                    finished = true;
                    break;
                } else {
                    vb = response.get(0);
                }
                finished = checkWalkFinished(targetOID, pdu, vb);
                if (!finished) {
//                    System.out.println(vb.getOid() + " = " + vb.getVariable());
                    pdu.setRequestID(new Integer32(0));
                    pdu.set(0, vb);
                    map.put(vb.getOid().toString(), vb.getVariable().toString());
                } else {
                    snmp.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeSnmp(snmp);
        }
        return map;
    }

    public static void closeSnmp(Snmp snmp) {
        try {
            if (null != snmp) {
                snmp.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
        boolean finished = false;
        if (pdu.getErrorStatus() != 0) {
            System.out.println("[true] responsePDU.getErrorStatus() != 0");
            System.out.println(pdu.getErrorStatusText());
            finished = true;
        } else if (vb.getOid() == null) {
            System.out.println("[true] vb.getOid() == null");
            finished = true;
        } else if (vb.getOid().size() < targetOID.size()) {
            System.out.println("[true] vb.getOid().size() < targetOID.size()");
            finished = true;
        } else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
//            System.out.println("[true] targetOID.leftMostCompare() != 0");
            finished = true;
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            System.out.println("[true] Null.isExceptionSyntax(vb.getVariable().getSyntax())");
            finished = true;
        } else if (vb.getOid().compareTo(targetOID) <= 0) {
            System.out.println("[true] Variable received is not " + "lexicographic successor of requested " + "one:");
            System.out.println(vb.toString() + " <= " + targetOID);
            finished = true;
        }
        return finished;

    }

    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    //统一判断snmp的返回数据是否有误
    public static Boolean ifListTrue(List<Map<String, String>> list, String oid) {
        return list != null && list.size() > 0 && list.get(0).get(oid) != null && !list.get(0).get(oid).equals("noSuchInstance") && !list.get(0).get(oid).equals("noSuchObject");
    }

    public static String getChinese(String octetString) {    //snmp4j遇到中文直接转成16进制字符串
        try {
            String[] temps = octetString.split(":");
            byte[] bs = new byte[temps.length];
            for (int i = 0; i < temps.length; i++) {
                bs[i] = (byte) Integer.parseInt(temps[i], 16);
            }
            return new String(bs, "GB2312");
        } catch (Exception e) {
            return octetString;
        }
    }

    @Test
    public void ttttt(){
        String local = "127.0.0.1";
        String netBridge="virbr";

        String netIndexOid = ".1.3.6.1.2.1.4.20.1.2";
        String netDescOid = ".1.3.6.1.2.1.2.2.1.2";
        String netTypeOid = ".1.3.6.1.2.1.2.2.1.3";
        String netSpeedOid = ".1.3.6.1.2.1.2.2.1.5";
        String netMacAddrOid = ".1.3.6.1.2.1.2.2.1.6";
        String netStateOid = ".1.3.6.1.2.1.2.2.1.8";
        String netInOctetsOid = ".1.3.6.1.2.1.2.2.1.10";
        String netOutOctetsOid = ".1.3.6.1.2.1.2.2.1.16";
        String svrIp = "192.168.1.114";


        Map<String, String> netDescMap = snmpWalk(svrIp, "public", "161", netDescOid);
        System.out.println("网卡描述 " + netDescMap);

        Map<String, String> netIndexMap = snmpWalk(svrIp, "public", "161", netIndexOid);
        System.out.println("网卡index " + netIndexMap);

        Map<String, String> netTypeMap = snmpWalk(svrIp, "public", "161", netTypeOid);
        System.out.println("网卡type " + netTypeMap);

        Map<String, String> netSpeedMap = snmpWalk(svrIp, "public", "161", netSpeedOid);
        System.out.println("网卡speed " + netSpeedMap);

        Map<String, String> netMacAddrMap = snmpWalk(svrIp, "public", "161", netMacAddrOid);
        System.out.println("网卡MacAddr " + netMacAddrMap);

        Map<String, String> netStateMap = snmpWalk(svrIp, "public", "161", netStateOid);
        System.out.println("网卡state " + netStateMap);

        Map<String, String> netInOctetsMap = snmpWalk(svrIp,  "public", "161", netInOctetsOid);
        System.out.println("第一次网卡接收字节数 " + netInOctetsMap);

        Map<String, String> netOutOctetsMap = snmpWalk(svrIp, "public", "161", netOutOctetsOid);
        System.out.println("第一次网卡发送字节数 " + netOutOctetsMap);

        for (Map.Entry<String, String> ipAndIndexEntry : netIndexMap.entrySet()) {
            String key = ipAndIndexEntry.getKey();
            String ip = key.substring(netIndexOid.length());
            String index = ipAndIndexEntry.getValue();
            String arm = netDescOid.substring(1)+ "." + index;

            String netDesc = netDescMap.get(arm);
            netDesc = getChinese(netDesc);
            if (netDesc == null || netDesc.contains(netBridge) || ip.equals(local) ){
                continue;
            }

            String netType = netTypeMap.get(netTypeOid.substring(1) + "."+ index);
            String netSpeed = netSpeedMap.get(netSpeedOid.substring(1) + "." + index);
            String netMacAddr = netMacAddrMap.get(netMacAddrOid.substring(1) + "." + index);
            String netState = netStateMap.get(netStateOid.substring(1) + "." + index);
            String firstnetInOctets = netInOctetsMap.get(netInOctetsOid.substring(1) + "." + index);
            System.out.println(firstnetInOctets);
            String firsetnetOutOctets = netOutOctetsMap.get(netOutOctetsOid.substring(1) + "." + index);
            System.out.println(firsetnetOutOctets);
            System.out.println("数据对象："+ip+"_"+netDesc+"_"+netType+"_"+netSpeed+"_"+netMacAddr+"_"+netState+"_"+firsetnetOutOctets+"_"+firstnetInOctets);

        }

        // 出和入方向速度15秒更新一次(15s 计算一次 kb/s；历史数据为5min 计算一次)
    }

    @Test
    public void testtt(){

        Map<String, String> aPublic1 = snmpWalk("192.168.1.10", "public", "161", ".1.3.6.1.2.1.2.2.1.2");
        System.out.println(aPublic1);
        Map<String, String> aPublic2 = snmpWalk("192.168.1.10", "public", "161", ".1.3.6.1.2.1.2.2.1.10");
        System.out.println(aPublic2);
    }
}