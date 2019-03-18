package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {

	/**
	 * 检查用户数据的合法性
	 * @param data
	 * @param type
	 * @return
	 */
	TaotaoResult checkUserData(String data,int type);

	/**
	 * 注册
	 * @param user
	 * @return
	 */
	TaotaoResult register(TbUser user);

	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	TaotaoResult login(String username,String password);

	/**
	 * 获取用户信息
	 * @param token
	 * @return
	 */
	TaotaoResult getUserByToken(String token);

	/**
	 * 安全退出
	 * @param token
	 * @return
	 */
	TaotaoResult logout(String token);

}
