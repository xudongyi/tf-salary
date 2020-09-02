package business.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.util.Map;
@Slf4j
public class MyBatisWapperUtil {
    public static final String SQL_RULES_COLUMN = "SQL_RULES_COLUMN";

    private static final String BEGIN = "_begin";
    private static final String END = "_end";
    /**
     * 数字类型字段，拼接此后缀 接受多值参数
     */
    private static final String MULTI = "_MultiString";
    private static final String STAR = "*";
    private static final String COMMA = ",";
    private static final String NOT_EQUAL = "!";
    /**页面带有规则值查询，空格作为分隔符*/
    private static final String QUERY_SEPARATE_KEYWORD = " ";
    /**高级查询前端传来的参数名*/
    private static final String SUPER_QUERY_PARAMS = "superQueryParams";
    /** 高级查询前端传来的拼接方式参数名 */
    private static final String SUPER_QUERY_MATCH_TYPE = "superQueryMatchType";
    /** 单引号 */
    public static final String SQL_SQ = "'";
    /**排序列*/
    private static final String ORDER_COLUMN = "column";
    /**排序方式*/
    private static final String ORDER_TYPE = "order";
    private static final String ORDER_TYPE_ASC = "ASC";

    /**时间格式化 */
    private static final ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>();
    private static SimpleDateFormat getTime(){
        SimpleDateFormat time = local.get();
        if(time == null){
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            local.set(time);
        }
        return time;
    }

    /**
     * 获取查询条件构造器QueryWrapper实例 通用查询条件已被封装完成
     * @param parameterMap request.getParameterMap()
     * @return QueryWrapper实例
     */
    //多字段排序 TODO 需要修改前端
    public static void doMultiFieldsOrder(QueryWrapper<?> queryWrapper,Map<String, String[]> parameterMap,String alias) {
        String column=null,order=null;
        if(parameterMap!=null&& parameterMap.containsKey(ORDER_COLUMN)) {
            column = parameterMap.get(ORDER_COLUMN)[0];
        }
        if(parameterMap!=null&& parameterMap.containsKey(ORDER_TYPE)) {
            order = parameterMap.get(ORDER_TYPE)[0];
        }
        log.debug("排序规则>>列:"+column+",排序方式:"+order);
        if (StringUtils.isNotEmpty(column) && StringUtils.isNotEmpty(order)) {
            String colunmAlias=camelToUnderline(column);
            if(alias!=null){
                colunmAlias=alias+"."+camelToUnderline(column);
            }
            if (order.toUpperCase().indexOf(ORDER_TYPE_ASC)>=0) {
                    queryWrapper.orderByAsc(colunmAlias);
            } else {
                queryWrapper.orderByDesc(colunmAlias);
            }
        }
    }
    //多字段排序 TODO 需要修改前端
    public static void doMultiFieldsOrder(QueryWrapper<?> queryWrapper,Map<String, String[]> parameterMap) {
        doMultiFieldsOrder(queryWrapper,parameterMap,null);
    }
    /**
     * 将驼峰命名转化成下划线
     * @param para
     * @return
     */
    public static String camelToUnderline(String para){
        if(para.length()<3){
            return para.toLowerCase();
        }
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        //从第三个字符开始 避免命名不规范
        for(int i=2;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return sb.toString().toLowerCase();
    }

}
