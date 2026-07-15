package com.fs.model;

/**
 * 通用响应结果封装类
 * 用于统一API接口的返回格式
 * @param <T> 数据类型
 */
public class Result<T> {
    /** 状态码 */
    private Integer code;

    /** 响应消息 */
    private String msg;

    /** 请求是否成功 */
    private Boolean success;

    /** 响应数据 */
    private T data;

    /** 数据总数（用于分页场景） */
    private Integer count;

    /** 无参构造方法 */
    public Result() {
    }

    /**
     * 全参构造方法
     * @param code 状态码
     * @param msg 响应消息
     * @param success 请求是否成功
     * @param data 响应数据
     * @param count 数据总数
     */
    public Result(Integer code, String msg, Boolean success, T data, Integer count) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.data = data;
        this.count = count;
    }

    /**
     * 获取状态码
     * @return 状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置状态码
     * @param code 状态码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取响应消息
     * @return 响应消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置响应消息
     * @param msg 响应消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取成功标识
     * @return 成功标识
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 设置成功标识
     * @param success 成功标识
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * 获取响应数据
     * @return 响应数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置响应数据
     * @param data 响应数据
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * 获取数据总数
     * @return 数据总数
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置数据总数
     * @param count 数据总数
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 判断请求是否成功
     * @return true-成功，false-失败
     */
    public boolean isSuccess() {
        return success != null && success;
    }

    /**
     * 重写toString方法，方便调试和日志输出
     * @return 结果对象的字符串表示
     */
    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                ", data=" + data +
                ", count=" + count +
                '}';
    }
}
