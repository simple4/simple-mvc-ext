package net.simpleframework.mvc.component.ext.category;

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
public class CategoryBean extends AbstractContainerBean {
	private boolean cookies = true;

	private boolean draggable = true, dynamicTree;

	private boolean showContextMenu = true;

	private String jsLoadedCallback;

	public CategoryBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public boolean isCookies() {
		return cookies;
	}

	public CategoryBean setCookies(final boolean cookies) {
		this.cookies = cookies;
		return this;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public CategoryBean setDraggable(final boolean draggable) {
		this.draggable = draggable;
		return this;
	}

	public boolean isShowContextMenu() {
		return showContextMenu;
	}

	public CategoryBean setShowContextMenu(final boolean showContextMenu) {
		this.showContextMenu = showContextMenu;
		return this;
	}

	public boolean isDynamicTree() {
		return dynamicTree;
	}

	public CategoryBean setDynamicTree(final boolean dynamicTree) {
		this.dynamicTree = dynamicTree;
		return this;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public CategoryBean setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback" };
	}
}
