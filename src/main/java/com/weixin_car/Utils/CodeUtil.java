package com.weixin_car.Utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

public class CodeUtil {
    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        System.out.println(property);
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/weixin_car?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC",
                "root",
                "Abc20020217!")
                .globalConfig(builder -> {
                    builder.author("liHeWei") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(property+"/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com") // 设置父包名
                            .moduleName("weixin_car") // 设置父包模块名
                            .entity("pojo")
                            .mapper("mapper")
                            .service("service")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, property+"/src/main/resources/mapper"));

                })
                .strategyConfig(builder -> {
                    builder.addInclude("user_car"); // 设置需要生成的表名
                })
                .execute();
    }
}
