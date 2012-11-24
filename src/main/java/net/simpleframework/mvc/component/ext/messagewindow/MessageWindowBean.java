package net.simpleframework.mvc.component.ext.messagewindow;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageWindowBean extends WindowBean {
	private int frequency = 30;

	private int closeDelay = 0;

	public MessageWindowBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setTitle($m("MessageWindowBean.0"));
		setHeight(210);
		setWidth(350);
		setHandleClass(DefaultMessageWindowHandler.class);
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(final int frequency) {
		this.frequency = frequency;
	}

	public int getCloseDelay() {
		return closeDelay;
	}

	public void setCloseDelay(final int closeDelay) {
		this.closeDelay = closeDelay;
	}

	@Override
	public boolean isShowCenter() {
		return false;
	}

	@Override
	public boolean isModal() {
		return false;
	}
}
