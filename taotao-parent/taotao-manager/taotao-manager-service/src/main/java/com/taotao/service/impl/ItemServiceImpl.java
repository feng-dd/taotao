package com.taotao.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtil;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="itemAddTopic")
	private Destination destination ;

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${ITEM_EXPIRE}")
	private Integer ITEM_EXPIRE;

	@Override
	public TbItem getItemById(long itemId) {
		//1.先查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO+":"+itemId+":BASE");
			if(StringUtils.isNotBlank(json)){
				//把json转换成对象
				return JSON.parseObject(json, TbItem.class);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		//把查询结果添加到缓存
		try {
			//把查询结果添加到缓存
			jedisClient.set(ITEM_INFO+":"+itemId+":BASE", JSON.toJSONString(tbItem));
			//设置过期时间，提高缓存的利用率
			jedisClient.expire(ITEM_INFO+":"+itemId+":BASE", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItem;
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//1.先查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO+":"+itemId+":DESC");
			if(StringUtils.isNotBlank(json)){
				//把json转换成对象
				return JSON.parseObject(json, TbItemDesc.class);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		//把查询结果添加到缓存
		try {
			//把查询结果添加到缓存
			jedisClient.set(ITEM_INFO+":"+itemId+":DESC", JSON.toJSONString(tbItemDesc));
			//设置过期时间，提高缓存的利用率
			jedisClient.expire(ITEM_INFO+":"+itemId+":DESC", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//1.创建查询条件的对象
		TbItemExample example = new TbItemExample();

		//2.设置分页信息
		PageHelper.startPage(page,rows);

		//3.接收查询结果
		List<TbItem> list = itemMapper.selectByExample(example);

		//4.取出查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);

		//5.返回结果
		return result;
	}


	@Override
	public TaotaoResult createItem(TbItem item, String desc) throws Exception{
		//1.生成商品ID
		long itemId = IDUtil.genItemId();

		//2.补全商品信息
		item.setId(itemId);
		item.setStatus(((byte)1));//商品状态，1-正常，2-下架，3-删除
		item.setCreated(new Date());
		item.setUpdated(new Date());

		//3.调用Mapper插入到数据库
		itemMapper.insert(item);

		//4.添加商品描述
		insertItemDesc(itemId,desc);
		//发送activemq消息
		jmsTemplate.send(destination,new MessageCreator(){

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId+"");
				return textMessage;
			}
		});
		//5.返回插入状态
		return TaotaoResult.ok();
	}

	/**
	 * 添加商品描述
	 */
	private void insertItemDesc(long itemId,String desc){
		//1.创建一个商品描述表对应的pojo
		TbItemDesc itemDesc = new TbItemDesc();

		//2.补全pojo的属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());

		//3.向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
	}

}
