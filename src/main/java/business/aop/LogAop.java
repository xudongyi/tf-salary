package business.aop;

import business.annotation.Log;
import business.bean.AuthUser;
import business.bean.OperateLog;
import business.constant.Constants;
import business.emum.OperLogType;
import business.service.IAuthUserService;
import business.service.IOperateLogService;
import business.util.IpAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/*** @Aspect 标记当前类为功能增强类 切面类 *
 *  @Configuration 标记当前类为配置类 这个注解包含了@Component的功能
 */
@Aspect
@Configuration
@Slf4j
public class LogAop {


    @Resource
    private IAuthUserService iAuthUserService;

    @Resource
    private IOperateLogService iOperateLogService;

    /**
     * JoinPoint 连接点 就是切入点 通过这个对象可以获取切入点的相关所有信息 例如：被切入的方法和注解
     *
     * @param joinPoint ** 切入点的设置 切注解 @annotation *
     */
    @After("@annotation(business.annotation.Log)")
    public void logAfter(JoinPoint joinPoint) {
        InsertLog(joinPoint);
    }

    public void InsertLog(JoinPoint joinPoint) {
        //一个日志的实体，用来保存日志信息
        OperateLog operateLog = new OperateLog();
        //获取用户的请求
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 从 http 请求头中取出 token
        String token = request.getHeader(Constants.AUTHENTICATION);
        if(token!=null){
            token = token.split(" ")[1];
        }
        //根据token拿到用户对象
        AuthUser customerInfo = iAuthUserService.getUserByToken(token);
        if (customerInfo!=null){
            operateLog.setUserId(customerInfo.getLoginid());
        }
        operateLog.setOperateTime(new Date());
        /**
         * 获取用户的ip
         * 通过工具类 ip
         */
        operateLog.setIp(IpAddressUtil.getIp());

        /**
         * 操作的描述
         * 执行的方法不同
         * 获取注解的值
         */
//        1.通过连接点获取方法签名 被切入方法的所有信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        2.获取被切入方法对象
        Method method = signature.getMethod();
//        3.获取方法上的注解
        Log annotation = method.getAnnotation(Log.class);
//        4.获取注解的值
        String value = annotation.value();
        operateLog.setContent(value);
        // 获取注解的类型
        OperLogType type = annotation.type();
        if (type!=null){
            operateLog.setOperateType(type.TYPE());
            operateLog.setOperateName(type.NAME());
        }
        operateLog.setContent(value);
        System.out.println(operateLog);
        log.debug("loginLog===="+ operateLog);
        boolean save = iOperateLogService.save(operateLog);
        log.debug("保存日志------"+save);
    }
}