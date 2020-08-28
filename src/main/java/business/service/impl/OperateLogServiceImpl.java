package business.service.impl;

import business.annotation.Log;
import business.bean.AuthUser;
import business.bean.OperateLog;
import business.common.api.vo.Result;
import business.constant.Constants;
import business.emum.OperLogType;
import business.mapper.AuthTokenMapper;
import business.mapper.AuthUserMapper;
import business.mapper.OperateLogMapper;
import business.service.IAuthUserService;
import business.service.IOperateLogService;
import business.util.IpAddressUtil;
import business.vo.AuthUserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements IOperateLogService {

    @Resource
    OperateLogMapper operateLogMapper;

    @Resource
    private IAuthUserService iAuthUserService;
    @Override
    public void insertLog(OperLogType operateTye,String content) {
        OperateLog operateLog = new OperateLog();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(Constants.AUTHENTICATION);
        if(token==null){
            return;
        }
        if(token!=null){
            token = token.split(" ")[1];
        }
        AuthUser customerInfo = iAuthUserService.getUserByToken(token);
        if (customerInfo!=null){
            operateLog.setUserId(customerInfo.getLoginid());
        }
        operateLog.setOperateTime(new Date());
        operateLog.setIp(IpAddressUtil.getIp());
        operateLog.setOperateType(operateTye.TYPE());
        operateLog.setOperateName(operateTye.NAME());
        operateLog.setContent(content);
        this.save(operateLog);
    }

    @Override
    public IPage<OperateLog> queryLog(IPage<OperateLog> page) {
        return operateLogMapper.queryLog(page);
    }
}