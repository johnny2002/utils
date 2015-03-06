<#assign sf = JspTaglibs["http://www.springframework.org/tags/form"] >
<#assign c = JspTaglibs["http://java.sun.com/jsp/jstl/core"] >
<#assign fmt = JspTaglibs["http://java.sun.com/jsp/jstl/fmt"] >
<#assign disp = JspTaglibs["http://displaytag.sf.net"] >
<#import "/macros/spring.ftl" as spring>
<script type="text/javascript">
	
	var treeNodesData = ${funcTreeNodes};
	var treeObj;
	var settings = {
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};	
	$(document).ready(function(){
		treeObj = $.fn.zTree.init($("#treeRoleFunc"), settings, treeNodesData);
	});
	function saveRoleFunc(theForm){
		var nodes = treeObj.getCheckedNodes();
		//var data = $(theForm).serialize();
		//data.funcs = new Array(nodes.length);
		//alert(checkedFuncIds.value);
		for (i = 0; i < nodes.length; i++){
			var func = document.getElementById("funcs").cloneNode();
			func.id += func.id + "_" + i;
			func.value=nodes[i].id;
			$(theForm).append(func);
		}
		submitForm(theForm, 'PUT');
	}
</script>
<div>角色[<b>${role.name}</b>]的功能权限：</div>
<div class="zTreeDemoBackground left">
		<ul id="treeRoleFunc" class="ztree">角色功能树.</ul>
</div>
	<input id="funcs" type="hidden" name="funcs" value=""/>
<form action="roles/${role.code}/functions.htm" >
	<input type="button" class="button" value="保存" onclick="saveRoleFunc(this.form)"/>
</form>