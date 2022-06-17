package cn.lrl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 作用范围
@Target(ElementType.PARAMETER)
// 什么时候被解析
@Retention(RetentionPolicy.RUNTIME)
public @interface UserParamResolver {

}
