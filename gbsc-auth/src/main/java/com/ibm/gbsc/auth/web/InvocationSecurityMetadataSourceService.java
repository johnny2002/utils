package com.ibm.gbsc.auth.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.ibm.gbsc.auth.model.Function;
import com.ibm.gbsc.auth.model.Role;
import com.ibm.gbsc.auth.resource.FunctionService;
import com.ibm.gbsc.auth.user.UserService;

/**
 * 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果.
 * 注意，我例子中使用的是AntUrlPathMatcher这个path matcher来检查URL是否与资源定义匹配，
 * 事实上你还要用正则的方式来匹配，或者自己实现一个matcher。 此类在初始化时，应该取到所有资源及其对应角色的定义
 * 说明：对于方法的spring注入，只能在方法和成员变量里注入， 如果一个类要进行实例化的时候，不能注入对象和操作对象，
 * 所以在构造函数里不能进行操作注入的数据。
 */
// @Service("IRMPInvocationSecurityMetadataSourceService")
public class InvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
	/**
	 * Logger for this class.
	 */
	private final Logger logger = LoggerFactory.getLogger(InvocationSecurityMetadataSourceService.class);

	@Inject
	UserService userService;
	@Inject
	FunctionService funcService;

	// private UrlMatcher urlMatcher = new AntUrlPathMatcher();
	private Map<String, String> resourceMap = null; // cache the mapping of
	                                                // function url and id;

	/**
	 *
	 */
	@javax.annotation.PostConstruct
	public void loadResourceDefine() {
		resourceMap = new HashMap<String, String>();
		List<Function> funcs = funcService.getAllFunctions();
		for (Function func : funcs) {

			if (func.getUrl() != null) {
				resourceMap.put(func.getUrl(), func.getCode());
			}
			if (func.getPortletUrl() != null) {
				resourceMap.put(func.getPortletUrl(), func.getCode());
			}
		}

	}

	// According to a URL, Find out permission configuration of this URL.
	/** {@inheritDoc} */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAttributes(Object) - start"); //$NON-NLS-1$
		}
		// guess object is a URL.
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		Iterator<String> ite = resourceMap.keySet().iterator();
		while (ite.hasNext()) {
			String funcURL = ite.next();
			if (logger.isDebugEnabled()) {
				logger.debug("funcURL:" + funcURL + " requestUrl:" + requestUrl);
			}
			if (requestUrl.contains(funcURL)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Mached.");
				}
				Function func = funcService.getFunctionById(resourceMap.get(funcURL), false, null);
				Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
				for (Role role : func.getRoles()) {
					atts.add(new SecurityConfig(role.getAuthority()));
				}
				return atts;
			}
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}
}