package com.taotao.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	/**
	 *
	 * @param parentId	通过父节点ID查询树结构
	 *                  懒加载，最开始只显示一级目录，
	 *                  点击一级目录，会查询下级目录信息
	 * @return
	 */
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		//分析：单表查询、有查询条件,切查询结果是多个
		//1.创建单表查询条件对象
		TbItemCatExample example = new TbItemCatExample();
		//2.设置条件对象
		TbItemCatExample.Criteria criteria= example.createCriteria();
		//3.设置父节点Id查询条件
		criteria.andParentIdEqualTo(parentId);
		//4.执行查询,查询结果是itemCat对象
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//5.将查询结果转换成EasyUITreeList集合，并返回
		List<EasyUITreeNode> easyUITreeNodes = new ArrayList<>();
		//使用foreach循环将itemCat对象的属性值赋给EasyUITreeNode对象的属性
		for(TbItemCat itemCat:list){
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(itemCat.getId());
			easyUITreeNode.setText(itemCat.getName());
			easyUITreeNode.setState(itemCat.getIsParent()?"closed":"open");
			easyUITreeNodes.add(easyUITreeNode);
		}
		return easyUITreeNodes;
	}
}
