package com.ibm.gbsc.web.springmvc.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ibm.gbsc.utils.vo.PagedQueryParam;
import com.ibm.gbsc.utils.vo.PagedQueryResult;

/**
 * 查询Controller父类.
 *
 * @author Li Xin
 *
 * @param <E>
 * @param <P>
 */
public abstract class SearchListBaseController<E, P extends PagedQueryParam> extends BaseController {

	private static final int[] DEFAULT_PAGE_SIZE_OPTIONS = new int[] { 10, 20, 50, 100 };
	/**
	 *
	 */
	public static final String FORM_COMMAND_NAME = "queryObject";

	/**
	 * execute search data from backend.
	 *
	 * @param queryParam
	 *            queryParam.
	 * @throws Exception
	 *             Exception.
	 * @return PagedQueryResult<E>.
	 */
	protected abstract PagedQueryResult<E> searchData(P queryParam) throws Exception;

	/**
	 * @param queryParam
	 *            queryParam.
	 * @param request
	 *            request.
	 * @param model
	 *            model.
	 * @return string.
	 * @throws Exception
	 *             Exception.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String initSearch(@ModelAttribute(FORM_COMMAND_NAME) P queryParam, HttpServletRequest request, Model model) throws Exception {
		log.debug("init search..." + request.getRequestURI());
		initQueryParam(queryParam);
		model.addAttribute("pageSizeOptions", DEFAULT_PAGE_SIZE_OPTIONS);
		model.addAttribute("requestPath", request.getRequestURI());
		refData(model);
		return getListViewName();
	}

	/**
	 * 初始化查询参数.
	 *
	 * @param queryParam
	 *            queryParam.
	 */
	protected void initQueryParam(P queryParam) {
		queryParam.init();
	}

	/**
	 * @param queryParam
	 *            queryParam.
	 * @param bindingResult
	 *            bindingResult.
	 * @param model
	 *            model.
	 * @param request
	 *            request.
	 * @return string.
	 * @throws Exception
	 *             Exception.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String doSearch(@Validated @ModelAttribute(FORM_COMMAND_NAME) P queryParam, BindingResult bindingResult, Model model,
	        HttpServletRequest request) throws Exception {

		if (bindingResult.getErrorCount() == 0) {
			String paramPage = request
			        .getParameter((new ParamEncoder(getTableId()).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			String paramExport = request.getParameter((new ParamEncoder(getTableId())
			        .encodeParameterName(TableTagParameters.PARAMETER_EXPORTTYPE)));
			String paramSort = request
			        .getParameter((new ParamEncoder(getTableId()).encodeParameterName(TableTagParameters.PARAMETER_SORT)));

			if (!StringUtils.isEmpty(paramExport)) {
				log.debug("exporting data..." + paramExport);

				queryParam.setPageNumber(1);
				queryParam.setPageSize(20000);

			} else {
				if (paramSort != null && paramSort.length() > 0) {
					log.debug("sort data..." + paramSort);
					// searchData();
				}
				if (paramPage != null && paramPage.length() > 0) {
					log.debug(paramPage);
					queryParam.setPageNumber(Integer.parseInt(paramPage));
				} else {
					queryParam.setPageNumber(1); // 初期第一页
				}
			}
			model.addAttribute("result", searchData(queryParam));

			if (!StringUtils.isEmpty(paramExport)) {
				queryParam.init();
				return getExportResultJSP();
			}
		}

		model.addAttribute("pageSizeOptions", DEFAULT_PAGE_SIZE_OPTIONS);
		model.addAttribute("requestPath", request.getRequestURI());
		refData(model);
		model.addAttribute("tableId", getTableId());

		return getListViewName();

	}

	/**
	 * @param model
	 *            model.
	 */
	protected abstract void refData(Model model);

	/**
	 * 列表表现视图名称，如: userList.tile, dataList.jsp.
	 *
	 * @return string.
	 */
	protected abstract String getListViewName();

	/**
	 * 返回数据列表的ID，必须和jsp中display-tag的table.uid一致，同一个页面的多个表，ID不能相同。非同一页面则没规定.
	 *
	 * @return string.
	 */
	protected abstract String getTableId();

	/**
	 * @return
	 *
	 *         导出excel时跳转的页面,reset page参数
	 */
	protected String getExportResultJSP() {
		return null;
	}

}
