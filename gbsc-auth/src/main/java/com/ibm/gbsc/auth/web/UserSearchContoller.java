package com.ibm.gbsc.auth.web;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.gbsc.auth.model.User;
import com.ibm.gbsc.auth.service.AuthService;
import com.ibm.gbsc.auth.vo.UserPagedQueryParam;
import com.ibm.gbsc.common.vo.PagedQueryResult;
import com.ibm.gbsc.web.springmvc.controller.PagedSearchListController;

/**
 * @author yangyz
 *
 */
@Controller
@RequestMapping("/auth/usersearch")
public class UserSearchContoller extends PagedSearchListController<User, UserPagedQueryParam> {

	@Inject
	AuthService authService;

	@Override
	protected PagedQueryResult<User> searchData(UserPagedQueryParam queryParam) {

		return authService.searchUser(queryParam);
	}

	@Override
	protected void refData(Model model) {

	}

	@Override
	protected String getListViewName() {
		return "/auth/userSearchList.ftl";
	}

	@Override
	protected String getTableId() {
		return "tUser";
	}
}
