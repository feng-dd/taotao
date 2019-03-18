package com.taotao.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HtmlGenController {
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@RequestMapping("genhtml")
	@ResponseBody
	public String genHtml() throws Exception{
		//1.获取configuration对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//2.创建模板
		Template template = configuration.getTemplate("hello.ftl");
		//3.设置数据
		Map data = new HashMap<>();
		data.put("hello", "spring freemarker test");
		//4.创建模板文件
		Writer out = new FileWriter(new File("G:/Java/freemarker/out/test.html"));
		//5.输出文件
		template.process(data, out);
		//6.关闭资源
		out.close();
		//返回结果
		return "OK";
	}
}
