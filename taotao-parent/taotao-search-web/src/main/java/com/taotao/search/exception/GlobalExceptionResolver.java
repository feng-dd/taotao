package com.taotao.search.exception;

import com.taotao.search.utils.SendMail;
import com.taotao.search.utils.StackTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理类
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
	//获取logger
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest rquest, HttpServletResponse response,
										 Object handler,Exception e) {
		logger.info("进入全局异常处理器。。。");
		logger.debug("测试handler的类型："+handler.getClass());
		//控制台打印异常
		e.printStackTrace();
		//向日志文件中写入异常
		logger.error("系统发生异常", e);
		//发邮件（采用jmail客户端进行发送）
		Runnable myRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					SendMail.sendEmail("搜索系统出现异常", StackTrace.getStackTrace(e));
				}catch (Exception e1){
					e1.printStackTrace();
				}
			}
		};
		Thread thread = new Thread(myRunnable);
		thread.start();
		//发短信
		//展示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "当前网络出现故障，请稍后重试！");
		//返回逻辑视图，这样回去访问error目录下的error.jsp
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}



}
