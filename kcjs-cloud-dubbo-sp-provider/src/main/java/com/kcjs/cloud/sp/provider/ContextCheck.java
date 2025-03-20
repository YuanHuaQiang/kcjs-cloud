package com.kcjs.cloud.sp.provider;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Component
public class ContextCheck implements CommandLineRunner {

    private final ApplicationContext context;

    public ContextCheck(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        String[] beans = context.getBeanNamesForType(DataSource.class);
        if (beans.length == 0) {
            System.out.println("❌ 没有 DataSource Bean");
        } else {
            System.out.println("✅ 找到 DataSource：" + beans[0]);
            DataSource dataSource = (DataSource) context.getBean(beans[0]);
            System.out.println("✅ DataSource 类型：" + dataSource.getClass());
        }
    }
}