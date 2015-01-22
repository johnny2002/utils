<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
<style>
.errorMessage{color:red}
.errorClass{color:red}
</style>
<script type="text/javascript">
	function putForm(theForm){
		$.ajax({
			url: theForm.action,
			type: "PUT",
			data: $(theForm).serialize(),
			success: function( data ) {
				alert(data);
				window.location.reload();
			},
			error: function(request, error) {
				alert(error);
				//window.location.reload();
			}
			
		});
		return false;
	}
</script>

</head>
<body>
<form method="post" id="userform">

 	<#--input type="hidden" name="_method" value="put"/ -->
	<@spring.bind "theUser"/>
	<@spring.showErrors  "<br>"  "errorClass"/>
		<table class="resultTable">
			<tr>
				<td>员工编码</td>
				<td>${theUser.code}</td><@spring.formHiddenInput path="theUser.code" />
				<td>姓名:</td>
				<td><@spring.formInput path="theUser.fullName" /></td>
			</tr>
			<tr>
				<td>分机号码</td>
				<td><@spring.formInput path="theUser.extNumber" /> </td>
				<td>手提电话</td>
				<td><@spring.formInput path="theUser.mobileNumber" /> </td>
			</tr>
			<tr>
				<td>生日</td>
				<td><@spring.formInput path="theUser.birthDate" /> 
			 <#--if theUser.birthDate??>${theUser.birthDate?string("yyyy-MM-dd HH:mm:ss")}</#if--> 		
				</td>
				<td>状态</td>
				<td>
				 <@spring.formInput path="theUser.status" />		
				</td>
			</tr>
			<tr>
				<td>邮件地址</td>
				<td><@spring.formInput path="theUser.email" /> </td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
		
		<br/>
		
		<input type="button" value="保存" onclick="putForm(this.form)"/>
		<input type="button" value="Cancel" onclick="location.href='search.htm'"/>
		
	
	</form>
	
	

</body>
</html>

	
