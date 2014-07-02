package test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;



/**
 * 开放平台接口测试基类
 * 
 * @author coldwater
 * 
 */
@RunWith(NewSpringJunitClassRunner.class)
@ContextConfiguration(locations = "/spring/spring-app.xml")
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public abstract class AbstractBaseTest {
	protected Log log = LogFactory.getLog(getClass());
}
