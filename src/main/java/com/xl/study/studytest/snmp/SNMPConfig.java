package com.xl.study.studytest.snmp;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
/**
    * @date 2021/9/3
    * @author xionglei
    * @description 还没有实现
    * TODO
    */
@Configuration
@Slf4j
public class SNMPConfig {

    @Value("${snmp.verson}")
    private int version;

    @Value("${snmp.address}")
    private String address;

    @Value("${snmp.community}")
    private String community;

    @Value("${snmp.retries}")
    private int retries;

    @Value("${snmp.timeout}")
    private int timeout;

    private UdpAddress udpAddress;

    @Bean
    public TransportMapping<UdpAddress> transportMapping() throws IOException {
        DefaultUdpTransportMapping transportMapping = new DefaultUdpTransportMapping();
        // 开启监听
        // 一定要开启，不然没有response
        transportMapping.listen();
        return transportMapping;
    }

    // 必须要的
    @Bean
    public Snmp snmp() throws IOException {
        return new Snmp(transportMapping());
    }

    // target地址
    @Bean
    public Address address(){
        this.udpAddress = new UdpAddress(this.address);
        return udpAddress;
    }

    @Bean
    public Target target() throws IOException {
        log.debug("------------------------【加载target】--------------------------");
        log.debug("target version: {}", this.version);
        log.debug("target address: {}", this.address);
        log.debug("target community: {}", this.community);
        log.debug("target retries: {}", this.retries);
        log.debug("target timeout: {}", this.timeout);
        Target target = null;
        if (version == SnmpConstants.version3) {
            // 添加用户
            snmp().getUSM().addUser(new OctetString("MD5DES"),new UsmUser(new OctetString("MD5DES"), AuthMD5.ID,new OctetString("MD5DESUserAuthPassword"), PrivDES.ID, new OctetString("MD5DESUserPrivPassword")));
            target = new UserTarget();
            // 设置安全级别
            ((UserTarget) target).setSecurityLevel(SecurityLevel.AUTH_PRIV);
            ((UserTarget) target).setSecurityName(new OctetString("MD5DES"));
            target.setVersion(SnmpConstants.version3);
        } else {
            target = new CommunityTarget();
            if (version == SnmpConstants.version1) {
                target.setVersion(SnmpConstants.version1);
                ((CommunityTarget) target).setCommunity(new OctetString(this.community));
            } else {
                target.setVersion(SnmpConstants.version2c);
                ((CommunityTarget) target).setCommunity(new OctetString(this.community));
            }

        }
        // 目标对象相关设置
        target.setAddress(this.udpAddress);
        target.setRetries(this.retries);
        target.setTimeout(this.timeout);
        log.debug("【target对象】： {}", target.toString());
        return target;
    }
}