package net.simpleframework.mvc.component.ext.category;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.window.AbstractWindowHandler;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentBean(CategoryBean.class)
@ComponentName(CategoryRegistry.category)
@ComponentRender(CategoryRender.class)
@ComponentResourceProvider(CategoryResourceProvider.class)
public class CategoryRegistry extends AbstractComponentRegistry {

	public static final String category = "category";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pParameter,
			final Object data) {
		final CategoryBean categoryBean = (CategoryBean) super.createComponentBean(pParameter, data);
		ComponentHtmlRenderEx.createAjaxRequest(ComponentParameter.get(pParameter, categoryBean));

		final ComponentParameter nComponentParameter = ComponentParameter.get(pParameter,
				categoryBean);
		final String categoryName = (String) nComponentParameter.getBeanProperty("name");

		// 编辑

		pParameter.addComponentBean(categoryName + "_edit_page", AjaxRequestBean.class)
				.setHandleMethod("editUrl").setHandleClass(CategoryAction.class);
		final WindowBean window = (WindowBean) pParameter
				.addComponentBean(categoryName + "_edit", WindowBean.class)
				.setContentRef(categoryName + "_edit_page").setContentStyle("padding: 0;")
				.setHandleClass(CategoryEditWindow.class);
		window.setAttr("@category", categoryBean);

		// 删除
		pParameter.addComponentBean(categoryName + "_delete", AjaxRequestBean.class)
				.setConfirmMessage($m("Confirm.Delete")).setHandleMethod("doDelete")
				.setHandleClass(CategoryAction.class);

		// 移动
		pParameter.addComponentBean(categoryName + "_move", AjaxRequestBean.class)
				.setHandleMethod("doMove").setHandleClass(CategoryAction.class);
		return categoryBean;
	}

	public static class CategoryEditWindow extends AbstractWindowHandler {
		@Override
		public Object getBeanProperty(final ComponentParameter cParameter, final String beanProperty) {
			if (beanProperty.equals("title") || beanProperty.equals("resizable")
					|| beanProperty.equals("height") || beanProperty.equals("width")) {
				final ComponentParameter nComponentParameter = ComponentParameter.get(cParameter,
						(AbstractComponentBean) cParameter.componentBean.getAttr("@category"));
				final Map<String, Object> attri = ((ICategoryHandler) nComponentParameter
						.getComponentHandler()).categoryEdit_attri(nComponentParameter);
				if (beanProperty.equals("title")) {
					return Convert.toString(attri.get(ICategoryHandler.window_title),
							$m("CategoryRegistry.0"));
				} else if (beanProperty.equals("height")) {
					return Convert.toInt(attri.get(ICategoryHandler.window_height), 280);
				} else if (beanProperty.equals("width")) {
					return Convert.toInt(attri.get(ICategoryHandler.window_width), 340);
				} else {
					return Convert.toBool(attri.get(ICategoryHandler.window_resizable), false);
				}
			}
			return super.getBeanProperty(cParameter, beanProperty);
		}
	}
}
