package com.ibm.gbsc.auth.user;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.banking.framework.dao.HibernateDao;
import com.ibm.banking.framework.dto.PagedQueryResult;

/**
 * @author xushigang
 */
@Service
@Transactional(readOnly = true)
public class UserAllInfoServiceImpl implements UserAllInfoService {
	Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	HibernateDao dao;

	/** {@inheritDoc} */
	public PagedQueryResult<UserAllInfo> getUserAllInfo(UserAllInfoPagedQueryParam queryParam) {
		Criteria criteria = dao.getSession().createCriteria(UserAllInfo.class);
		if (StringUtils.isNotBlank(queryParam.getUserId())) {
			criteria.add(Restrictions.eq("userId", queryParam.getUserId().trim()));
		}
		if (StringUtils.isNotBlank(queryParam.getUserName())) {
			criteria.add(Restrictions.ilike("userName", queryParam.getUserName().trim().toLowerCase(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(queryParam.getOrgCode())) {
			criteria.add(Restrictions.eq("orgCode", queryParam.getOrgCode().trim()));
		}
		if (StringUtils.isNotBlank(queryParam.getRoleId())) {
			criteria.add(Restrictions.eq("roleId", queryParam.getRoleId().trim()));
		}
		if (StringUtils.isNotBlank(queryParam.getPermissionType())) {
			criteria.add(Restrictions.eq("permissionType", "TYPES".equals(queryParam.getPermissionType().trim()) ? PermissionType.TYPES
					: PermissionType.SUBTYPES));
		}
		PagedQueryResult<UserAllInfo> rst = dao.executePagingQuery(criteria, queryParam);

		return rst;
	}
}
