package net.simpleframework.mvc.component.ext.messagewindow;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.window.WindowRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(MessageWindowRegistry.messageWindow)
@ComponentBean(MessageWindowBean.class)
@ComponentRender(MessageWindowRender.class)
@ComponentResourceProvider(MessageWindowResourceProvider.class)
public class MessageWindowRegistry extends WindowRegistry {
	public static final String messageWindow = "messageWindow";

	@Override
	public MessageWindowBean createComponentBean(final PageParameter pParameter, final Object data) {
		final MessageWindowBean messageWindowBean = (MessageWindowBean) super.createComponentBean(
				pParameter, data);

		final String beanId = messageWindowBean.hashId();
		if (!StringUtils.hasText(messageWindowBean.getContent())
				&& !StringUtils.hasText(messageWindowBean.getContentRef())) {
			final String ajaxRequestName = "ajaxRequest2_" + beanId;
			messageWindowBean.setContent(AbstractComponentRegistry.getLoadingContent());
			messageWindowBean.setContentRef(ajaxRequestName);
			pParameter
					.addComponentBean(ajaxRequestName, AjaxRequestBean.class)
					.setShowLoading(false)
					.setUrlForward(
							getComponentResourceProvider().getResourceHomePath()
									+ "/jsp/message_window.jsp?" + MessageWindowUtils.BEAN_ID + "=" + beanId);
		}

		pParameter
				.addComponentBean("ajaxRequest_" + beanId, AjaxRequestBean.class)
				.setShowLoading(false)
				.setJsCompleteCallback(
						"$Actions['" + messageWindowBean.getName() + "'].ajaxRequestCallback(json);")
				.setParameters(MessageWindowUtils.BEAN_ID + "=" + beanId)
				.setHandleClass(MessageAction.class);
		return messageWindowBean;
	}
}
