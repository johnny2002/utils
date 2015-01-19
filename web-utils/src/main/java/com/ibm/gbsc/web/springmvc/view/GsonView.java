package com.ibm.gbsc.web.springmvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.view.AbstractView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Usage: <br>
 * public View getJsonData(Model model){
 *
 * <br>
 * model.addAttribute("data", datas);
 *
 * <br>
 * return new GsonView("data", null); <br>
 *
 * A View that renders its model as a JSON object.
 *
 * @author Johnny
 */
public class GsonView extends AbstractView {
	private String datePattern = "yyyy-MM-dd HH:mm:ss";
	/** Default content type. Overridable as bean property. */
	// private static final String DEFAULT_JSON_CONTENT_TYPE =
	// "application/json";
	private String jsonObjectName;

	/**
	 *
	 */
	public GsonView() {
		super();
	}

	/**
	 * @param jsonObjectName
	 *            jsonObjectName.
	 * @param excludeStrategy
	 *            excludeStrategy.
	 */
	public GsonView(String jsonObjectName, ExclusionStrategy excludeStrategy) {
		super();
		this.jsonObjectName = jsonObjectName;
		this.excludeStrategy = excludeStrategy;
	}

	private int responseStatus = HttpStatus.OK.value();
	private ExclusionStrategy excludeStrategy;
	private FieldNamingStrategy fieldNamingStrategy = null;

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
	        throws Exception {
		response.setStatus(getResponseStatus());
		response.setContentType(getContentType());
		GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat(datePattern);

		if (excludeStrategy != null) {
			gsonBuilder.setExclusionStrategies(excludeStrategy);
		}
		if (this.fieldNamingStrategy != null) {
			gsonBuilder.setFieldNamingStrategy(this.fieldNamingStrategy);
		}
		Gson gson = gsonBuilder.create();
		gson.toJson(jsonObjectName == null ? model : model.get(jsonObjectName), response.getWriter());
	}

	/**
	 * @return the objectName in model
	 */
	public String getJsonObjectName() {
		return jsonObjectName;
	}

	/**
	 * Set the date pattern, the default pattern is "yyyy-MM-dd HH:mm:ss".
	 *
	 * @param pattern
	 *            pattern.
	 */
	public void setDatePattern(String pattern) {
		this.datePattern = pattern;
	}

	/**
	 * @param objectName
	 *            the objectName in model that will be convert to json, if not
	 *            set, then convert the hole model to json
	 */
	public void setJsonObjectName(String objectName) {
		this.jsonObjectName = objectName;
	}

	/**
	 * @return the excludeStrategy
	 */
	public ExclusionStrategy getExcludeStrategy() {
		return excludeStrategy;
	}

	/**
	 * @param excludeStrategy
	 *            the excludeStrategy to set
	 */
	public void setExcludeStrategy(ExclusionStrategy excludeStrategy) {
		this.excludeStrategy = excludeStrategy;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.web.servlet.view.AbstractView#getContentType()
	 */
	@Override
	public String getContentType() {
		return "text/html;charset=utf-8";
	}

	/**
	 * @return the responseStatus
	 */
	public int getResponseStatus() {
		return responseStatus;
	}

	/**
	 * @param responseStatus
	 *            the responseStatus to set
	 */
	public void setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
	}

	/**
	 * @param fs
	 *            fs.
	 */
	public void setFieldNamingStrategy(FieldNamingStrategy fs) {
		this.fieldNamingStrategy = fs;
	}

}
