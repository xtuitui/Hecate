package org.xiaotuitui.hecate.interfaces;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xiaotuitui.framework.interfaces.BaseCtrl;

@Controller
@RequestMapping(value = "/user")
public class UserCtrl extends BaseCtrl{
	
	@RequestMapping(value = "/toUser", method = RequestMethod.GET)
	public String toUser(){
		return "/user";
	}

}