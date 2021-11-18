package com.xl.study.studytest;

import com.xl.study.studytest.testdesign.SnmpServiceUtil;
import com.xl.study.studytest.testdesign.service.DevOP;
import com.xl.study.studytest.testdesign.service.impl.MPSwitchOP;
import com.xl.study.studytest.testdesign.service.impl.SvrOP;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication
public class StudytestApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudytestApplication.class, args);
    }
    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    @Test
    public void aaa(){

        ExecutorService service = Executors.newFixedThreadPool(3);
        final CountDownLatch latch = new CountDownLatch(2);
        for (int i = 0; i < 2; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("子线程" + Thread.currentThread().getName() + "开始执行");
                        Thread.sleep((long) (Math.random() * 1000));
                        System.out.println("子线程"+Thread.currentThread().getName()+"执行完成");
                        latch.countDown();//当前线程调用此方法，则计数减一
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            service.execute(runnable);
        }

        try {
            System.out.println("主线程"+Thread.currentThread().getName()+"等待子线程执行完成...");
            latch.await();//阻塞当前线程，直到计数器的值为0
            System.out.println("主线程"+Thread.currentThread().getName()+"开始执行...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void bbb(){
        DevOP SvrOP = new SvrOP();
        float leoxiong1 = SnmpServiceUtil.getCpuLoad("192.168.1.106", "public", "161", SvrOP);
        System.out.println(leoxiong1);
    }

    @Test
    public void bbbbb(){
        long a = 100000000;
        long b = 1111111;
        long l = (a / b) / 1000;
        System.out.println(l);
    }


}
