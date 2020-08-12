package business.task;

import business.bean.AuthToken;
import business.mapper.AuthTokenMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author:xudy
 * @Date:2020/08/06 12:52
 */
@Component
@Slf4j
public class SchedulerTask {

    @Autowired
    private AuthTokenMapper authTokenMapper;

    @Scheduled(cron = "0 0/1 * * * ?")
    private void scanToken() {
        log.debug(" {} 扫描过期Token", LocalDateTime.now());
        authTokenMapper.delete(new LambdaQueryWrapper<AuthToken>().le(AuthToken::getExpireTime, new Date()));
    }
}