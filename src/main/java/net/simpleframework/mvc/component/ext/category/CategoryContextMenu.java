package net.simpleframework.mvc.component.ext.category;

import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.menu.AbstractMenuHandler;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CategoryContextMenu extends AbstractMenuHandler {

	@Override
	public MenuItems getMenuItems(final ComponentParameter cParameter, final MenuItem menuItem) {
		final ComponentParameter nComponentParameter = CategoryUtils.get(cParameter);
		return ((ComponentHandlerEx) nComponentParameter.getComponentHandler()).getContextMenu(
				nComponentParameter, (MenuBean) cParameter.componentBean, menuItem);
	}
}
