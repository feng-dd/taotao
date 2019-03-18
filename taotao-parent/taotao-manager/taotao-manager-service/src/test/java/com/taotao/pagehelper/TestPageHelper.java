package com.taotao.pagehelper;

public class TestPageHelper {

//	@Test
//	public void testPageHelper() throws Exception{
//		//1.在Mybatis配置文件中配置分页插件
//		//2.在执行查询之前配置分页条件，使用pagehelper静态方法
//		PageHelper.startPage(1, 10);
//
//		//3.创建一个spring容器
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
//
//		//4.从spring容器中获得Mapper的代理对象
//		TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
//
//		//5.执行查询并分页
//		TbItemExample tbItemExample = new TbItemExample();
//
//		//如果要使用条件查询，则先创建Criteria，然后使用它来拼接查询条件，这里我们不按条件查询，我们查询全部。
//		//Criteria criteria = tbItemExample.createCriteria();
//		//criteria.andIdEqualTo(1L);
//		//pagehelper的Page类是继承ArrayList的，Page里面有分页结果
//		//6.查询
//		List<TbItem> list = tbItemMapper.selectByExample(tbItemExample);
//
//		//7.取分页信息，使用PageInfo对象获取，我们使用PageInfo的目的便是把List强转成Page对象，从而得到分页结果
//		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
//		System.out.println("总记录数："+pageInfo.getTotal());
//		System.out.println("总页数："+pageInfo.getPages());
//		System.out.println("返回的记录数："+pageInfo.getSize());
//	}
}
