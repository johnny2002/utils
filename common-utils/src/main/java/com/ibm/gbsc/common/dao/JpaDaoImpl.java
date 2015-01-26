/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.common.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import com.ibm.gbsc.common.vo.PagedQueryParam;
import com.ibm.gbsc.common.vo.PagedQueryResult;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public abstract class JpaDaoImpl implements JpaDao {
	/*
	 * (non-Javadoc)
	 *
	 * @see com.ibm.gbsc.common.dao.JpaDao#executePagedQuery(javax.persistence.
	 * EntityManager, java.lang.Class, com.ibm.gbsc.common.vo.PagedQueryParam,
	 * javax.persistence.criteria.CriteriaQuery,
	 * javax.persistence.criteria.Predicate[])
	 */
	@Override
	public <T> PagedQueryResult<T> executePagedQuery(EntityManager em, Class<T> resultClazz, PagedQueryParam queryParam,
	        CriteriaQuery<T> criteriaQuery, Predicate[] predicates) {
		TypedQuery<T> pgQuery = em.createQuery(criteriaQuery);
		if (queryParam.getPageNumber() > 1) {
			pgQuery.setFirstResult((queryParam.getPageNumber() - 1) * queryParam.getPageSize());
		}
		pgQuery.setMaxResults(queryParam.getPageSize());
		List<T> list = executeQuery(pgQuery, true, false);// pgQuery.getResultList();
		int totalResults = list.size();
		// 如果当前记录数大于一页，并且入参中的记录总数为空，那么就要到数据库中求总数
		if (totalResults >= queryParam.getPageSize()) {
			if (queryParam.getRecordCount() != null) {
				totalResults = queryParam.getRecordCount();
			} else {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Long> cqCount = cb.createQuery(Long.class);
				Expression<T> root = cqCount.from(resultClazz);
				cqCount.select(cb.count(root));
				cqCount.where(predicates);
				totalResults = em.createQuery(cqCount).getSingleResult().intValue();
			}
		}
		PagedQueryResult<T> result = new PagedQueryResult<T>(totalResults, queryParam.getPageSize(), queryParam.getPageNumber());
		result.setDatas(list);
		return result;
	}

}
