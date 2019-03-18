package com.taotao.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;


	@Override
	public TaotaoResult checkUserData(String data, int type) {
		//1.设置查询条件
		TbUserExample example = new TbUserExample();
		TbUserExample.Criteria criteria = example.createCriteria();
		//2.添加查询条件
		if(type == 1){//判断用户名是否可用
			criteria.andUsernameEqualTo(data);
		}else if(type == 2){//判断电话是否可用
			criteria.andPhoneEqualTo(data);
		}else if(type == 3){//判断邮箱是否可用
			criteria.andEmailEqualTo(data);
		}else {
			return TaotaoResult.build(400,"所传参数不合法！");
		}
		//3.查询
		List<TbUser> list = userMapper.selectByExample(example);
		//4.处理结果
		if(list != null && list.size() > 0){//结果不为空，不合法
			return TaotaoResult.ok(false);
		}
		return TaotaoResult.ok(true);
	}

	public TaotaoResult register(TbUser user){
		//检查数据有效性
		if(StringUtils.isBlank(user.getUsername())){
			return TaotaoResult.build(400, "用户名不能为空！");
		}
		TaotaoResult taotaoResult = checkUserData(user.getUsername(), 1);
		if(!(Boolean)taotaoResult.getData()){
			return TaotaoResult.build(400, "用户名不能重复！");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return TaotaoResult.build(400, "密码不能为空！");
		}
		if(StringUtils.isNotBlank(user.getPhone())){
			//如果电话不为空，那么接着判断是否重复，电话是不能重复的
			taotaoResult = checkUserData(user.getPhone(), 2);
			if(!(Boolean)taotaoResult.getData()){
				return TaotaoResult.build(400, "电话不能重复！");
			}
		}
		if(StringUtils.isNotBlank(user.getEmail())){
			//如果邮箱不为空，那么接着判断是否重复，邮箱也是不能重复的
			taotaoResult = checkUserData(user.getEmail(), 3);
			if(!(Boolean)taotaoResult.getData()){
				return TaotaoResult.build(400, "邮箱不能重复！");
			}
		}
		//填充属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//密码要进行Md5加密，我们不用添加额外的jar包，只需要使用Spring自带的包就可以了
		String md5Str = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Str);
		//添加
		userMapper.insert(user);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult login(String username, String password) {

		//1.判断用户名和密码是否正确
		TbUserExample example = new TbUserExample();
		TbUserExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() == 0){
			//返回登录失败
			return TaotaoResult.build(400, "用户名或密码不正确！");
		}
		TbUser user = list.get(0);
		//密码要进行md5加密后再校验
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			//返回登录失败
			return TaotaoResult.build(400, "用户名或密码不正确！");
		}
		//2.生成token，使用uuid
		String token = UUID.randomUUID().toString();
		//3.把用户信息保存到redis当中，key就是token，value就是用户信息
		//我们在redis中存放用户信息不要存密码，因为这样太危险了，因此我们先把密码置空
		user.setPassword(null);
		jedisClient.set(USER_SESSION+":"+token, JSON.toJSONString(user));
		//4.设置过期时间
		jedisClient.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
		//5.返回登录成功，要记得带回token信息
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		//1.从缓存中查询key=token的value,加上我们设置的key的前缀USER_SESSION
		String json = jedisClient.get(USER_SESSION+":"+token);
		if(StringUtils.isBlank(json)){//json为空的,返回token过期的提示
			return TaotaoResult.build(400,"token过期");
		}
		/* 	如果我们直接把json返回的话，由于字符串中的" 在redis中是有特殊意义的，因此
			" 会被转义，这不是我们想要的结果，我们想要的结果是不带转义符的字符串，因此
		 	我们需要先把json转换成对象，然后把对象返回。
		 */
		TbUser user = JSON.parseObject(json, TbUser.class);
		//我们每访问一次该token，如果该token还没过期，我们便需要更新token的值，再把token的过期时间恢复到最大值
		jedisClient.expire(USER_SESSION+":"+token,SESSION_EXPIRE);
		//返回结果
		return TaotaoResult.ok(user);
	}

	@Override
	public TaotaoResult logout(String token) {
		jedisClient.expire(USER_SESSION+":"+token, 0);
		return TaotaoResult.ok();
	}

}
