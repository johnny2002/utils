<#import "/common/spring.ftl" as spring>
<#assign sf = JspTaglibs["http://www.springframework.org/tags/form"] >
<#assign c =JspTaglibs["http://java.sun.com/jsp/jstl/core"] >
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${resRoot}/js/jquery/jquery${minSuffix}.js"></script>
<script type="text/javascript" src="${resRoot}/js/jquery/jquery${minSuffix}.validate.js"></script>

<style>
.errorMessage{color:red}
</style>

<link href="${resRoot}/css/sn_common0925${minSuffix}.css" rel="stylesheet" type="text/css" />
<style>.errorClass{color:red}</style>
</head>
<body>
<form  action="saveFtl.htm" method="POST" id='form1'>

	<@spring.bind "theUser"/>
	<@spring.showErrors  "<br>"  "errorClass"/>

<@spring.formHiddenInput path="theUser.code" />
<@spring.formHiddenInput path="theUser.displayName" />


		<table>
			<TR>
				<TD>姓名:</TD>
				<TD>${theUser.displayName}</TD>
				<TD></TD>
				<TD></TD>
			</TR>
			<TR>
				<TD>省份:</TD>
				<TD></TD>
				
				<TD>电话:</TD>
				<TD><@spring.formInput path="theUser.email" />
				<@spring.bind "theUser.email"/>
				 <lable class="errorMessage">${spring.status.errorMessage}</lable>  
				 </TD>
			</TR>
			<TR>
				<TD>入职日期:</TD>
				<TD>
					<@spring.formInput path="theUser.lastLoginTime" /></TD>
				<TD>入职日期:</TD>
				<TD>
				 <#if theUser.lastLoginTime??>${theUser.lastLoginTime?string("yyyy-MM-dd HH:mm:ss")}</#if> 		
				</TD>
			</TR>
		</table>
		
		<br/>
		
		<input type="submit"/>
		<input type="button" value=" Cancel " onclick="location.href='search.htm'"/>
		
	
	</form>
	
	

</body>
</html>

	
