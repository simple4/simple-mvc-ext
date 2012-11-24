package net.simpleframework.mvc.component.ext.category;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.Convert;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CategoryAction extends DefaultAjaxRequestHandler {

	public IForward editUrl(final ComponentParameter cParameter) {
		final ComponentParameter nComponentParameter = CategoryUtils.get(cParameter);
		return new UrlForward((String) ((ICategoryHandler) nComponentParameter.getComponentHandler())
				.categoryEdit_attri(nComponentParameter).get(ICategoryHandler.edit_url));
	}

	public IForward doSave(final ComponentParameter cParameter) {
		final ComponentParameter nComponentParameter = CategoryUtils.get(cParameter);
		return ((ICategoryHandler) nComponentParameter.getComponentHandler())
				.categoryEdit_onSave(nComponentParameter);
	}

	public IForward doDelete(final ComponentParameter cParameter) {
		final ComponentParameter nComponentParameter = CategoryUtils.get(cParameter);
		final TreeBean treeBean = (TreeBean) nComponentParameter.componentBean.getAttr("$tree");
		return ((ICategoryHandler) nComponentParameter.getComponentHandler()).onCategoryDelete(
				nComponentParameter, treeBean);
	}

	public IForward doMove(final ComponentParameter cParameter) {
		final ComponentParameter nComponentParameter = CategoryUtils.get(cParameter);

		final TreeBean treeBean = (TreeBean) nComponentParameter.componentBean.getAttr("$tree");
		final ComponentParameter tComponentParameter = ComponentParameter.get(cParameter, treeBean);
		final TreeNode node1 = TreeUtils.getTreenodeById(tComponentParameter,
				tComponentParameter.getParameter("b1"));
		final TreeNode node2 = TreeUtils.getTreenodeById(tComponentParameter,
				tComponentParameter.getParameter("b2"));
		if (node1 == null || node2 == null) {
			return new JavascriptForward("alert('").append($m("CategoryAction.0")).append("');");
		}

		return ((ICategoryHandler) nComponentParameter.getComponentHandler()).onCategoryMove(
				nComponentParameter, treeBean, node1.getDataObject(), node2.getDataObject(),
				Convert.toBool(nComponentParameter.getParameter("up")));
	}
}
