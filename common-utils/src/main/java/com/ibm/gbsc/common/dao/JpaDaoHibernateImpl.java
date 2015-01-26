/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.common.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public class JpaDaoHibernateImpl extends JpaDaoImpl {
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ibm.gbsc.common.dao.JpaDao#executeQuery(javax.persistence.TypedQuery,
	 * boolean, boolean)
	 */
	@Override
	public <T> List<T> executeQuery(TypedQuery<T> query, boolean readonly, boolean cacheable) {
		if (readonly) {
			query.setHint("org.hibernate.readOnly", true);
		}
		if (cacheable) {
			query.setHint("org.hibernate.cacheable", true);
		}
		return query.getResultList();
	}

	@Override
	public BigDecimal getNextId(EntityManager em) {
		return (BigDecimal) em.createNativeQuery("select hibernate_sequence.nextval from dual").getSingleResult();

	}
}
