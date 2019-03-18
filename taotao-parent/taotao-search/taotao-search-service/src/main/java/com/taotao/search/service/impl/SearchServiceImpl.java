package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;

	@Override
	public SearchResult search(String queryString, int page, int rows) throws Exception{
		//创建solr查询对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery(queryString);
		//设置分页信息
		if(page<1) page=1;
		query.setStart((page-1)*rows);
		if(rows<1) rows=1;
		query.setRows(rows);
		//设置默认搜索域
		query.set("df","item_title");
		//设置高亮显示
		query.setHighlight(true);//开启高亮显示
		query.addHighlightField("item_title");//设置高亮字段
		query.setHighlightSimplePre("<em>");//前加
		query.setHighlightSimplePost("</em>");//后加
		//调用dao层执行查询
		SearchResult searchResult = searchDao.search(query);
		//处理分页
		long totalNumber = searchResult.getTotalNumber();//总数
		long pages = totalNumber / rows;//总页数
		if(totalNumber % rows > 0){//余数>0
			pages++;//页数+1
		}
		//设置总页数
		searchResult.setTotalPages(pages);
		return searchResult;
	}
}
