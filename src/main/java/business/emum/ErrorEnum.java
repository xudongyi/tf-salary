package business.emum;

import business.constant.Constants;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

public enum ErrorEnum {

    SUCCESS(Constants.OPERATION_SUCCESS, Constants.SUCCESS_MESSAGE, ""),
    ERROR(Constants.OPERATION_FAIL, Constants.ERROR_MESSAGE, ""),

    DATA_NO_EXIST(202, "该数据不存在", "data not exists"),
    PARAM_ERROR(203, "参数错误", ""),
    LOGIN_DISABLE(204, "账户已被禁用,请联系管理员解除限制", ""),
    LOGIN_ERROR(205, "登录失败，用户名或密码错误", ""),
    ACCESS_NO_PRIVILEGE(206, "不具备访问权限", ""),
    PARAM_INCORRECT(207, "传入参数有误", ""),
    INVALID_TOKEN(208, "token解析失败", ""),
    REGISTER_ADMIN(209, "注册失败", ""),
    ACCOUNT_EXIST(210, "账号已存在", ""),
    ACCOUNT_NOT_EXIST(211, "用户不存在", ""),
    PASSWORD_ERROR(212, "密码错误", ""),
    SYNC_POSTS_ERROR(213, "同步文章失败", ""),
    UPDATE_PASSWORD_ERROR(214, "密码修改失败", ""),
    FILE_TYPE_ERROR(215, "文件类型错误", ""),
    IMPORT_FILE_ERROR(216, "文件导入失败", ""),
    DATABASE_SQL_PARSE_ERROR(217, "数据库解析异常", ""),
    EXPIRED_TOKEN(218, "token已过期", ""),
    ;

    private final static Map<Integer, ErrorEnum> errorEnumMap = new HashMap<>();

    static {
        for (ErrorEnum errorEnum : ErrorEnum.values()) {
            errorEnumMap.put(errorEnum.code, errorEnum);
        }
    }

    private final int code;
    private final String zhMsg;
    private final String enMsg;

    ErrorEnum(int code, String zhMsg, String enMsg) {
        this.code = code;
        this.zhMsg = zhMsg;
        this.enMsg = enMsg;
    }

    public static String getMsg(@NotBlank int code) {
        return errorEnumMap.get(code).zhMsg;
    }

    public static ErrorEnum getErrorEnumMap(@NotBlank String code) {
        return errorEnumMap.get(code);
    }

    public int getCode() {
        return code;
    }

    public String getEnMsg() {
        return enMsg;
    }

    public String getZhMsg() {
        return zhMsg;
    }
}