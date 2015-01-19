package com.ibm.gbsc.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author yangyz
 *
 */
public class LookupTag extends TagSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = -5594482526141365603L;
	private int width = 300;
	private int height = 500;
	private String url;
	private boolean disabled;
	private boolean draggable;
	private String params;
	private String id;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		// HttpServletRequest request = (HttpServletRequest)
		// pageContext.getRequest();
		try {
			out.append("<div id=\"lookup_").append(getId()).append("\"></div>");
			// the div used to display lookup page
			out.append("<a id=\"open_lookup_").append(getId()).append("\" class=\"lookup");
			if (isDisabled()) {
				out.append(" lookupDisabled");
			}
			out.append("\" href=\"javascript: openlookup('").append(getId()).append("');\">");
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_BODY_INCLUDE;
	}

	/** {@inheritDoc} */
	@Override
	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			out.append("</a>");
			out.append("<script type=\"text/javascript\"> $(function() {$(\"#lookup_").append(getId())
			        .append("\").dialog({show: 'clip',hide: 'clip',bgiframe: true,autoOpen: false,height:")
			        .append(String.valueOf(getHeight())).append(",draggable:").append(String.valueOf(draggable)).append(",width:")
			        .append(String.valueOf(getWidth())).append(",modal: true,close: function(){$(\"#lookup_").append(getId())
			        .append("\").html(\"\");},open: function(){$(\"#lookup_").append(getId()).append("\").load(\"");
			out.append(getUrl());
			if (getUrl().indexOf("?") >= 0) {
				out.append("&");
			} else {
				out.append("?");
			}
			if (getParams() != null) {
				out.append(getParams()).append("&");
			}
			out.append("lookupId=").append(getId());
			out.append("\");}});");
			if (isDisabled()) {
				out.append("$('#open_lookup_").append(getId()).append("').bind('click', function(){return false;});");
			}
			out.append("})</script>");

		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @param disabled
	 *            the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the draggable
	 */
	public boolean isDraggable() {
		return draggable;
	}

	/**
	 * @param draggable
	 *            the draggable to set
	 */
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	/**
	 * @return the params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

}
