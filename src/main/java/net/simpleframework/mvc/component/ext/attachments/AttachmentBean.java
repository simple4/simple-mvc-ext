package net.simpleframework.mvc.component.ext.attachments;

import net.simpleframework.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AttachmentBean extends AbstractContainerBean {

	private String insertTextarea;

	private boolean readonly;

	public AttachmentBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public String getInsertTextarea() {
		return insertTextarea;
	}

	public AttachmentBean setInsertTextarea(final String insertTextarea) {
		this.insertTextarea = insertTextarea;
		return this;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public AttachmentBean setReadonly(final boolean readonly) {
		this.readonly = readonly;
		return this;
	}
}
