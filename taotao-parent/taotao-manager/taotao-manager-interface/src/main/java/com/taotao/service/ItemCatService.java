package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * 商品分类service
 */
public interface ItemCatService {

	/**
	 * 查询商品分类树节点列表
	 * @param parentId	通过父节点ID查询树结构
	 *                  懒加载，最开始只显示一级目录，
	 *                  点击一级目录，会查询下级目录信息
	 * @return	树列表
	 */
	List<EasyUITreeNode> getItemCatList(long parentId);
}
