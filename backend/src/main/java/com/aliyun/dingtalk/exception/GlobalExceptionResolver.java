package com.aliyun.dingtalk.exception;

import com.aliyun.dingtalk.model.RpcServiceResult;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionResolver {

    /**
     * 处理所有不可知异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Exception.class)
    public RpcServiceResult handleException(Exception e) {
        // 打印异常堆栈信息
        log.error(e.getMessage(), e);
        return RpcServiceResult.getFailureResult(HttpStatus.INTERNAL_SERVER_ERROR.value() + "", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    /**
     * 处理api调用异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(InvokeDingTalkException.class)
    public RpcServiceResult handleInvokeDingTalkException(InvokeDingTalkException e) {
        // 打印异常堆栈信息
        log.error(e.getMessage(), e);
        return RpcServiceResult.getFailureResult(e.getErrCode(), e.getErrMsg());
    }

}
