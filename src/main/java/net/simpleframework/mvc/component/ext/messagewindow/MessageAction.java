package net.simpleframework.mvc.component.ext.messagewindow;

import java.util.Collection;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JsonForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageAction extends DefaultAjaxRequestHandler {

	@Override
	public IForward ajaxProcess(final ComponentParameter cParameter) {
		final JsonForward json = new JsonForward();
		final ComponentParameter nComponentParameter = MessageWindowUtils.get(cParameter);
		final IMessageWindowHandle messageWindowHandle = (IMessageWindowHandle) nComponentParameter
				.getComponentHandler();
		if (DefaultMessageWindowHandler.class.equals(messageWindowHandle.getClass())) {
			json.put("showMessageNotification", true);
		} else {
			final Collection<MessageNotification> coll = messageWindowHandle
					.getMessageNotifications(nComponentParameter);
			json.put("showMessageNotification", coll != null && coll.iterator().hasNext());
		}
		return json;
	}
}