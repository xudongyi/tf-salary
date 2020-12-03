package business.service.impl;

import business.bean.AuthToken;
import business.bean.AuthUser;
import business.bean.OperateLog;
import business.common.api.vo.Result;
import business.constant.Constants;
import business.emum.ErrorEnum;
import business.emum.OperLogType;
import business.jwt.JwtUtil;
import business.mapper.*;
import business.service.IOauthService;
import business.service.IOperateLogService;
import business.util.CaptchaUtil;
import business.util.ExceptionUtil;
import business.util.IpAddressUtil;
import business.vo.AuthUserModify;
import business.vo.AuthUserSSO;
import business.vo.AuthUserVO;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class OathServiceImpl implements IOauthService {
    @Resource
    private AuthUserMapper authUserMapper;

    @Resource
    private AuthTokenMapper authTokenMapper;

    @Resource
    private HrmResourceMapper hrmResourceMapper;

    @Resource
    private IOperateLogService iOperateLogService;
    @Resource
    private HrMapper hrMapper;

    @Resource
    private SalarySubDeptConfigMapper salarySubDeptConfigMapper;



    @Override
    public Result<?> login(AuthUserVO authUserVO) {
        log.debug("login {}", authUserVO);
        if (authUserVO == null || StringUtils.isBlank(authUserVO.getLoginid()) || StringUtils.isBlank(authUserVO.getPassword())) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }
        String psw = SecureUtil.md5(authUserVO.getPassword());
        AuthUser authUser = authUserMapper.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getPassword, psw)
                .eq(AuthUser::getLoginid, authUserVO.getLoginid()));
        if(authUser==null){
            return Result.error(500,"用户名或密码错误！");
        }
        authUserVO.setRoleId(authUser.getRoleid());
        String token = JwtUtil.getToken(new AuthUserVO().setPassword(authUser.getPassword()).setLastname(authUser.getLastname()).setId(authUser.getId()));
        authUserVO.setToken(token);
        AuthToken authToken = authTokenMapper.selectOne(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getUserId,authUser.getId()));
        if(authToken==null){
            authToken = new AuthToken();
            authToken.setUserId(authUser.getId())
                    .setToken(token);
        }else{
            authTokenMapper.deleteById(authToken.getId());
        }
        Date expireDate = new Date(Constants.EXPIRE_TIME + System.currentTimeMillis());
        authToken.setExpireTime(expireDate);
        authTokenMapper.insert(new AuthToken()
                .setUserId(authUser.getId())
                .setToken(token)
                .setExpireTime(expireDate));
        authUserVO.setExpireTime(expireDate.getTime());
        authUserVO.setLastname(authUser.getLastname());
        authUserVO.setFirst_login(authUser.getFirstLogin());
        authUserVO.setWorkcode(authUser.getWorkcode());
        authUserVO.setSite(authUser.getSite());
        //插入日志
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateType(OperLogType.LOGIN.TYPE());
        operateLog.setUserId(authUser.getLoginid());
        operateLog.setIp(IpAddressUtil.getIp());
        operateLog.setOperateTime(new Date());
        operateLog.setOperateName(OperLogType.LOGIN.NAME());
        operateLog.setContent("后台登录进入系统");
        log.debug("loginLog===="+ operateLog);
        boolean save = iOperateLogService.save(operateLog);
        log.debug("保存日志------"+save);
        return Result.ok(authUserVO);
    }

    @Override
    public Result<?> sso(AuthUserSSO authUserSSO) {
        log.debug("sso {}", authUserSSO);
        if (authUserSSO == null || StringUtils.isBlank(authUserSSO.getAppid())|| StringUtils.isBlank(authUserSSO.getLoginid())|| StringUtils.isBlank(authUserSSO.getWorkcode())) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }

        if(!authUserSSO.getAppid().equals(Constants.OA_APP_ID)){
            ExceptionUtil.rollback(ErrorEnum.PARAM_INCORRECT);
        }
        AuthUser authUser = authUserMapper.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getLoginid, authUserSSO.getLoginid()));
        if(authUser==null){
            Map<String,Object> hrmresource = hrmResourceMapper.getHrmResource(authUserSSO.getLoginid());
            Map<String,Object> hrmInfo = hrMapper.getMobilePhone(authUserSSO.getWorkcode());
            if(hrmresource==null){
                return Result.error(500,"OA系统中未找到该登录名，请核查信息！");
            }
            if(hrmInfo==null){
                return Result.error(500,"HR系统中未找到该工号，请核查信息！");
            }
            //插入数据
            authUser = new AuthUser();
            authUser.setLoginid(authUserSSO.getWorkcode());
            authUser.setRoleid(1L);
            authUser.setWorkcode(hrmresource.get("WORKCODE").toString());
            authUser.setFirstLogin(0);
            authUser.setLastname(hrmresource.get("LASTNAME").toString());
            authUser.setSite(String.valueOf(salarySubDeptConfigMapper.getSubDept(hrmInfo.get("DEPARTID").toString()).get(0).getId()));
            authUserMapper.insert(authUser);
        }
        String token = JwtUtil.getSSOToken(authUserSSO);

        AuthToken authToken = authTokenMapper.selectOne(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getUserId,authUser.getId()));
        if(authToken==null){
            authToken = new AuthToken();
            authToken.setUserId(authUser.getId())
                    .setToken(token);
        }else{
            authTokenMapper.deleteById(authToken.getId());
        }
        Date expireDate = new Date(Constants.EXPIRE_TIME + System.currentTimeMillis());
        authToken.setExpireTime(expireDate);
        authTokenMapper.insert(new AuthToken()
                .setUserId(authUser.getId())
                .setToken(token)
                .setExpireTime(expireDate));
        authUserSSO.setToken(token);
        authUserSSO.setExpireTime(expireDate.getTime());
        //插入日志
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateType(OperLogType.SSO.TYPE());
        operateLog.setUserId(authUser.getLoginid());
        operateLog.setIp(IpAddressUtil.getIp());
        operateLog.setOperateTime(new Date());
        operateLog.setOperateName(OperLogType.SSO.NAME());
        operateLog.setContent("OA进入系统");
        log.debug("loginLog===="+ operateLog);
        iOperateLogService.save(operateLog);
        return  Result.ok(authUserSSO);
    }

    @Override
    public Result<?> checkSso(AuthUserSSO authUserSSO) {
        log.debug("checkSso {}", authUserSSO);
        if (authUserSSO == null || StringUtils.isBlank(authUserSSO.getLoginid())|| StringUtils.isBlank(authUserSSO.getToken())) {
            return Result.error(500,"参数错误！");
        }
        Map<String,Object> hrmresource = hrmResourceMapper.getHrmResource(authUserSSO.getLoginid());
        if(hrmresource==null){
            return Result.error(500,"OA中不存在该用户！");
        }
        AuthUser authUser = authUserMapper.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getLoginid, authUserSSO.getLoginid()));
        if(authUser==null){
            return Result.error(500,"用户未在系统中注册，请检查用户！");
        }
        AuthToken authToken = authTokenMapper.selectOne(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getUserId,authUser.getId()).eq(AuthToken::getToken,authUserSSO.getToken()));
        if(authToken==null){
            return Result.error(500,"验证失败，请重新获取token！");
        }
        authUserSSO.setRoleId(authUser.getRoleid());
        authUserSSO.setWorkcode(authUser.getWorkcode());
        authUserSSO.setLastname(hrmresource.get("LASTNAME").toString());
        authUserSSO.setExpireTime(authToken.getExpireTime().getTime());
        authUserSSO.setFirst_login(authUser.getFirstLogin());
        authUserSSO.setSite(authUser.getSite());
        return  Result.ok(authUserSSO);
    }

    @Override
    public Result<?> sendMobile(HttpServletRequest httpRequest, AuthUserModify authUserModify) {
        if(authUserModify==null || StringUtils.isBlank(authUserModify.getPassword()) || StringUtils.isBlank(authUserModify.getMobile())){
            return Result.error(500,"参数错误！");
        }
        if(!authUserModify.getPassword().equals("-1")){
            String psw = SecureUtil.md5(authUserModify.getPassword());
            AuthUser authUser = authUserMapper.selectOne(new LambdaQueryWrapper<AuthUser>()
                    .eq(AuthUser::getPassword, psw)
                    .eq(AuthUser::getWorkcode, authUserModify.getWorkcode()));
            if(authUser==null){
                return Result.error(500,"密码错误！");
            }
        }
        Map<String,Object> hr = hrMapper.getMobilePhone(authUserModify.getWorkcode());
        if(hr==null){
            return Result.error(500,"工号在HR系统中未查询到，请检查！");
        }
        if(!authUserModify.getMobile().equals(hr.get("MOBILEPHONE"))){
            return Result.error(500,"手机号不匹配，HR中的手机号为："+hr.get("MOBILEPHONE")+"！");
        }
        //TODO 查询本月该用户的已发送次数,没有超过发送次数则发送短信，否则提示用户本月无法查询
        List<OperateLog> list = iOperateLogService.list(new LambdaQueryWrapper<OperateLog>()
                .eq(OperateLog::getUserId, authUserModify.getWorkcode())
                .ge(OperateLog::getOperateTime, DateUtil.beginOfDay(new Date()))
                .le(OperateLog::getOperateTime, new Date())
                .eq(OperateLog::getOperateType, 2));

        //TODO 发送短信
        String code = "888888";
        CaptchaUtil.save(authUserModify.getMobile(),code,180);
        //插入日志
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateType(OperLogType.SEND_MOBILE.TYPE());
        operateLog.setUserId(authUserModify.getWorkcode());
        operateLog.setIp(IpAddressUtil.getIp());
        operateLog.setOperateTime(new Date());
        operateLog.setOperateName(OperLogType.SEND_MOBILE.NAME());
        operateLog.setContent("发送短信");
        log.debug("loginLog===="+ operateLog);
        iOperateLogService.save(operateLog);
        return Result.ok("发送成功,本月已发送："+list.size()+"次");
    }


    @Override
    public Result<?> modifyPassword(HttpServletRequest httpRequest,AuthUserModify authUserModify) {
        if(authUserModify==null || StringUtils.isBlank(authUserModify.getCaptcha())
                ||StringUtils.isBlank( authUserModify.getWorkcode()) || authUserModify.getCheckPass()==null
                || authUserModify.getPassword()==null){
            return Result.error(500,"参数错误！");
        }
        //1.验证手机号和验证码是否匹配
        String result = CaptchaUtil.validate(authUserModify.getMobile(),authUserModify.getCaptcha());
        if(!result.equals("")){
            return Result.error(500,result);
        }
        //2.修改密码
        if(!authUserModify.getPassword().equals(authUserModify.getCheckPass())){
            return Result.error(500,"两次密码不一致！");
        }
        //3.
        AuthUser authUser = authUserMapper.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getWorkcode, authUserModify.getWorkcode()));
        if(authUser==null){
            return Result.error(500,"用户不存在！");
        }
        String psw = SecureUtil.md5(authUserModify.getPassword());
        authUser.setPassword(psw);
        authUser.setFirstLogin(1);
        authUserMapper.update(authUser,new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getId,authUser.getId()));
        return Result.ok("修改成功");
    }


}
