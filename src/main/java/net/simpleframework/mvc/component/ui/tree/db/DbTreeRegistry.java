package net.simpleframework.mvc.component.ui.tree.db;

import net.simpleframework.common.xml.XmlElement;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeRegistry;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(DbTreeRegistry.dbTree)
@ComponentBean(DbTreeBean.class)
public class DbTreeRegistry extends AbstractTreeRegistry {
	public static final String dbTree = "dbTree";

	@Override
	protected TreeNode createTreeNode(final XmlElement xmlElement, final TreeBean treeBean,
			final TreeNode parent) {
		return null;
	}
}
