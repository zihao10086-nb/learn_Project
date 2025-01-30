package com.qiaochu.usercenter.common;

/**
 * 返回工具类
 */
public class ResultUtils {
    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"success");
    }
    /**
     * 失败
     */

    public static  BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    public static  BaseResponse error(int code,String message,String description){
        return new BaseResponse<>(code,null,message,description);
    }
    public static  BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse<>(errorCode.getCode(),null,message,description);
    }
    public static  BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse<>(errorCode.getCode(),null,errorCode.getMessage(),description);
    }
}
