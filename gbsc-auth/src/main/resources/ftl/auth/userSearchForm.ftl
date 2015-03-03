<#import "/macros/spring.ftl" as spring>
	<div class="fieldset_container">
	<table cellspacing="2" cellpadding="0" class="tabframe1" width="100%">
		<tr>
			<th>用户姓名：</th><td><@spring.formInput path="queryObject.name" attributes="tooltip=\"支持模糊查询\"" /></td>
		</tr>
		<tr>
			<th>部门名称：</th><td><@spring.formInput path="queryObject.orgName" attributes="tooltip=\"支持模糊查询\"" /></td>
		</tr>
	</table>
	</div>
