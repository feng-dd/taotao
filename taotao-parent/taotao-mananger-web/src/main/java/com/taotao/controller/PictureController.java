package com.taotao.controller;

import com.taotao.common.utils.JsonUtil;
import com.taotao.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;

	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		Map result  = new HashMap<>();
		try {
			//1.接收上传的文件
			String orignalName = uploadFile.getOriginalFilename();

			//2.获取扩展名
			String extName = orignalName.substring(orignalName.lastIndexOf(".")+1);

			//3.上传到图片服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/client.conf");
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			url = IMAGE_SERVER_URL + url;
			result.put("error", 0);
			result.put("url", url);
			//4.返回Json格式的字符串
//			return JSON.toJSONString(result);
			return JsonUtil.objectToJson(result);
		}  catch (Exception e) {
			e.printStackTrace();
			result.put("error", 0);
			result.put("message", "上传图片失败！");
//			return JSON.toJSONString(result);
			return JsonUtil.objectToJson(result);
		}
	}
}
