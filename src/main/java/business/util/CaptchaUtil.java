package business.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CaptchaUtil {

    static Map<String,Object> concurrentHashMap=new ConcurrentHashMap<String,Object>();
    /**
     * 保存验证码信息
     * @param mobile  手机号码
     * @param code  验证码
     * @param expire 有效时间，单位(秒)
     */
    public static void save(
            String mobile,
            String code,
            int expire){
        concurrentHashMap.put("sms_mobile_"+mobile, mobile);
        concurrentHashMap.put("sms_code_"+mobile, code);
        concurrentHashMap.put("sms_createTime_"+mobile, System.currentTimeMillis());
        concurrentHashMap.put("sms_expire_"+mobile, expire);
    }
    /**
     * 校验验证码
     * @param mobile  手机号码
     * @param code  验证码
     */
    public static String validate(
            String mobile,
            String code){
        String sessionMobile = blank(concurrentHashMap.get("sms_mobile_"+mobile));
        String sessionCode = blank(concurrentHashMap.get("sms_code_"+mobile));
        String createTime = blank(concurrentHashMap.get("sms_createTime_"+mobile));
        String expire = blank(concurrentHashMap.get("sms_expire_"+mobile));
        if(sessionMobile.equals(""))
            return "未生成验证码";
        if(!sessionMobile.equals(mobile)){
            return "手机号错误";
        }
        if(!sessionCode.equals(code)){
            return "验证码错误";
        }
        if((System.currentTimeMillis() - Long.parseLong(createTime)) > 1000 * Integer.parseInt(expire)){
            return "验证码已过期";
        }
        save("", "", 0);
        return "";
    }
    private static String blank(Object s){
        if(s == null)
            return "";
        return s.toString();
    }
}
