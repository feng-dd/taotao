package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

/**
 * 内容分类管理service
 */
public interface ContentCategoryService {

	/**
	 * 获取内容分类列表
	 * @param parentId	父节点ID
	 * @return
	 */
	List<EasyUITreeNode> getContentCategoryList(long parentId);

	/**
	 * 新增内容分类
	 * @param parentId	父节点ID
	 * @param name	内容分类名称=EasyUITreeNode.text
	 * @return
	 */
	TaotaoResult addContentCategory(long parentId,String name);

	/**
	 * 修改内容分类
	 * @param id 	节点ID
	 * @param name	内容分类名称=EasyUITreeNode.text
	 * @return
	 */
	TaotaoResult updateContentCategory(long id,String name);

	/**
	 * 删除内容分类:选择的节点可能不是叶子节点，此时删除该节点要删除所有下级节点。
	 * @param id	节点ID
	 * @return
	 */
	TaotaoResult deleteContentCategory(long id);
}
