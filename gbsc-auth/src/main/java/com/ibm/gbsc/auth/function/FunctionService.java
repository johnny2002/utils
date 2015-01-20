package com.ibm.gbsc.auth.function;

import java.util.List;

/**
 * function service interface.
 * @author Johnny
 */
public interface FunctionService {

	/**
	 * get menu items by type.
	 * @param functionType
	 *            menu type
	 * @param retrieveAll indicate to retrieve all functions of only 1 level.
	 * @return the selected menu items.
	 */
	List<Function> getFunctionsByType(String functionType, boolean retrieveAll);
	/**
	 * 
	 * @return 激活的功能树
	 */
	List<Function> getFunctionTree();
	/**
	 * 
	 * @return 激活的功能列表
	 */
	List<Function> getAllFunctions();

	/**
	 * retrieve the menu by id.
	 * 
	 * @param funcId
	 *            the menu id
	 * @param recursive TODO
	 * @param type TODO
	 * @return the menu item
	 */
	Function getFunctionById(String funcId, boolean recursive, String type);

	/**
	 * Save the menu.
	 * 
	 * @param func function
	 */
	void saveFunction(Function func);

	/**
	 * Save the menu tree, cascade to all children menu items.
	 * 
	 * @param functions functionss
	 */
	void saveFunctionTree(List<Function> functions);
	/**
	 * retrieve the menu by id
	 * 
	 * @param funcId
	 *            the menu id
	 * @return the menu item
	 */
	Function getFunctionById(String funcId);
	List<Function> getFunctionsByType(String menuType);
}
