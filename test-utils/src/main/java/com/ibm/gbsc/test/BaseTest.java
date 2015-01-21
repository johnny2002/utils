/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
@ContextConfiguration(locations = { "classpath:conf/spring/test-dao.xml", "classpath:conf/spring/test-res.xml",
        "classpath:conf/spring/test-biz.xml" })
@TransactionConfiguration(defaultRollback = false)
public class BaseTest extends AbstractTransactionalTestNGSpringContextTests {

}
