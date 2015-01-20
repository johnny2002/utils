/**
 * 
 */
package com.ibm.gbsc.auth.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.banking.framework.dto.PagedQueryResult;
import com.ibm.banking.framework.web.SearchListBaseController;
import com.ibm.gbsc.auth.user.Role;
import com.ibm.gbsc.auth.user.RolePagedQueryParam;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author fanjingxuan
 * 
 */
@Controller
@RequestMapping("/roleManage")
public class RoleListController extends SearchListBaseController<Role, RolePagedQueryParam> {
	@Autowired
	UserService userService;

	@Override
	protected String getListViewName() {
		return "roleList.tile";
	}

	@Override
	protected void refData(Model arg0) {

	}

	@Override
	protected PagedQueryResult<Role> searchData(RolePagedQueryParam queryParam) {

		return userService.searchRole(queryParam);
	}

	@Override
	protected String getTableId() {
		return "roleListTable";
	}
}
