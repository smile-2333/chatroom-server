package org.hj.chatroomserver.util.persistence.query;

import java.util.Date;

/**
 * @author Nxcat
 * @version 1.0
 * @description 时间查询
 * @date 2019/8/5
 */
public interface TimeQuery {

    /**
     * 时间字段
     */
    String getTimeField();

    /**
     * 开始时间
     */
    Date getStartTime();

    /**
     * 结束时间
     */
    Date getEndTime();

}
