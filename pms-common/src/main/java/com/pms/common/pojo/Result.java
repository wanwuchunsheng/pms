package com.pms.common.pojo;

import java.io.Serializable;

import com.pms.common.constant.IErrorCode;
import com.pms.common.constant.ResultCode;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用返回对象
 * Created by macro on 2019/4/19.
 */
@Setter
@Getter
public class Result<T> implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer code;
    
    private String message;
    
    private T data;

    private Long count;

    protected Result() {
    	
    }

    protected Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    protected Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Result(Integer code,Long total,T data) {
        this.data = data;
        this.code = code;
        this.count = total;
    }
    
    public Result(Integer code,Long total,T data, String message) {
        this.data = data;
        this.code = code;
        this.count = total;
        this.message = message;
    }
    
    /**
     * 返回查询总数量跟自定义数据
     * @param data
     * @return
     */
    public static<T> Result<T> data(Integer code,Long total, T data) {
        return new Result<T>(code, total ,data);
    }
    
    /**
     * 返回查询总数量跟自定义数据
     * @param data
     * @return
     */
    public static<T> Result<T> data(Integer code,Long total, T data,String msg) {
        return new Result<T>(code,total,data,msg);
    }
    
    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
    
    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), message, data);
    }
    
    /**
     * 成功返回自定义结果
     * @param errorCode 错误码
     */
    public static <T> Result<T> success(Integer successCode,String successMsg, T data) {
        return new Result<T>(successCode, successMsg, data);
    }

    
    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static <T> Result<T> failed(IErrorCode errorCode) {
        return new Result<T>(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> failed(Integer errorCode, String errorMessage) {
        return new Result<T>(errorCode, errorMessage);
    }
    
    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> failed(String message) {
        return new Result<T>(ResultCode.FAILED.getCode(), message);
    }
    
    /**
     * 失败返回自定义结果
     * @param errorCode 错误码
     */
    public static <T> Result<T> failed(Integer errorCode,String errorMsg, T data) {
        return new Result<T>(errorCode, errorMsg, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized(T data) {
        return new Result<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden(T data) {
        return new Result<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }
   

}
