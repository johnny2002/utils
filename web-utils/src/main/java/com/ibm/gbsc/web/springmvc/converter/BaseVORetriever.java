/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.web.springmvc.converter;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.gbsc.common.vo.BaseVO;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
@Component
@Transactional(readOnly = true)
public class BaseVORetriever {
	@PersistenceContext
	EntityManager em;

	/**
	 * @param <T>
	 *            t.
	 * @param clazz
	 *            clazz.
	 * @param id
	 *            id.
	 * @return t.
	 */
	public <T extends BaseVO> T getEntityById(Class<T> clazz, Serializable id) {
		return em.getReference(clazz, id);
	}

}
