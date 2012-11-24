package net.simpleframework.mvc.component.ext.category;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryTreeHandler;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class CategoryUtils {
	public static final String BEAN_ID = "category_@bid";

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static class CategoryTree extends AbstractTreeHandler {
		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$category");
			return ((ICategoryHandler) nComponentParameter.getComponentHandler())
					.getFormParameters(nComponentParameter);
		}

		@Override
		public TreeNodes getTreenodes(final ComponentParameter cParameter, final TreeNode treeNode) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$category");
			return ((ICategoryHandler) nComponentParameter.getComponentHandler())
					.getCategoryTreenodes(nComponentParameter, (TreeBean) cParameter.componentBean,
							treeNode);
		}

		@Override
		public boolean doDragDrop(final ComponentParameter cParameter, final TreeNode drag,
				final TreeNode drop) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$category");
			return ((ICategoryHandler) nComponentParameter.getComponentHandler()).onCategoryDragDrop(
					nComponentParameter, (TreeBean) cParameter.componentBean, drag.getDataObject(),
					drop.getDataObject());
		}
	}

	public static class CategoryDictTree extends DictionaryTreeHandler {
		@Override
		public TreeNodes getTreenodes(final ComponentParameter cParameter, final TreeNode treeNode) {
			final ComponentParameter nComponentParameter = CategoryUtils.get(cParameter);
			return ((ICategoryHandler) nComponentParameter.getComponentHandler())
					.getCategoryDictTreenodes(nComponentParameter, (TreeBean) cParameter.componentBean,
							treeNode);
		}
	}
}
