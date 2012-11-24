package net.simpleframework.mvc.component.ui.tree.db;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ado.DataServiceFactory;
import net.simpleframework.ado.db.ITableEntityService;
import net.simpleframework.ado.db.common.DbTable;
import net.simpleframework.ado.db.common.ExpressionValue;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class DbTreeHandler extends AbstractTreeHandler {

	@Override
	public TreeNodes getTreenodes(final ComponentParameter cParameter, final TreeNode treeNode) {
		final TreeNodes nodes = TreeNodes.of();
		final DbTreeBean dbTree = (DbTreeBean) cParameter.componentBean;
		final DbTreeNode dbNode = (DbTreeNode) treeNode;
		final IDataQuery<Map<String, Object>> set = getDataQuery(cParameter, dbNode);
		Map<String, Object> rowData;
		while ((rowData = set.next()) != null) {
			nodes.add(new DbTreeNode(dbTree, dbNode, rowData));
		}
		return nodes;
	}

	private final Map<String, ITableEntityService> entityManagerCache = new HashMap<String, ITableEntityService>();

	protected IDataQuery<Map<String, Object>> getDataQuery(final ComponentParameter cParameter,
			final DbTreeNode dbNode) {
		final DbTreeBean dbTree = (DbTreeBean) cParameter.componentBean;
		final String table = dbTree.getTableName();
		final String id = dbTree.getIdName();

		ITableEntityService entityManager = entityManagerCache.get(table);
		if (entityManager == null) {
			entityManagerCache.put(
					table,
					entityManager = new DataServiceFactory(getDataSource())
							.createEntityService(new DbTable(table, id)));
		}
		final String parentId = dbTree.getParentIdName();
		if (dbNode == null) {
			return entityManager.query(new ExpressionValue(getRootExpressionValue(parentId)));
		} else {
			return entityManager.query(new ExpressionValue(parentId + "=?", dbNode.getRowData()
					.get(id)));
		}
	}

	protected abstract DataSource getDataSource();

	protected String getRootExpressionValue(final String parentId) {
		return parentId + "=0";
	}
}
