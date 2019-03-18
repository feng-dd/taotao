package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

/**
 * 搜索服务
 */
public interface SearchService {

	SearchResult search(String queryString,int page,int rows) throws Exception;
}
