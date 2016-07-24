package org.xiaotuitui.hecate;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.xiaotuitui.hecate.application.UserSrv;
import org.xiaotuitui.hecate.domain.repository.UserRep;
import org.xiaotuitui.hecate.spring.RootConfiguration;
import org.xiaotuitui.hecate.spring.WebConfiguration;

@RunWith(value = SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {RootConfiguration.class, WebConfiguration.class})
public class Test01 {
	
	@Autowired
	private UserRep userRep;
	
	@Autowired
	private UserSrv userSrv;
	
	@Autowired
	private RequestMappingHandlerMapping handlerMapping;
	
	@Test
	public void test01() throws SQLException{
		System.out.println(userRep);
		System.out.println(userSrv);
	}

}