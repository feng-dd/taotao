package com.taotao.search.mapper;

import com.taotao.common.pojo.SearchItem;

import java.util.List;

public interface SearchItemMapper {

	/**
	 * 获取要导入索引库的数据
	 * @return
	 */
	List<SearchItem> getSearchItemList();

	/**
	 * 通过商品ID查询商品详情
	 * @return
	 */
	SearchItem getItemById(long itemId);
}
