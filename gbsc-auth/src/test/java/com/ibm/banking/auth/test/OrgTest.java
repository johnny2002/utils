package com.ibm.banking.auth.test;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(locations={"classpath:conf/banking-auth-dao.xml","classpath:conf/banking-auth-res-test.xml"})
@TransactionConfiguration( defaultRollback=false)
public class OrgTest extends AbstractTransactionalTestNGSpringContextTests{
	@Autowired
    protected SessionFactory sessionFactory;
	
	

	public void testGetNodes(){
		
	}
}
