package business.controller;

import business.bean.HrmResource;
import business.bean.PersonnelSalary;
import business.common.api.vo.Result;
import business.service.IHrmResourceService;
import business.service.IPersonnelSalaryService;
import business.util.FileUtils;
import business.vo.OperateLogVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchExecutorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("salaryImport")
@Slf4j
public class SalaryImportController {
    @Autowired
    private IPersonnelSalaryService personnelSalaryService;

//    @Autowired
//    private IHrmResourceService iHrmResourceService;

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result<?> importExcel(@RequestParam("file") MultipartFile file,@RequestParam("uploadDate") String uploadDate) throws Exception {
        List<PersonnelSalary> salaryList = FileUtils.importExcel(file, 0,1, PersonnelSalary.class);
        for(PersonnelSalary personnelSalary:salaryList){
            personnelSalary.setSalaryDate(uploadDate);
        }
        try {
            personnelSalaryService.saveBatch(salaryList);
        } catch(PersistenceException e){
            if(e.getCause()!=null && "org.apache.ibatis.executor.BatchExecutorException".equals(e.getCause().getClass().getName())){
                // 唯一约束异常
                BatchExecutorException executorException = (BatchExecutorException)e.getCause();
                return Result.error("第"+(executorException.getBatchUpdateException().getUpdateCounts().length+2)+"行工号当前月薪资存在重复！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return Result.error("上传失败！");
        }
        return Result.ok();
    }
}
