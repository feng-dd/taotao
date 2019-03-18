package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {

	/**
	 * 跳转主页
	 * @return
	 */
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}

	/**
	 * 跳转节点对应的页面
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
}
