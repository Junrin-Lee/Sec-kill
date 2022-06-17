package cn.pointer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

class SecKillCreateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void gen(){
        String projectPath = System.getProperty("user.dir");//获取当前目录
        FastAutoGenerator.create("jdbc:mariadb://localhost:3306/sec_kill33",
                        "root",
                        "123456")
                .globalConfig(builder -> {
                    builder.author("lrl") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .dateType(DateType.TIME_PACK)
                            .commentDate("yyyy-MM-dd") // 设置日期格式
                            .outputDir(projectPath + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("cn") // 设置父包名
                            .moduleName("pointer.lrl.generator") // 设置父包模块名
                            .controller("controller")
                            .mapper("mapper")
                            .entity("pojo")
                            .service("service")
                            .serviceImpl("service.imp")
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.mapperXml, projectPath + "/src/main/resources/mapper/")
                            ); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("system_menu") // 设置需要生成的表名
//                            .addTablePrefix("")// 设置需要生成的表名后缀
//                            .addFieldPrefix() // 设置字段前缀
//                            .addFieldSuffix() // 设置字段后缀
                            .addTablePrefix("t_") // 设置过滤表前缀
                            .entityBuilder() // 实体策略配置
                            .enableLombok() // 开启lombok
                            .enableChainModel() // 开启链式编程
                            .versionColumnName("version") // 数据库乐观锁字段名
                            .versionPropertyName("version")// 实体类乐观锁字段名
                            .logicDeleteColumnName("deleted")// 数据库逻辑删除字段名
                            .logicDeletePropertyName("deleted")// 实体类逻辑删除字段名
//                            .naming(NamingStrategy.underline_to_camel)// 开启驼峰命名法
//                            .columnNaming(NamingStrategy.underline_to_camel)// 开启驼峰命名法，没有默认用naming
                            .idType(IdType.AUTO)// 主键生成策略
                            .disableSerialVersionUID();// 关闭序列化UID
                })
                .execute();
    }
}
