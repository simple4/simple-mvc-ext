package net.simpleframework.mvc.component.ext.messagewindow;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.IComponentRegistry;
import net.simpleframework.mvc.component.ui.window.WindowRender;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageWindowRender extends WindowRender {

	public MessageWindowRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getJavascriptCode(cParameter));
		sb.append("__message_window_actions_init(")
				.append(ComponentRenderUtils.actionFunc(cParameter)).append(", '")
				.append(cParameter.getBeanProperty("name")).append("', '").append("ajaxRequest_")
				.append(cParameter.hashId()).append("', ")
				.append(cParameter.getBeanProperty("frequency")).append(", ")
				.append(cParameter.getBeanProperty("closeDelay")).append(");");
		return sb.toString();
	}
}
