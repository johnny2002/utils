package com.ibm.gbsc.auth.user;

import com.ibm.banking.framework.dto.PagedQueryResult;

/**
 * 
 * 获取用户全部信息接口.
 * 
 * @author xushigang
 * 
 */
public interface UserAllInfoService {
	PagedQueryResult<UserAllInfo> getUserAllInfo(UserAllInfoPagedQueryParam queryParam);
}
