/**
 * 
 */
package com.ibm.gbsc.auth.web.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.ibm.gbsc.auth.function.Function;
import com.ibm.gbsc.auth.function.FunctionService;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author xushigang
 */
@Controller
@RequestMapping("/auth")
@SessionAttributes({ "functionList", "theFunction" })
public class FunctionEditContoller {
	Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	UserService userService;
	@Autowired
	FunctionService functionService;

	/**
	 * @param model
	 *            model
	 */
	@ModelAttribute
	protected void refData(Model model) {

		model.addAttribute("appRoles", userService.getAllRoles());

	}

	/**
	 * @param model
	 *            model
	 * @return page
	 */
	@RequestMapping(value = "/function/functions", method = RequestMethod.GET)
	public String showFunctionAll(Model model) {

		List<Function> functionList = functionService.getFunctionTree();

		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("name") && !f.getName().equals("id") && !f.getName().equals("children");
			}

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});

		model.addAttribute("zTreeJson", bld.create().toJson(functionList));
		model.addAttribute("functionList", functionList);
		return "functionAll.tile";
	}

	/**
	 * @param funcId
	 *            func id
	 * @param functionList
	 *            func list
	 * @param model
	 *            model
	 * @return page
	 */
	@RequestMapping(value = "/function/functions/{funcId}", method = RequestMethod.GET)
	public String showFunctionDetail(@PathVariable String funcId, @ModelAttribute List<Function> functionList, Model model) {
		return gotoFunctionDetail(funcId, functionList, model);
	}

	/**
	 * @param funcId
	 *            func id
	 * @param functionList
	 *            func list
	 * @param model
	 *            model
	 * @return page
	 */
	private String gotoFunctionDetail(String funcId, List<Function> functionList, Model model) {
		Function theFunction = findFunction(functionList, funcId);
		model.addAttribute("theFunction", theFunction);
		return "/functionrole/functionDetail.jsp";
	}

	/**
	 * @param functionList
	 *            func list
	 * @param funcId
	 *            func id
	 * @return page
	 */
	private Function findFunction(List<Function> functionList, String funcId) {
		Function foundFunction = null;
		for (Function func : functionList) {
			if (funcId.equals(func.getId())) {
				foundFunction = func;
				break;
			} else if (func.getChildren() != null && !func.getChildren().isEmpty()) {
				foundFunction = findFunction(func.getChildren(), funcId);
				if (foundFunction != null) {
					break;
				}
			}
		}
		return foundFunction;
	}

	/**
	 * @param funcId func id
	 * @param theFunction func
	 * @param functionList func list
	 * @param model model
	 * @return page
	 */
	@RequestMapping(value = "/function/cachefunc", method = RequestMethod.POST)
	public String cacheFunctionChange(@RequestParam String funcId, @ModelAttribute("theFunction") Function theFunction,
			@ModelAttribute("functionList") List<Function> functionList, Model model) {
		log.debug("cachefunc method");
		return gotoFunctionDetail(funcId, functionList, model);
	}

	/**
	 * @param theFunction func
	 * @param functionList func list
	 * @param status status
	 * @return page
	 */
	@RequestMapping(value = "/function/savefunc", method = RequestMethod.POST)
	public String saveFunctionChange(@ModelAttribute("theFunction") Function theFunction,
			@ModelAttribute("functionList") List<Function> functionList, SessionStatus status) {

		functionService.saveFunctionTree(functionList);
		status.setComplete();
		return "redirect:/auth/function/functions.htm";
	}

	/**
	 * @param model model
	 * @param status status
	 * @return page
	 */
	@RequestMapping(value = "/function/cancelfunc", method = RequestMethod.GET)
	public String cancelFunctionChange(Model model, SessionStatus status) {
		status.setComplete();
		return "redirect:/";
	}

}
