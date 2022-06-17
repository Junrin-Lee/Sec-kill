package cn.pointer.lrl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流，防盗刷
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimited {

    //指定时间，redis数据过期时间
    int second();

    //指定时间内，最多的请求的次数
    int maxCount();

}
