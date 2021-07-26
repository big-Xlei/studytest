package com.xl.study.studytest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TransactionConfig {
    @Bean
    public TransactionTemplate transactionTemplate(DataSourceTransactionManager dataSourceTransactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(dataSourceTransactionManager);
//      可以设置事务隔离级别, 默认使用数据库默认隔离级别
//		transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
//      可以设置事务传播方式
//		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//      可以设置事务超时时间
//		transactionTemplate.setTimeout(TransactionDefinition.TIMEOUT_DEFAULT);
        return transactionTemplate;
    }
}
