package com.twg.encrypt;

import com.twg.encrypt.annotation.Decrypt;
import com.twg.encrypt.utils.AESUtils;
import com.twg.encrypt.utils.EncryptProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 解码接口
 * @author :twg
 * @date :2022/6/20
 * @description :
 */
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class DecryptRequest extends RequestBodyAdviceAdapter {

    @Autowired
    EncryptProperties properties;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {

        //判断参数或者方法是否有Decrypt注解，如果有表示该接口需要解密处理
        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
    }

    /**
     * 这个方法会在参数转换具体对象之前执行，先从流中加载到数据，然后对数据进行解密，解密完成后再重新构造HttpInputMessage返回
     * @param inputMessage
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     * @throws IOException
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        System.out.println("请求前置处理");

        byte[] bytes = new byte[inputMessage.getBody().available()];

        inputMessage.getBody().read(bytes);

        try{
            byte[] decrypt = AESUtils.decrypt(bytes, properties.getKey().getBytes());

            final ByteArrayInputStream inputStream = new ByteArrayInputStream(decrypt);

            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    return inputStream;
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }
}
