package org.hj.chatroomserver.util.persistence.query;

import java.util.List;

/**
 * @author Nxcat
 * @version 1.0
 * @description 数据查询权限
 * @date 2019/8/5
 */
public interface DataAuthorityQuery {

    /**
     * 设置充电站id 【二级运营商数据权限】
     */
    void setStationIds(List<String> stationIds);

    /**
     * 获取充电站id 【二级运营商数据权限】
     */
    List<String> getStationIds();

}
