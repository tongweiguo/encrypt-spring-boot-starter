package com.twg.encrypt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twg.encrypt.annotation.Encrypt;
import com.twg.encrypt.model.RespBean;
import com.twg.encrypt.utils.AESUtils;
import com.twg.encrypt.utils.EncryptProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 加密接口<br/>
 * 设置ResponseBody返回时数据加密<br/>
 * 注意：ResponseBodyAdvice需要在使用了@ResponseBody注解时才会生效<br/>
 * @author :twg
 * @date :2022/6/20
 * @description :
 */
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class EncryptResponse implements ResponseBodyAdvice<RespBean> {

    private final ObjectMapper om = new ObjectMapper();

    @Autowired
    EncryptProperties properties;

    /**
     * 该方法用于判断当前请求，是否需要执行beforeBodyWrite方法
     * @param methodParameter 方法的参数对象
     * @param aClass http消息转换器类型
     * @return 返回true表示需要加密
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //方法参数是否有Encrypt注解，如果有表示该接口需要加密处理
        return methodParameter.hasMethodAnnotation(Encrypt.class);
    }

    /**
     * 这个方法会在响应之前执行，也就是会在响应之前对数据进行处理再返回
     * @param respBean 返回对象
     * @param methodParameter 方法的参数对象
     * @param mediaType 方法类型
     * @param aClass http消息转换器类型
     * @param serverHttpRequest 服务请求
     * @param serverHttpResponse 服务响应
     * @return RespBean返回模型
     */
    @Override
    public RespBean beforeBodyWrite(RespBean respBean, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //获取加密key
        byte[] key = properties.getKey().getBytes();
        try{
            if(respBean.getMsg() != null){
                //响应模型的提示信息不为空执行加密重新设置进去
                respBean.setMsg(AESUtils.encrypt(respBean.getMsg().getBytes(), key));
            }
            if(respBean.getData() != null){
                //响应模型的数据不为空执行加密重新设置进去
                respBean.setData(AESUtils.encrypt(om.writeValueAsBytes(respBean.getData()), key));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return respBean;
    }
}
