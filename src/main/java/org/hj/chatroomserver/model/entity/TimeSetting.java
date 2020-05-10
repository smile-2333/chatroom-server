package org.hj.chatroomserver.model.entity;

import java.util.Date;

public interface TimeSetting {
    Date getCreateTime();
    void setCreateTime(Date createTime) ;
    Date getUpdateTime() ;
    void setUpdateTime(Date updateTime);
    default void setAllWithCurrentTime(){
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
    }

    default void setCreateTimeWithCurrentTime(){
        this.setCreateTime(new Date());
    }

    default void setUpdateWithCurrentTime(){
        this.setUpdateTime(new Date());
    }
}
