package net.simpleframework.mvc.component.ext.messagewindow;

import java.util.Collection;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.window.IWindowHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IMessageWindowHandle extends IWindowHandler {

	Collection<MessageNotification> getMessageNotifications(ComponentParameter cParameter);
}
