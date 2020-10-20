package business.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 制造部门配置文件取值
 */
@Component
@ConfigurationProperties(prefix = "manufacturing-dept")
public class ManufacturingDeptConfig{
    private String deptA1AssemblyCodes;
    private String deptA1TestingCodes;
    private String deptD2AssemblyCodes;
    private String deptD2TestingCodes;
    private String deptA3AssemblyCodes;
    private String deptA3TestingCodes;
    private String packagingCodes;
    private String surfaceCodes;

    public String getDeptA1AssemblyCodes() {
        return deptA1AssemblyCodes;
    }

    public void setDeptA1AssemblyCodes(String deptA1AssemblyCodes) {
        this.deptA1AssemblyCodes = deptA1AssemblyCodes;
    }

    public String getDeptA1TestingCodes() {
        return deptA1TestingCodes;
    }

    public void setDeptA1TestingCodes(String deptA1TestingCodes) {
        this.deptA1TestingCodes = deptA1TestingCodes;
    }

    public String getDeptD2AssemblyCodes() {
        return deptD2AssemblyCodes;
    }

    public void setDeptD2AssemblyCodes(String deptD2AssemblyCodes) {
        this.deptD2AssemblyCodes = deptD2AssemblyCodes;
    }

    public String getDeptD2TestingCodes() {
        return deptD2TestingCodes;
    }

    public void setDeptD2TestingCodes(String deptD2TestingCodes) {
        this.deptD2TestingCodes = deptD2TestingCodes;
    }

    public String getDeptA3AssemblyCodes() {
        return deptA3AssemblyCodes;
    }

    public void setDeptA3AssemblyCodes(String deptA3AssemblyCodes) {
        this.deptA3AssemblyCodes = deptA3AssemblyCodes;
    }

    public String getDeptA3TestingCodes() {
        return deptA3TestingCodes;
    }

    public void setDeptA3TestingCodes(String deptA3TestingCodes) {
        this.deptA3TestingCodes = deptA3TestingCodes;
    }

    public String getPackagingCodes() {
        return packagingCodes;
    }

    public void setPackagingCodes(String packagingCodes) {
        this.packagingCodes = packagingCodes;
    }

    public String getSurfaceCodes() {
        return surfaceCodes;
    }

    public void setSurfaceCodes(String surfaceCodes) {
        this.surfaceCodes = surfaceCodes;
    }
}