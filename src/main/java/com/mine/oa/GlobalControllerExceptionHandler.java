package com.mine.oa;

import com.mine.oa.vo.CommonResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResultVo unknownException(Exception e) {
        if (e instanceof NoHandlerFoundException) {
            LOGGER.error("路径错误异常", e);
            return new CommonResultVo(404, "未找到对应请求路径。");
        }
        LOGGER.error("未捕获异常", e);
        return new CommonResultVo(500, "系统繁忙，请稍后再试。");
    }

}
