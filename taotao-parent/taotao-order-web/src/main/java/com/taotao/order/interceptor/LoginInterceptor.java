package com.taotao.order.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	@Value("${SSO_URL}")
	private String SSO_URL;
	@Autowired
	private UserService userService;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 执行的时机，在handler之前先执行此方法，拦截请求让用户登录就在这个方法拦截
		//1.从cookie中取token信息
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		//2.如果取不到token，跳转到sso的登录页面，需要把当前请求的url做为参数传递给sso，sso登录成功之后跳转回请求的页面
		if(StringUtils.isBlank(token)){
			//取当前请求的url
			String requestURL = request.getRequestURL().toString();
			//跳转到登录页面，用redirect比较合适，登录之后还要回到当前页面，因此要在请求url中添加一个回调地址
			response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
			//既然没登录，肯定是要拦截的
			return false;
		}
		//3.取到token，调用sso系统的服务判断用户是否登录，既然要调用SSO服务接口，就要依赖这个taotao-sso-interface
		TaotaoResult result = userService.getUserByToken(token);
		//4.如果用户未登录（有token，但是已经过期，也算是没登录），即没有取到用户信息。跳转到sso的登录页面
		//返回的TaotaoResult如果没有登录的话，状态码是400，如果登录了的话，状态码是200
		if(result.getStatus() != 200){
			//取当前请求的url
			String requestURL = request.getRequestURL().toString();
			//跳转到登录页面，用redirect比较合适，登录之后还要回到当前页面，因此要在请求url中添加一个回调地址
			response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
			//既然没登录，肯定是要拦截的
			return false;
		}
		//5.如果取到用户信息，就放行。
		//返回值true表示放行，返回false表示拦截
		return true;
	}

	/*
	 * 这个方法只会在当前这个Interceptor的preHandle方法返回值为true的时候才会执行。postHandle是进行处理器拦截用的，它的执行时间是在处理器进行处理之
	 * 后，也就是在Controller的方法调用之后执行，但是它会在DispatcherServlet进行视图的渲染之前执行，也就是说在这个方法中你可以对ModelAndView进行操
	 * 作。这个方法的链式结构跟正常访问的方向是相反的，也就是说先声明的Interceptor拦截器该方法反而会后调用，这跟Struts2里面的拦截器的执行过程有点像，
	 * 只是Struts2里面的intercept方法中要手动的调用ActionInvocation的invoke方法，Struts2中调用ActionInvocation的invoke方法就是调用下一个Interceptor
	 * 或者是调用action，然后要在Interceptor之前调用的内容都写在调用invoke之前，要在Interceptor之后调用的内容都写在调用invoke方法之后。
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
						   ModelAndView modelAndView) throws Exception {
		// handler执行之后，modelAndView返回之前，可以对返回值进行处理

	}

	/*
	* 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，也就是DispatcherServlet渲染了视图执行，
	* 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
	*/
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 在ModelAndView返回之后，这时只能做些异常处理了

	}

}
