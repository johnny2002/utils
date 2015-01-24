/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.common.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import com.ibm.gbsc.common.vo.PagedQueryParam;
import com.ibm.gbsc.common.vo.PagedQueryResult;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public class JpaHelper {
	public <T> PagedQueryResult<T> executePagingQuery(EntityManager em, CriteriaQuery<T> criteria, PagedQueryParam query) {
		TypedQuery<T> pgQuery = em.createQuery(criteria);
		if (query.getPageNumber() > 1) {
			pgQuery.setFirstResult((query.getPageNumber() - 1) * query.getPageSize());
		}
		int totalResults = 0;
		pgQuery.setMaxResults(query.getPageSize());
		// pgQuery.setReadOnly(true);
		List<T> list = pgQuery.getResultList();
		if (query.getPageNumber() == 1 && list.size() < query.getPageSize()) {
			totalResults = list.size();

		} else {

		}
		PagedQueryResult<T> result = new PagedQueryResult<T>(totalResults, query.getPageSize(), query.getPageNumber());
		result.setDatas(list);
		return result;
	}

}
