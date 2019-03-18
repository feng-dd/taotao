package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * 内容管理service
 */
public interface ContentService {

	/**
	 * 获取内容列表
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGridResult getContentList(Long categoryId,int page,int rows);

	/**
	 * 新增内容
	 * @param content
	 * @return
	 */
	TaotaoResult addContent (TbContent content);

	/**
	 * 修改内容
	 */
	TaotaoResult updateContent(TbContent content);

	/**
	 * TbContent根据id查询content
	 * @param id
	 * @return
	 */
	TaotaoResult getContent(long id);

	/**
	 * 删除选中的所有行
	 * @param ids
	 * @return
	 */
	TaotaoResult deleteContent(String ids);

	/**
	 * 查询大广告位的数据
	 * @param cid
	 * @return
	 */
	List<TbContent> getContentListByCid(long cid);
}
