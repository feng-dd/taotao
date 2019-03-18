package com.taotao.portal.controller;

import com.alibaba.fastjson.JSON;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

	@Value("${AD1_CATEGORY_ID}")
	private Long AD1_CATEGORY_ID;
	@Value("${AD1_WIDTH}")
	private Integer AD1_WIDTH;
	@Value("${AD1_WIDTH_B}")
	private Integer AD1_WIDTH_B;
	@Value("${AD1_HEIGHT}")
	private Integer AD1_HEIGHT;
	@Value("${AD1_HEIGHT_B}")
	private Integer AD1_HEIGHT_B;

	@Autowired
	private ContentService contentService;


	@RequestMapping("/index")
	public String showIndex(Model model){
		//根据categoryId查询内容列表
		List<TbContent> contents = contentService.getContentListByCid(AD1_CATEGORY_ID);
		//将content对象和配置文件中的常量拼装成AD1Node对象
		List<AD1Node> adlist = new ArrayList<>();
		for (TbContent content:contents){
			AD1Node ad = new AD1Node();
			ad.setAlt(content.getTitle());
			ad.setHeight(AD1_HEIGHT);
			ad.setHeightB(AD1_HEIGHT_B);
			ad.setHref(content.getUrl());
			ad.setWidth(AD1_WIDTH);
			ad.setWidthB(AD1_WIDTH_B);
			ad.setSrc(content.getPic());
			ad.setSrcB(content.getPic2());
			adlist.add(ad);
		}
		//用fastjson将AD1Node对象集合转换成Json字符串
		String ad1 = JSON.toJSONString(adlist);
		//视图渲染
		model.addAttribute("ad1",ad1);
		//返回到页面
		return "index";
	}

}
