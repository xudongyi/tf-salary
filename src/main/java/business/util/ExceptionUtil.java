package business.util;

import business.constant.Constants;
import business.emum.ErrorEnum;
import io.micrometer.core.instrument.util.StringUtils;

/**
 * @Author:xudy
 * @Date:2018/09/27 12:52
 */
public class ExceptionUtil {

    /**
     * 业务回滚，抛出特定异常：包含错误消息
     */
    public static void rollback(String message) {
        throw new BusinessException(message);
    }

    /**
     * 业务回滚，抛出特定异常：包含错误消息，错误编码
     */
    public static void rollback(String message, int code) {
        throw new BusinessException(message, code);
    }

    /**
     * 业务回滚，抛出特定异常：包含错误消息，错误原因
     */
    public static void rollback(String message, Throwable cause) {
        throw new BusinessException(message, cause);
    }

    /**
     * 业务回滚，抛出特定异常：包含错误消息，错误编码，错误原因
     */
    public static void rollback(String message, int code, Throwable cause) {
        throw new BusinessException(message, code, cause);
    }

    public static void rollback(ErrorEnum errorEnum) {
        throw new BusinessException(errorEnum);
    }

    public static void isRollback(boolean flag, ErrorEnum errorEnum){
        if (flag){
            rollback(errorEnum);
        }
    }
}