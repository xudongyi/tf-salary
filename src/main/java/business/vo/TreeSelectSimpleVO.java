package business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TreeSelectSimpleVO {

    private String id;

    private String pId;

    private String value;

    private String title;

    private boolean selectable = false;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @JsonProperty("pId")
    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
}
