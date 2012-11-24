package net.simpleframework.mvc.component.ext.comments;

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
public class CommentBean extends AbstractContainerBean {

	/* 是否显示表情 */
	private boolean showSmiley = true;

	/* 是否允许评论 */
	private boolean canReply = true;

	/* 操作角色 */
	private String role;

	public CommentBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public boolean isShowSmiley() {
		return showSmiley;
	}

	public CommentBean setShowSmiley(final boolean showSmiley) {
		this.showSmiley = showSmiley;
		return this;
	}

	public boolean isCanReply() {
		return canReply;
	}

	public CommentBean setCanReply(final boolean canReply) {
		this.canReply = canReply;
		return this;
	}

	public String getRole() {
		return role;
	}

	public CommentBean setRole(final String role) {
		this.role = role;
		return this;
	}
}
