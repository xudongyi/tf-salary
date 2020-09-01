package business.vo;

import business.bean.OperateLog;
import lombok.Data;

@Data
public class OperateLogVO extends OperateLog {

    //部门信息
    private String dept;

    private String operateTimeST;

    private String operateTimeED;

    private String lastname;


}
