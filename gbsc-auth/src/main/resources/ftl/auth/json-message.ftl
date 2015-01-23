<meta http-equiv="Content-Type" content="application/json; charset=utf-8" />
<#assign sf = JspTaglibs["http://www.springframework.org/tags/form"] >
<#assign c = JspTaglibs["http://java.sun.com/jsp/jstl/core"] >
<#assign fmt = JspTaglibs["http://java.sun.com/jsp/jstl/fmt"] >
<#assign disp = JspTaglibs["http://displaytag.sf.net"] >
<@fmt.setBundle basename="i18n/auth-messages" />
<#import "/macros/spring.ftl" as spring>
{message:"<@spring.messageArgs MSG_KEY MSG_PARAM />"}