package business.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Slf4j
@Component
public class MessageUtil {
    @Value("${message.url}")
    public String base_url;

    @Value("${message.spcode}")
    public String spcode;

    @Value("${message.loginname}")
    public String loginname;

    @Value("${message.password}")
    public String password;
    public  Map<String, String> sendMessage(String mobile, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("SpCode", spcode); //企业编号（没特殊要求，一般都是和LoginName一样）
        params.put("LoginName", loginname); //用户编码（也叫登录账号）
        params.put("Password",password); //用户密码
        String msg = "验证码："+code+",用于薪资查询，泄露有风险，请勿转发。有效期3分钟";
        params.put("MessageContent", charsetEncode(msg, "GBK")); //短信内容, 最大480个字（短信内容要求的编码为gbk）
        params.put("MessageType", "1"); //短信类型，1=验证码、2=通知、3=广告
        params.put("UserNumber", mobile); //手机号码(多个号码用”,”分隔)，最多1000个号码
        params.put("SerialNumber", "000"+DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN)); //流水号，20位数字，唯一 （规则自定义,建议时间格式精确到毫秒）
        params.put("ScheduleTime", ""); //预约发送时间，格式:yyyyMMddhhmmss,如‘20090901010101’，立即发送请填空（预约时间要写当前时间5分钟之后的时间，若预约时间少于5分钟，则即时发送）
        params.put("ExtendAccessNum", ""); // 接入号扩展号（默认不填，扩展号为数字，扩展位数由当前所配的接入号长度决定，整个接入号最长4位）
        params.put("f", "1"); // 提交时检测方式 1 --- 提交号码中有效的号码仍正常发出短信，无效的号码在返回参数faillist中列出 不为1 或该参数不存在 --- 提交号码中只要有无效的号码，那么所有的号码都不发出短信，所有的号码在返回参数faillist中列出
        params.put("AutographId", ""); //签名编码，可选参数，如果不传则使用默认签名发送
        String result = HttpUtil.post(base_url + "send", params);
        Map<String, String> resultMap = getUrlMap(result);
        if (resultMap.containsKey("description")) {
            resultMap.put("description", charsetDecode(resultMap.get("description"), "GB2312"));
        }
        return getUrlMap(result);
    }

    /**
     * 将url中参数封装成map
     *
     * @param result
     * @return
     */
    public static Map<String, String> getUrlMap(String result) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(result)) {
            String[] arr = result.split("&");
            for (String s : arr) {
                String[] ar = s.split("=");
                if (ar.length == 1) {
                    String key = s.split("=")[0];
                    map.put(key, "");
                } else if (ar.length == 2) {
                    String key = s.split("=")[0];
                    String value = s.split("=")[1];
                    map.put(key, value);
                }
            }
        }
        return map;

    }

    /**t
     * gbk与utf-8互转
     * 利用BASE64Encoder/BASE64Decoder实现互转
     *
     * @param str
     * @return
     */
    private static String charsetEncode(String str, String charset) {
        try {
            str = URLEncoder.encode(str, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * gbk与utf-8互转
     * 利用BASE64Encoder/BASE64Decoder实现互转
     *
     * @param str
     * @return
     */
    private static String charsetDecode(String str, String charset) {
        try {
            str = URLDecoder.decode(str, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getRandom6(){
       return  getRandom(6);
    }

    public static String getRandom(int length){
        String code = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int r = random.nextInt(10); //每次随机出一个数字（0-9）
            code = code + r;  //把每次随机出的数字拼在一起
        }
        return code;
    }
}
