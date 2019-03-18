package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		//创建一个查询类
		TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
		//设置查询条件
		TbContentCategoryExample.Criteria criteria = contentCategoryExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		criteria.andStatusEqualTo(1);//status 1:正常 2:删除
		//查询
		List<TbContentCategory> categoryList = contentCategoryMapper.selectByExample(contentCategoryExample);
		//将categoryList转换为List<EasyUITreeNode>
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for(TbContentCategory contentCategory : categoryList){
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(contentCategory.getId());
			easyUITreeNode.setText(contentCategory.getName());
			easyUITreeNode.setState(contentCategory.getIsParent() ? "closed":"open");
			resultList.add(easyUITreeNode);
		}
		return resultList;
	}

	@Override
	public TaotaoResult addContentCategory(long parentId, String name) {
		//1.因为是新增一个TbContentCategory对象，所以先创建对象
		TbContentCategory contentCategory = new TbContentCategory();
		//2.将新增对象的属性赋值
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setStatus(1);//状态：1:正常 2:删除 新增肯定是正常的
		contentCategory.setIsParent(false);//是否父节点 1:是 2:否 新增肯定不是父节点
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategory.setSortOrder(1);//数据库默认为1
		//3.插入节点
		contentCategoryMapper.insert(contentCategory);
		//4.增加一个节点需要判断他的父节点是不是叶子节点（parenr==0）
		//若是，则改变状态(parent==1)
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		return TaotaoResult.ok(contentCategory);// contentCategory对象里面是包含id属性的
	}

	@Override
	public TaotaoResult updateContentCategory(long id, String name) {
		//1.通过id查询修改的对象
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		//2.将对象的修改项重新赋值
		contentCategory.setName(name);
		//3.完成修改
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		//4.返回结果
		return TaotaoResult.ok();
	}

	/**
	 * 查询要删除节点的所有子节点
	 * @param parentId
	 * @return
	 */
	private List<TbContentCategory> getContentCategoryListByParentId(long parentId){
		//分析：单表查询、有查询条件,切查询结果是多个
		//1.创建查询对象
		TbContentCategoryExample example = new TbContentCategoryExample();
		//2.创建查询条件对象
		TbContentCategoryExample.Criteria criteria = example.createCriteria();
		//3.设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//4.查询
		List<TbContentCategory> list= contentCategoryMapper.selectByExample(example);
		return list;
	}

	/**
	 * 递归删除节点(逻辑删除)
	 * @param parentId
	 */
	private void deleteNode(long parentId){
		List<TbContentCategory> list = getContentCategoryListByParentId(parentId);
		for(TbContentCategory contentCategory : list){
			//逻辑删除
			contentCategory.setStatus(2);
			contentCategoryMapper.updateByPrimaryKey(contentCategory);
			//判断该节点是否为父节点
			if(contentCategory.getIsParent()){
				//是，查询该节点的id，并再次调用删除子节点
				deleteNode(contentCategory.getId());
			}
		}
	}


	/**
	 * 删除节点
	 * @param id	节点ID
	 * @return
	 */
	@Override
	public TaotaoResult deleteContentCategory(long id) {
		//1.通过id查询逻辑删除的对象
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		//2.逻辑删除
		contentCategory.setStatus(2);
		//3.修改
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		//4.判断当前删除的节点是不是父节点（还有没有子节点）
		if(contentCategory.getIsParent()){
			//逻辑删除所有子节点
			deleteNode(contentCategory.getId());
		}
		//5.判断该节点的父节点还有没有其他子节点
		//无：需要将父节点的isParent属性改为false   有：不需要改动
		//通过该节点的父节点Id 获取父节点
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
		//查询父节点的子节点
		List<TbContentCategory> list = getContentCategoryListByParentId(parent.getId());
		//判断是否有子节点,子节点的status属性是2，就是逻辑删除了，就没有
		Boolean flag = false;
		for (TbContentCategory category : list){
			//有子节点
			if(category.getStatus() == 1){
				flag = true;
				break;
			}
		}

			//若没有其他子节点了，则将isParent属性改为false
			if(!flag){
				parent.setIsParent(false);
				contentCategoryMapper.updateByPrimaryKey(parent);
			}

		//返回节点
		return TaotaoResult.ok();
	}
}
