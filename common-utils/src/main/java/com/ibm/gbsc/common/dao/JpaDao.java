/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.common.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import com.ibm.gbsc.common.vo.PagedQueryParam;
import com.ibm.gbsc.common.vo.PagedQueryResult;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 * @param <T>
 */
public interface JpaDao {

	/**
	 *
	 * 协助执行分页查询
	 *
	 * @param em
	 * @param resultClazz
	 * @param queryParam
	 * @param criteriaQuery
	 * @param predicates
	 * @return
	 */
	<T> PagedQueryResult<T> executePagedQuery(EntityManager em, Class<T> resultClazz, PagedQueryParam queryParam,
	        CriteriaQuery<T> criteriaQuery, Predicate[] predicates);

	<T> List<T> executeQuery(TypedQuery<T> query, boolean readonly, boolean cacheable);

	/**
	 * 执行查询用只读模式，以提升性能
	 * 
	 * @param query
	 * @return
	 */
	<T> List<T> executeReadonlyQuery(TypedQuery<T> query);

	BigDecimal getNextId(EntityManager em);

}