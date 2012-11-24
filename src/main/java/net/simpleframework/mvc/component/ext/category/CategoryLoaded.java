package net.simpleframework.mvc.component.ext.category;

import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.category.CategoryUtils.CategoryTree;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.tree.TreeBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CategoryLoaded extends DefaultPageHandler {

	@Override
	public void beforeComponentRender(final PageParameter pParameter) {
		final ComponentParameter cParameter = CategoryUtils.get(pParameter);

		final CategoryBean categoryBean = (CategoryBean) cParameter.componentBean;
		final String categoryName = (String) cParameter.getBeanProperty("name");

		final TreeBean treeBean = pParameter.addComponentBean(categoryName + "_tree", TreeBean.class);
		if ((Boolean) cParameter.getBeanProperty("showContextMenu")) {
			treeBean.setContextMenu(categoryName + "_contextMenu");

			pParameter.addComponentBean(categoryName + "_contextMenu", MenuBean.class).setHandleClass(
					CategoryContextMenu.class);
		}

		treeBean.setDynamicLoading((Boolean) cParameter.getBeanProperty("dynamicTree"))
				.setCookies((Boolean) cParameter.getBeanProperty("cookies"))
				.setJsLoadedCallback((String) cParameter.getBeanProperty("jsLoadedCallback"))
				.setContainerId("category_" + categoryBean.hashId()).setHandleClass(CategoryTree.class)
				.setRunImmediately(false).setAttr("$category", categoryBean);
		categoryBean.setAttr("$tree", treeBean);
	}
}
