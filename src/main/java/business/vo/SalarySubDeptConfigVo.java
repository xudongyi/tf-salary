package business.vo;

import business.bean.SalarySubDeptConfig;
import business.bean.SalarySubDeptConfigDt;
import lombok.Data;

import java.util.List;

@Data
public class SalarySubDeptConfigVo extends SalarySubDeptConfig {
    public SalarySubDeptConfigVo() {
    }

    public SalarySubDeptConfigVo(SalarySubDeptConfig config) {
        this.setId(config.getId());
        this.setSubName(config.getSubName());
        this.setSort(config.getSort());
    }

    private List<SalarySubDeptConfigDt> details;

    private Object[] detail;

    private List<SalarySubDeptConfigVo.SubDepartLabel> subDepartLabel;

    @Data
    public static class SubDepartLabel{
        private String label;
        private String value;
    }
}
