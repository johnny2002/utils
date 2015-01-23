<#import "/macros/spring.ftl" as spring>
<#assign sf = JspTaglibs["http://www.springframework.org/tags/form"] >
<#assign c = JspTaglibs["http://java.sun.com/jsp/jstl/core"] >
<#assign fmt = JspTaglibs["http://java.sun.com/jsp/jstl/fmt"] >
<#assign disp = JspTaglibs["http://displaytag.sf.net"] >
	<@disp.table uid="tUser" list=result.datas excludedParams="*" class="resultTable">
		<@disp.caption>用户列表</@disp.caption>
		<@disp.column title="用户代码" property="code" />
		<@disp.column title="用户名称" property="fullName" />
		<@disp.column title="所属部门"><#list tUser.departments as dept>${dept.name},</#list></@disp.column>
		<@disp.column title="邮件地址" property="email" />
		<@disp.column title="分机号码" property="extNumber" />
		<@disp.column title="手机号码" property="mobileNumber"/>
		<@disp.column title="出生日期" property="birthDate"/>
	</@disp.table>
