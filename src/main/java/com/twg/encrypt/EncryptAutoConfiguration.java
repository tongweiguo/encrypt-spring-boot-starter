package com.twg.encrypt;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 启动配置类
 * @author :twg
 * @date :2022/6/20
 * @description :
 */
@Configuration
@ComponentScan("com.twg.encrypt")
public class EncryptAutoConfiguration {
    public EncryptAutoConfiguration() {
        System.out.println("初始化加解密配置类");
    }
}
