package net.simpleframework.mvc.component.ext.category;

import java.util.Map;

import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CategoryEditLoaded extends DefaultPageHandler {
	@Override
	public void pageLoad(final PageParameter pParameter, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		final ComponentParameter cParameter = CategoryUtils.get(pParameter);
		((ICategoryHandler) cParameter.getComponentHandler()).categoryEdit_onLoaded(cParameter,
				dataBinding, selector);
	}

	@Override
	public void beforeComponentRender(final PageParameter pParameter) {
		final ComponentParameter cParameter = CategoryUtils.get(pParameter);
		((ICategoryHandler) cParameter.getComponentHandler()).categoryEdit_doInit(cParameter);
	}
}
