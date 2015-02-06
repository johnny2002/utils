<#assign sf = JspTaglibs["http://www.springframework.org/tags/form"] >
<#assign c = JspTaglibs["http://java.sun.com/jsp/jstl/core"] >
<#assign fmt = JspTaglibs["http://java.sun.com/jsp/jstl/fmt"] >
<#assign disp = JspTaglibs["http://displaytag.sf.net"] >
<#import "/macros/spring.ftl" as spring>
roles of org:${org.name}:<br/>
<form action="/firm-test/auth/orgs/${org.code}.htm" method="PUT" >
<@spring.formHiddenInput path="org.code" />
<@spring.formCheckboxes path="org.roles" options=roles separator="" errors=true/>
	<input type="button" onclick="submitForm(this.form, 'PUT')" value="保存" />
</form>