package com.taotao.sso.controller;

import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkUserData(@PathVariable String param,@PathVariable Integer type){
		TaotaoResult result = userService.checkUserData(param,type);
		return result;
	}

	@RequestMapping(value = "/user/register",method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user){
		TaotaoResult result = userService.register(user);
		return result;
	}

	@RequestMapping(value = "/user/login",method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username, String password, HttpServletRequest request, HttpServletResponse response){
		TaotaoResult result =userService.login(username,password);
		//判断result中有没有数据，也就是账号密码是否正确
		if(result.getData()!= null){
			//把token写入到cookie
			CookieUtils.setCookie(request,response,TOKEN_KEY,result.getData().toString());
			return result;
		}
		return result;
	}

	@RequestMapping(value = "/user/token/{token}",method = RequestMethod.GET)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		TaotaoResult result = userService.getUserByToken(token);
		//如果传递的参数中有callback参数，证明跨域
		if(StringUtils.isNotBlank(callback)){
			//处理返回结果为js函数
			return callback+"("+JSON.toJSONString(result) +")";
		}
		//不是跨域，转为json格式
		return JSON.toJSONString(result);
	}

	@RequestMapping(value = "/user/logout/{token}",method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult logout(@PathVariable String token){
		TaotaoResult result = userService.logout(token);
		return result;
	}


}
