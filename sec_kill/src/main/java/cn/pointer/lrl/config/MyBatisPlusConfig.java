package cn.pointer.lrl.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;

@Configuration
@MapperScan("cn.pointer.lrl.generator.mapper")
@Slf4j
public class MyBatisPlusConfig implements MetaObjectHandler {
//    @Bean
//    @Profile({"dev-lrl", "test"})
    // SQL执行效率插件
    /* public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor interceptor = new PerformanceInterceptor();
        interceptor.setMaxTime(3000);// 设置sql最大执行时间
        interceptor.setFormat(true);// sql语句执行打印是否格式化
        return interceptor;
    } */

    // 分页和乐观锁
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // 分页
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MARIADB));
        return interceptor;
    }

    @Override
    // 插入自动更新（createTime，updateTime）字段为当前时间（new Date()）
    public void insertFill(MetaObject metaObject) {
        log.info("start insert into updateTime and createTime fill...");
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    // 插入和修改自动更新updateTime字段为当前时间（new Date()）
    public void updateFill(MetaObject metaObject) {
        log.info("start update updateTime...");
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
