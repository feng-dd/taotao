package com.taotao.solrj;

public class TestSolrJ {

//	/**
//	 * 新增一条记录
//	 * @throws Exception
//	 */
//	@Test
//	public void testAddDocument() throws Exception{
//
//		//创建一个SolrServer对象，创建一个HttpSolrServer对象，需要指定solr服务的url
//		//如果有多个collection则需要指定要操作哪个collection，如果只有一个，可以不指定
//		SolrServer solrServer = new HttpSolrServer("http://192.168.156.71:8180/solr/collection1");
//		//创建一个文档对象SolrInputDocument
//		SolrInputDocument document = new SolrInputDocument();
//		//向文档中添加域，必须有id域，域的名称必须在schema.xml中定义
//		document.addField("id", "444");
//		document.addField("item_title", "夏普空调");
//		document.addField("item_sell_point", "送电暖宝一个哦");
//		document.addField("item_price", 20000);
//		document.addField("item_image", "http://www.123.jpg");
//		document.addField("item_category_name", "电器");
//		document.addField("item_desc", "这是一款最新的空调，质量好，值得信赖！！");
//		//将document添加到索引库
//		solrServer.add(document);
//		//提交
//		solrServer.commit();
//	}
//
//	@Test
//	public void testAdd() throws Exception{
//		String zkHost = "192.168.156.71:2181,192.168.156.71:2182,192.168.156.71:2183";
//		CloudSolrServer solrServer = new CloudSolrServer(zkHost);
//		solrServer.setDefaultCollection("collection1");
//		SolrInputDocument document = new SolrInputDocument();
//		document.addField("id", "1111");
//		document.addField("item_title", "海尔空调");
//		document.addField("item_sell_point", "送电暖宝一个哦");
//		document.addField("item_price", 10000);
//		document.addField("item_image", "http://www.123.jpg");
//		document.addField("item_category_name", "电器");
//		document.addField("item_desc", "这是一款最新的空调，质量好，值得信赖！！");
//		//将document添加到索引库
//		solrServer.add(document);
//		//提交
//		solrServer.commit();
//	}
//
//	/**
//	 * 通过ID删除记录
//	 * @throws Exception
//	 */
//	@Test
//	public void testDeleteDocument() throws Exception{
//		//创建一个SolrServer对象，创建一个HttpSolrServer对象，需要指定solr服务的url
//		SolrServer solrServer = new HttpSolrServer("http://192.168.156.71:8180/solr/collection1");
//		//通过id来删除文档
//		solrServer.deleteById("1111");
//		//提交
//		solrServer.commit();
//	}
//
//	/**
//	 * 通过其他条件删除记录
//	 * @throws Exception
//	 */
//	@Test
//	public void deleteDocumentByQuery() throws Exception{
//		//创建一个SolrServer对象，创建一个HttpSolrServer对象，需要指定solr服务的url
//		SolrServer solrServer = new HttpSolrServer("http://192.168.156.71:8180/solr/collection1");
//		//通过价格来删除文档
//		solrServer.deleteByQuery("item_price:20000");
//		//提交
//		solrServer.commit();
//	}
//
//	/**
//	 * 条件查询记录
//	 * @throws Exception
//	 */
//	@Test
//	public void queryDocument() throws Exception{
//		//创建一个SolrServer对象，创建一个HttpSolrServer对象，需要指定solr服务的url
//		SolrServer solrServer = new HttpSolrServer("http://192.168.156.71:8180/solr/collection1");
//		//通过id来删除文档
//		SolrQuery query = new SolrQuery();
//		query.setQuery("id:222");
//		QueryResponse response = solrServer.query(query);
//		SolrDocumentList list = response.getResults();
//		for(SolrDocument document : list){
//			String id = document.getFieldValue("id").toString();
//			String title = document.getFieldValue("item_title").toString();
//			System.out.println(id);
//			System.out.println(title);
//		}
//	}
//

//	@Test
//	public void queryDocument() throws Exception{
//		//创建一个SolrServer对象，创建一个HttpSolrServer对象，需要指定solr服务的url
//		SolrServer solrServer = new HttpSolrServer("http://192.168.156.71:8180/solr/collection1");
//		//创建一个SolrQuery对象
//		SolrQuery query = new SolrQuery();
//		//设置查询条件、过滤条件、分页条件、排序条件、高亮
//		//query.set("q", "*:*");
//		query.setQuery("手机");
//		//分页条件
//		query.setStart(0);
//		query.setRows(3);
//		//设置默认搜索域
//		query.set("df", "item_keywords");
//		//设置高亮
//		query.setHighlight(true);
//		//高亮显示的域
//		query.addHighlightField("item_title");
//		query.setHighlightSimplePre("<em>");
//		query.setHighlightSimplePost("</em>");
//		//执行查询，得到一个Response对象
//		QueryResponse response = solrServer.query(query);
//		//取查询结果	SolrDocumentList:solr文档集
//		SolrDocumentList solrDocumentList = response.getResults();
//		//取查询结果总记录数
//		System.out.println("查询结果总记录数："+solrDocumentList.getNumFound());
//		for(SolrDocument document : solrDocumentList){
//			//获取solr上有存储的字段,就是我们在schema.xml文件里配置的field标签的stored属性,=true就会存储在solr服务器上
//			System.out.println(document.getFieldValue("id"));
//			//取高亮显示
//			Map<String,Map<String,List<String>>> highlighting = response.getHighlighting();
//			//通过获取的id，去数据库获取具体的信息，并设置高亮
//			List<String> list = highlighting.get(document.getFieldValue("id")).get("item_title");
//			String itemTitle = "";
//			if(list != null && list.size() > 0){
//				itemTitle = list.get(0);
//			}else {
//				itemTitle = (String)document.get("item_title");
//			}
//			System.out.println(itemTitle);
//			System.out.println(document.get("item_sell_point"));
//			System.out.println(document.get("item_price"));
//			System.out.println(document.get("item_image"));
//			System.out.println(document.get("item_category_name"));
//			System.out.println("===============================================");
//		}
//	}
//
//
}
