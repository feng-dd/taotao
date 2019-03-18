package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;

	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(Long categoryId,int rows,int page){
		EasyUIDataGridResult result= contentService.getContentList(categoryId,page,rows);
		return result;
	}

	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult addContent(TbContent content){
		TaotaoResult result = contentService.addContent(content);
		return result;
	}

	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public TaotaoResult updateContent(TbContent content){
		TaotaoResult result = contentService.updateContent(content);
		return result;
	}

	@RequestMapping("/content/getContent")
	@ResponseBody
	public TaotaoResult getContent(long id){
		TaotaoResult result = contentService.getContent(id);
		return result;
	}

	@RequestMapping("/content/delete")
	@ResponseBody
	public TaotaoResult deleteContent(String ids){
		TaotaoResult result = contentService.deleteContent(ids);
		return result;
	}
}
