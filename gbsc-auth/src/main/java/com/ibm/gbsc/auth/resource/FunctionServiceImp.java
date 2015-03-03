package com.ibm.gbsc.auth.resource;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.gbsc.auth.model.Function;
import com.ibm.gbsc.auth.model.Role;

/**
 * function service implementation.
 *
 * @author Johnny
 */
@Service
@Transactional(readOnly = true)
public class FunctionServiceImp implements FunctionService {
	@PersistenceContext
	EntityManager em;
	/**
	 * logger.
	 */
	Logger log = LoggerFactory.getLogger(this.getClass());

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public List<Function> getFunctionsByType(String menuType, boolean retrieveAll) {
		List<Function> funcs;
		if ("TOP".equals(menuType)) {
			funcs = em.createNamedQuery("Function.getTOP").getResultList();
		} else {
			funcs = em.createNamedQuery("Function.getMenu").getResultList();
		}
		if (retrieveAll) {
			initSubFuncs(funcs, retrieveAll);
		}
		return funcs;
	}

	/** {@inheritDoc} */
	@Override
	public Function getFunctionById(String id, boolean recursive, String type) {
		Function func = em.find(Function.class, id);
		log.debug("Func name {}, Roles{}", func.getName(), func.getRoles());
		if (recursive) {
			initSubFuncs(func.getChildren(), recursive);
		}
		return func;
	}

	/** {@inheritDoc} */
	@Override
	public void saveFunction(Function func) {
		em.merge(func);
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void saveFunctionTree(List<Function> menus) {
		handleSaveMenuTree(menus);
	}

	/**
	 * @param menus
	 *            menus
	 * @param recursive
	 *            recursively or not
	 */
	private void initSubFuncs(List<Function> menus, boolean recursive) {
		for (Function menu : menus) {
			if (menu.getChildren() != null) {
				menu.getRoles().size();
				if (recursive) {
					initSubFuncs(menu.getChildren(), recursive);
				}
			}
		}

	}

	/**
	 * @param menus
	 *            menus
	 */
	private void handleSaveMenuTree(List<Function> menus) {
		for (Function menu : menus) {
			log.debug("save menu {}, roles{}", menu.getName(), menu.getRoles());
			if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
				handleSaveMenuTree(menu.getChildren());
				// 如果只是菜单组的话，权限是子菜单的并集，不然的话，应该加上
				if (menu.getUrl() == null || menu.getUrl().length() == 0) {
					if (menu.getRoles() == null) {
						menu.setRoles(new HashSet<Role>());
					} else {
						menu.getRoles().clear();
					}
				}
				// 把子菜单的权限加到父菜单上
				for (Function childMenu : menu.getChildren()) {
					for (Role role : childMenu.getRoles()) {
						menu.getRoles().add(role);
					}
				}
			}
			em.merge(menu);
		}

	}

	@Override
	public List<Function> getFunctionTree() {
		List<Function> funcs = em.createNamedQuery("Function.get1stLevel", Function.class).getResultList();
		initSubFuncs(funcs, true);
		return funcs;
	}

	@Override
	public List<Function> getAllFunctions() {
		List<Function> menus = em.createNamedQuery("Function.getAll", Function.class).getResultList();
		for (Function func : menus) {
			func.getRoles().size();
		}
		return menus;
	}

	@Override
	public List<Function> getFunctionsByType(String type) {
		// session.enableFilter("menuActiveFilter");
		List<Function> funcs;
		if ("TOP".equals(type)) {
			funcs = em.createNamedQuery("Function.getTOP", Function.class).getResultList();
		} else {
			funcs = em.createNamedQuery("Function.getByType", Function.class).setParameter("menuType", type).getResultList();
		}
		initSubMenus(funcs);
		// session.disableFilter("menuActiveFilter");
		return funcs;
	}

	@Override
	public Function getFunctionById(String menuId) {
		// session.enableFilter("menuActiveFilter");
		Function func = em.find(Function.class, menuId);
		if (func != null) {
			func.getRoles().size();
			initSubMenus(func.getChildren());
		}
		// session.disableFilter("menuActiveFilter");
		return func;
	}

	private void initSubMenus(List<Function> menus) {
		for (Function menu : menus) {
			if (menu.getChildren() != null) {
				menu.getRoles().size();
				menu.getChildren().size();
				initSubMenus(menu.getChildren());
			}
		}

	}
}
