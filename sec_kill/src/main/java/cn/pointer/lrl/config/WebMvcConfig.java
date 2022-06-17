package cn.pointer.lrl.config;

import cn.pointer.lrl.interceptor.AccessLimitedInterceptor;
import cn.pointer.lrl.interceptor.LoginInterceptor;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
//    @Autowired
//    UserArgResolver userArgResolver;

    /*
     * 配置视图控制器，实现无业务逻辑服务端跳转
     * */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toLogin").setViewName("login");// 登录界面
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录状态判断拦截器
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**") //所有请求路径都需要经过该拦截器进行过滤
                // 需要排除的拦截请求路径
                .excludePathPatterns("/common/captcha") //验证码
                .excludePathPatterns("/common/upload") //文件上传
                .excludePathPatterns("/js/**", "/bootstrap/**", "/img/**", "/jquery-validation/**", "/layer/**", "/lib/**", "/api/**", "/css/**") //静态资源
                .excludePathPatterns("/toLogin") //登陆界面
                .excludePathPatterns("/login/doRegister") //注册请求
                .excludePathPatterns("/login/doLogin") //登录请求
        ;

        // 接口防刷拦截器
        registry.addInterceptor(new AccessLimitedInterceptor()).addPathPatterns("/**");
    }

    /*
     * 配置静态资源处理器，过滤静态资源
     * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    /*
     * 配置参数解析器
     * */
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(userArgResolver);
//    }

    /**
     * 自定义使用FastJsonHttpMessageConverter
     */
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.QuoteFieldNames,
                SerializerFeature.WriteMapNullValue,// 保留空的字段
                SerializerFeature.WriteNullListAsEmpty,// List null-> []
                SerializerFeature.WriteDateUseDateFormat,// 日期格式化
                SerializerFeature.WriteNullStringAsEmpty);// String null -> ""

        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypeList);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        return fastJsonHttpMessageConverter;
    }

    // 保证StringHttpMessageConverter在FastJsonHttpMessageConverter前被调用
    /*
     * 配置额外的消息转化器
     * */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(fastJsonHttpMessageConverter());
//        converters.size();
    }
}