package business.vo;

import business.bean.SalaryReportConfig;
import business.bean.SalaryReportConfigDt;
import lombok.Data;

import java.util.List;
@Data
public class SalaryReportConfigVo extends SalaryReportConfig {
    public SalaryReportConfigVo() {
    }

    public SalaryReportConfigVo(SalaryReportConfig config) {
        this.setId(config.getId());
        this.setDepartName(config.getDepartName());
        this.setSite(config.getSite());
        this.setSort(config.getSort());
        this.setStage(config.getStage());
        this.setTabId(config.getTabId());
        this.setIsTotal(config.getIsTotal());
    }

    private List<SalaryReportConfigDt> details;

    private Object[] detail;

    private List<SubDepartLabel> subDepartLabel;

    @Data
    public static class SubDepartLabel{
        private String label;
        private String value;
    }

}
