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
	function openRoleResource(roleCode){
		var orUrl = "roles/"+roleCode + ".htm";
		openContentWindow(orUrl, "roleResource");
		return false;
	}

	$(document).ready(function(){
		$(".openRoleResource").click(function(){
			openRoleResource($(this).parent().children(".rolecode").text());
		});
	});
</script>
</head>
<body>

<table>
<tr><td>
<@disp.table uid="tRole" list=roles excludedParams="*" export=false class="resultTable" form="searchListForm" >
	<@disp.caption>角色列表</@disp.caption>
	<@disp.column title="角色代码" property="code" class="center openRoleResource rolecode"/>
	<@disp.column title="角色名称" property="name" class="center openRoleResource"/>
</@disp.table>
</td><td>
<div id="roleResource">
点击角色，查看和修改角色相关的资源和功能。
</div>
</td></tr>
</table>
</body>
</html>