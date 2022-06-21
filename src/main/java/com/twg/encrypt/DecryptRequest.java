package com.twg.encrypt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twg.encrypt.annotation.Decrypt;
import com.twg.encrypt.model.RespBean;
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
 * Controller层前置处理
 * @author :twg
 * @date :2022/6/20
 * @description :
 */
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class DecryptRequest extends RequestBodyAdviceAdapter {

    @Autowired
    EncryptProperties properties;

    /**
     * 该方法用于判断当前请求，是否需要执行beforeBodyRead方法
     * <br/>
     * 注意：此判断方法，会在beforeBodyRead和afterBodyRead方法前都会执行一次
     * @param methodParameter 方法的参数对象
     * @param type 方法的参数类型
     * @param aClass 将会用到的http消息转换器类型
     * @return 返回true则会执行beforeBodyRead
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        //判断参数或者方法是否有Decrypt注解，如果有表示该接口需要解密处理
        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
    }

    /**
     * 在http消息转换器执行转换之前执行
     * <br/>
     * 这个方法会在参数转换具体对象之前执行，先从流中加载到数据，然后对数据进行解密，解密完成后再重新构造HttpInputMessage返回
     * @param inputMessage 客户端的请求数据
     * @param parameter 方法的参数对象
     * @param targetType 方法的参数类型
     * @param converterType 将会用到的http消息转换器类型
     * @return 返回一个自定义的HttpInputMessage
     * @throws IOException 异常
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if(inputMessage.getBody().available() <= 0){
            return inputMessage;
        }

        byte[] bytes = new byte[inputMessage.getBody().available()];

        inputMessage.getBody().read(bytes);


        try{
            byte[] decrypt = AESUtils.decrypt(bytes, properties.getKey().getBytes());

            final ByteArrayInputStream inputStream = new ByteArrayInputStream(decrypt);

            return new HttpInputMessage() {
                @Override
                public InputStream getBody() {
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

    /**
     * 在http消息转换器执行转换之后执行
     * @param body 转换后的对象
     * @param inputMessage 客户端的请求数据
     * @param parameter 方法的参数对象
     * @param targetType 方法的参数类型
     * @param converterType 将会用到的http消息转换器类型
     * @return 返回一个新的对象
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    /**
     * 类似afterBodyRead方法，不过这个方法处理的是body为空的时候
     * @param body 转换后的对象
     * @param inputMessage 客户端的请求数据
     * @param parameter 方法的参数对象
     * @param targetType 方法的参数类型
     * @param converterType 将会用到的http消息转换器类型
     * @return Object
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.handleEmptyBody(body, inputMessage, parameter, targetType, converterType);
    }
}
