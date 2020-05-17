package org.hj.chatroomserver.model.enums;

public enum Status {
    NORMAL("正常",false),
    FREEZE("冻结中",true),
    ;
    String description;
    boolean freeze;

    Status(String description,boolean freeze) {
        this.description = description;
        this.freeze = freeze;
    }

    public static Status getStatus(boolean freeze){
        if (freeze){
            return FREEZE;
        }
        return NORMAL;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFreeze() {
        return freeze;
    }
}
