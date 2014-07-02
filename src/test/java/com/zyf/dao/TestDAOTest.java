package com.zyf.dao;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractBaseTest;

import com.zyf.po.TestPO;

public class TestDAOTest extends AbstractBaseTest {
	@Autowired
	private TestDAO testDAO;

	@Test
	public void testInsert() {
		TestPO test = new TestPO();
		test.setRemark("zyf高leggao");
		Object o = testDAO.insert(test);
		Assert.assertTrue(o != null);
	}
}
