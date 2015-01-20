/**
 * 
 */
package com.ibm.gbsc.auth.web.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.banking.framework.dto.PagedQueryResult;
import com.ibm.banking.framework.web.SearchListBaseController;
import com.ibm.gbsc.auth.user.Organization;
import com.ibm.gbsc.auth.user.Role;
import com.ibm.gbsc.auth.user.UserAllInfo;
import com.ibm.gbsc.auth.user.UserAllInfoPagedQueryParam;
import com.ibm.gbsc.auth.user.UserAllInfoService;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author xsg
 * 
 */
@Controller
@RequestMapping("/auth/user/userAllInfo")
public class UserAllInfoController extends SearchListBaseController<UserAllInfo, UserAllInfoPagedQueryParam> {
	@Autowired
	UserAllInfoService userAllInfoService;

	@Autowired
	UserService userService;
	
	@Override
	protected String getListViewName() {
		return "userAllInfo.tile";
	}

	@Override
	protected void refData(Model model) {
		//获取所有一级组织
		List<Organization> orgList = userService.getOrgTreeByLevel(1);
		//获取所有角色
		List<Role> roleList = userService.getAllRoles();
		model.addAttribute("orgList", orgList);
		model.addAttribute("roleList", roleList);
	}

	@Override
	protected PagedQueryResult<UserAllInfo> searchData(UserAllInfoPagedQueryParam queryParam) {

		return userAllInfoService.getUserAllInfo(queryParam);
	}

	@Override
	protected String getTableId() {
		return "userAllInfoTable";
	}
}
