package business.emum;

public enum OperLogType {
    DEFAULT(-1, "其他日志"),
    LOGIN(0, "登录"),
    QUERY_SALARY(1, "查询工资"),
    SEND_MOBILE(2, "发送短信");
    // 成员变量
    private String name;
    private int type;

    // 构造方法
    OperLogType(int type, String name) {
        this.type = type;
        this.name = name;

    }

    //覆盖方法
    @Override
    public String toString() {
        return this.type + "_" + this.name;
    }

    public int TYPE() {
        return this.type;
    }

    public String NAME() {
        return this.name;
    }
}
