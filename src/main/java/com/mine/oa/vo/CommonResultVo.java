package com.mine.oa.vo;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class CommonResultVo<T> {

    public static final int SUCCESS_CODE = 200;

    public static final int WARN_CODE = 403;

    private int code;
    private String msg;
    private T data;

    public CommonResultVo<T> success(T data) {
        this.setCode(SUCCESS_CODE);
        this.setData(data);
        return this;
    }

    public CommonResultVo() {
    }

    public CommonResultVo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
