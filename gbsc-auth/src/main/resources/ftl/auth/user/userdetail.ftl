<#assign sf = JspTaglibs["http://www.springframework.org/tags/form"] >
<#assign c = JspTaglibs["http://java.sun.com/jsp/jstl/core"] >
<#assign fmt = JspTaglibs["http://java.sun.com/jsp/jstl/fmt"] >
<#assign disp = JspTaglibs["http://displaytag.sf.net"] >
<#import "/macros/spring.ftl" as spring>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<#include RscPage>
<@fmt.setBundle basename="i18n/auth-messages" />
<script type="text/javascript">
	var sbMethod = window.location.href.indexOf("/new") > 0 ? "POST" : "PUT";
	//submitForm(theForm, sbMethod);
</script>

</head>
<body>
	<form method="post" id="userform">
<#--input type="hidden" name="_method" value="put"/ 
	<@spring.bind "theUser.*"/>
	<@spring.showErrors />-->
		<table class="resultTable">
			<tr>
				<td>员工编码</td>
				<td>
				<#if _mode == "new">
				<@spring.formInput path="theUser.code" errors=true/>
				<#else>
				<@spring.formHiddenInput path="theUser.code" />${theUser.code}
				</#if>
				</td>
				<td>姓名:</td>
				<td><@spring.formInput path="theUser.fullName" errors=true/></td>
			</tr>
			<tr>
				<td>分机号码</td>
				<td><@spring.formInput path="theUser.extNumber" errors=true/> </td>
				<td>手提电话</td>
				<td><@spring.formInput path="theUser.mobileNumber" errors=true/> </td>
			</tr>
			<tr>
				<td>生日</td>
				<td><@spring.formInput path="theUser.birthDate" errors=true/> 
			 <#--if theUser.birthDate??>${theUser.birthDate?string("yyyy-MM-dd HH:mm:ss")}</#if--> 		
				</td>
				<td>状态</td>
				<td><@spring.formRadioButtons path="theUser.status" options=UserStates separator="" errors=true/>
				</td>
			</tr>
			<tr>
				<td>邮件地址</td>
				<td><@spring.formInput path="theUser.email" errors=true/></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
<br/>
用户角色：<br/>		
<@spring.formCheckboxes path="theUser.roles" options=roles separator="" errors=true/>
		<hr/>
		<input type="button" value="保存" onclick="submitForm(this.form, sbMethod)"/>
		<input type="button" value="Cancel" onclick="location.href='../usersearch/search.htm'"/>
	</form>
	
	

</body>
</html>

	
