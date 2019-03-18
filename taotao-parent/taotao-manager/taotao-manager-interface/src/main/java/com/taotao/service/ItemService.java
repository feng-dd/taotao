package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {

	/**
	 * 通过ID查找商品
	 * @param itemId
	 * @return
	 */
	TbItem getItemById(long itemId);

	/**
	 * 获取商品列表
	 * @param page	页号
	 * @param rows	行数
	 * @return
	 */
	EasyUIDataGridResult getItemList(int page,int rows);

	/**
	 * 新增商品
	 * @param item	商品
	 * @param desc	描述
	 * @return
	 * @throws Exception
	 */
	TaotaoResult createItem(TbItem item,String desc) throws Exception;

	/**
	 * 通过ID查询商品描述
	 * @param itemId
	 * @return
	 */
	TbItemDesc getItemDescById(long itemId);
}
