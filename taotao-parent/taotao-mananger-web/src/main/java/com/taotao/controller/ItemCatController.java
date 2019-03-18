package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;

	/**
	 *
	 * @param parentId    通过父节点ID查询树结构
	 *                   懒加载，最开始只显示一级目录，
	 *                   点击一级目录，会查询下级目录信息
	 * @return
	 */
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(value="id", defaultValue="0") Long parentId ){
		/* 页面中传递过来的属性名是id，与prentId名称不一致
		 * 用 @RequestParam 关联起来
		 * 刚开始传入的id值为空，因此给一个默认值0
		 */
		//1.接收页面传ID，调用service进行业务处理
		List<EasyUITreeNode> itemCatList = itemCatService.getItemCatList(parentId);
		//2.将查询的商品分类结果返回给页面
		return itemCatList;
	}
}


