package com.twg.encrypt.utils;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author :twg
 * @date :2022/6/20
 * @description : 读取配置文件中的加密key
 */
@ConfigurationProperties(prefix = "spring.encrypt")
public class EncryptProperties {

    /**
     * 默认加密key
     */
    private final static String DEFAULT_KEY = "com.tong.encrypt";

    private String controller_packages;

    private String key = DEFAULT_KEY;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getController_packages() {
        return controller_packages;
    }

    public void setController_packages(String controller_packages) {
        this.controller_packages = controller_packages;
    }
}
