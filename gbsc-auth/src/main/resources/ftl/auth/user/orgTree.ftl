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
	function openOrgRoles(orgCode){
		var orUrl = "orgs/"+orgCode + "/roles.htm";
		openContentWindow(orUrl, "orgRoles");
		return false;
	
	}
	function orgClick(event, treeId, treeNode, clickFlag){
		openOrgRoles(treeNode.id);
	}
	
	var treeNodes = ${orgTreeJson};
	function setTreeNodeAttrs(treeNodes){
		for (i = 0; i < treeNodes.length; i++){
			treeNodes[i].onclick = "orgs/" + treeNodes[i].id + "/roles.htm";
		}
	}
	var settings = {
		callback: {
			onClick: orgClick
		}
	};
	
	$(document).ready(function(){
		//setTreeNodeAttrs(treeNodes);
		$.fn.zTree.init($("#treeOrg"), settings, treeNodes);
	});
</script>
</head>
<body>

演示：${base}
<div class="zTreeDemoBackground left">
		<ul id="treeOrg" class="ztree"></ul>
</div>

<div id="orgRoles">
点击机构节点，查看和修改机构的角色。
</div>

</body>
</html>