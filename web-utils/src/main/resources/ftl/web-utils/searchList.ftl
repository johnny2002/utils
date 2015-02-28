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
<link rel="stylesheet" type="text/css" href="${webRscRoot}/default/css/displaytag.css"/>
<@fmt.setBundle basename="i18n/auth-messages" />
</head>
<body>

	<form id="searchListForm" name="searchListForm" action="search.htm" method="post">
		<input id="operator" type="hidden" name="operator"/>
		<input id="operator" type="hidden" name="recordCount" value="${result.recordCount}"/>
	    <div id="searchListBlock" class="block_container">  <#-- block_container begin -->
	        <div class="fieldset_border fieldset_bg m-b">   <#-- fieldset_border fieldset_bg begin -->
	           <div class="legend_title">  <#-- legend_title begin -->
	              <a class="container_show">查询条件</a>
	           </div>  <#-- legend_title end -->
	           <div class="fieldset_container"> <#-- fieldset_container begin -->
<#include SearchCriteriaPage>
					<div class="sider">&#160;
					<input id="search" class="btn btn_main" type="submit" value="查询"/>
					<#assign pageSizeOptions = {"10":"10", "20":"20", "100":"100"}>
						每页行数：<@spring.formSingleSelect path="queryObject.pageSize" options=pageSizeOptions/>
					</div>
	           </div> <#-- fieldset_container end -->
	        </div> <#-- fieldset_border fieldset_bg end -->
	        <div class="fieldset_border fieldset_bg m-b"><#-- fieldset_border fieldset_bg begin -->
	           <div class="legend_title"> <#-- legend_title begin -->
	              <a class="container_show">查询结果</a>
	           </div>  <#-- legend_title end -->
	           <div class="fieldset_container"><#-- fieldset_container begin -->
    <#if result ??>
<#include SearchResultPage>
	<#else>
	<div class="">请输入查询条件，并点击查询按钮进行查询</div>
	</#if>
	           </div><#-- fieldset_container end -->
	        </div> <#-- fieldset_border fieldset_bg end -->
	     </div>  <#-- block_container begin -->
	</form>
	<script type="text/javascript">
	$(document).ready(function() {
	    // sub form toggle
	    $("#searchListBlock .linkNewWindow a").attr("taget", "_blank");
	    $("#searchListBlock table.resultTable tbody tr").hover(
			function () {
				$(this).addClass("highlighted");
			}, 
			function () {
				$(this).removeClass("highlighted");
			});
	 });
	</script>



</body>