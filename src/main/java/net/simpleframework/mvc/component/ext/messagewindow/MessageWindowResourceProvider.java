package net.simpleframework.mvc.component.ext.messagewindow;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentRegistry;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;
import net.simpleframework.mvc.component.ui.window.WindowRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageWindowResourceProvider extends AbstractComponentResourceProvider {

	public MessageWindowResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getDependentComponents(final PageParameter pParameter) {
		return new String[] { WindowRegistry.window };
	}

	@Override
	public String[] getCssPath(final PageParameter pParameter) {
		return new String[] { getCssResourceHomePath(pParameter) + "/message_window.css" };
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pParameter) {
		return new String[] { getResourceHomePath() + "/js/message_window.js" };
	}
}
