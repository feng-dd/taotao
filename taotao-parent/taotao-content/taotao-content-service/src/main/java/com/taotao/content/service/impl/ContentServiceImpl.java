package com.taotao.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;

	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;

	@Override
	public EasyUIDataGridResult getContentList(Long categoryId, int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page,rows);
		//创建查询对象
		TbContentExample example = new TbContentExample();
		//创建查询条件对象
		TbContentExample.Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(categoryId);
		//查询
		List<TbContent> list = contentMapper.selectByExample(example);
		//处理结果
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		//返回
		return result;
	}

	@Override
	public TaotaoResult addContent(TbContent content) {
		//更新时间、创建时间需手动修改数据
		content.setUpdated(new Date());
		content.setCreated(new Date());
		//插入
		contentMapper.insert(content);
		//同步缓存，由于首页大广告位的分类ID为89，content.getCategoryId()得到的便是89
		jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
		//返回结果
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult updateContent(TbContent content) {
		//更新时间需手动修改数据
		content.setUpdated(new Date());
		//更新
		contentMapper.updateByPrimaryKeyWithBLOBs(content);
		//同步缓存，因为数据改变了，要将之前存储在redis的cid=89的缓存删掉
		jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
		//返回结果
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult getContent(long id) {
		//根据id查询content
		TbContent content = contentMapper.selectByPrimaryKey(id);
		//返回结果
		return TaotaoResult.ok(content);
	}

	@Override
	public TaotaoResult deleteContent(String ids) {
		//将ids字符串转化为字符串数组
		String[] idList = ids.split(",");
		//遍历删除
		for(String id : idList){
			//删除,将id转换成long型
			contentMapper.deleteByPrimaryKey(Long.valueOf(id));
			//同步缓存，因为数据改变了，要将之前存储在redis的id的缓存删掉
//			jedisClient.hdel(INDEX_CONTENT, id);
		}
		//返回结果
		return TaotaoResult.ok();
	}

	@Override
	public List<TbContent> getContentListByCid(long cid) {
		//首先查询缓存，如果缓存中存在的话，就直接将结果返回给前台展示，查询缓存不能影响业务流程
		try {
			//查询redis:INDEX_CONTENT类中的cid的值，查出来是key-value形式的字符串
			String json = jedisClient.hget(INDEX_CONTENT, cid+"");
			//如果从缓存中查到了结果	isNotBlank(str) 判断str是否为空
			if(StringUtils.isNotBlank(json)){
				//将json串转化为List<TbContent>	JSON.parseArray(str, AA.class)将str字符串转换成AA对象集合
				List<TbContent> list = JSON.parseArray(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//1.创建查询对象
		TbContentExample example = new TbContentExample();
		//2.创建查询条件对象
		TbContentExample.Criteria criteria = example.createCriteria();
		//3.添加查询条件
		criteria.andCategoryIdEqualTo(cid);
		//4.查询
		List<TbContent> list = contentMapper.selectByExample(example);
		//添加缓存，不能影响业务流程
		try {
			//将查询结果转换成字符串
			String json = JSON.toJSONString(list);
			//使用hset方式存放到redis缓存中
			jedisClient.hset(INDEX_CONTENT, cid+"", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回结果
		return list;
	}
}
