package com.csu.backend.controller.exception;

import com.csu.backend.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ValidationController {

    /**
     * 当发生ValidationException异常时，即前端请求发生参数校验不通过时，此方法会被调用。
     * 它记录了异常的类名和消息，然后返回一个包含错误信息的RestBean对象。输出到控制台。
     *
     * @param exception 发生的ValidationException异常
     * @return 包含错误信息的RestBean对象
     */
    @ExceptionHandler(ValidationException.class)
    public RestBean<Void> validateException(ValidationException exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(400,"请求参数有误");
    }
}
