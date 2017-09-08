package com.mine.oa.exception;

/***
 *
 * 〈输入参数异常〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class InParamException extends RuntimeException {

    public InParamException() {
        super();
    }

    public InParamException(String message) {
        super(message);
    }
}
