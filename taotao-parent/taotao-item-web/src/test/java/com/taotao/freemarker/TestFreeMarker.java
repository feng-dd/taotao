package com.taotao.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class TestFreeMarker {

	@Test
	public void testFreeMarker()throws Exception{
		//1.创建一个模板文件 hello.ftl
		//2.创建Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3.设置模板文件路径
		configuration.setDirectoryForTemplateLoading(new File("G:/Java/IdeaProjects/taotao/taotao-parent/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
		//4.设置模板的字符集，utf-8
		configuration.setDefaultEncoding("utf-8");
		//5.加载模板文件，需指定文件名
		Template template = configuration.getTemplate("hello.ftl");
		//6.创建数据集，可以是pojo、map，推荐map
		Map data = new HashMap();
		data.put("hello","hello freemarker!");
		//7.创建字符输出流Writer对象，指定输出文件的路径和名称	路径中的文件夹需提前创建好
		Writer out = new FileWriter(new File("G:/Java/freemarker/out/hello.html"));
		//8.使用模板对象的process方法输出文件
		template.process(data,out);
		//9.关闭流
		out.close();
	}

	@Test
	public void testFreeMarker1() throws Exception{
		//1.创建一个模板文件(就是我们刚创建的hello.ftl模板)
		//2.创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3.设置模板所在的路径
		configuration.setDirectoryForTemplateLoading(new File("G:/Java/IdeaProjects/taotao/taotao-parent/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
		//4.设置模板的字符集，一般为utf-8
		configuration.setDefaultEncoding("utf-8");
		//5.使用Configuration对象加载一个模板文件，需要指定模板文件的文件名。
		Template template = configuration.getTemplate("student.ftl");
		//6.创建一个数据集，可以是pojo也可以是map，推荐使用map
		Map data = new HashMap<>();
		data.put("hello", "hello freemarker");
		Student student = new Student(1, "张三", 20, "北京市海淀区西二旗");
		data.put("student", student);
		//可以添加列表，模板需要改动
		List<Student> stuList = new ArrayList<>();
		stuList.add(new Student(1, "小米", 20, "北京市昌平区小米科技有限公司"));
		stuList.add(new Student(2, "小米2", 21, "北京市昌平区小米科技有限公司"));
		stuList.add(new Student(3, "小米3", 22, "北京市昌平区小米科技有限公司"));
		stuList.add(new Student(4, "小米4", 23, "北京市昌平区小米科技有限公司"));
		stuList.add(new Student(5, "小米5", 24, "北京市昌平区小米科技有限公司"));
		stuList.add(new Student(6, "小米6", 25, "北京市昌平区小米科技有限公司"));
		data.put("stuList", stuList);
		//日期类型
		data.put("date",new Date());
		//让var有值
		data.put("var","123");
		//7.创建一个Writer对象，指定输出文件的路径及文件名
		Writer out = new FileWriter(new File("G:/Java/freemarker/out/student.html"));
		//8.使用模板对象的process方法输出文件
		template.process(data, out);
		//9.关闭流
		out.close();
	}

}
