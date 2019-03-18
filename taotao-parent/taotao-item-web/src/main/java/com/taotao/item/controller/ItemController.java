package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	public String showItem(@PathVariable Long itemId, Model model){

		//1.获取商品的基本信息
		TbItem tbItem = itemService.getItemById(itemId);
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
		//2.将item对象转换成pojo对象，因为images属性变动
		Item item = new Item(tbItem);
		//3.向页面参数赋值
		model.addAttribute("item",item);
		model.addAttribute("itemdesc",tbItemDesc);
		//4.返回逻辑视图
		return "item";
	}

}
