package net.simpleframework.mvc.component.ui.pager.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ado.db.QueryEntitySet;
import net.simpleframework.ado.db.QueryEntitySet.ResultSetMetaDataCallback;
import net.simpleframework.ado.db.common.DbColumn;
import net.simpleframework.ado.db.common.SQLValue;
import net.simpleframework.ado.db.common.SqlUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.ColumnData;
import net.simpleframework.common.ado.FilterItem;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.html.element.ETextAlign;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerHandler;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumnMap;
import net.simpleframework.mvc.component.ui.pager.TablePagerUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractDbTablePagerHandler extends AbstractTablePagerHandler implements
		IDbTablePagerHandler {

	@Override
	public AbstractTablePagerSchema createTablePagerSchema() {
		return new DefaultDbTablePagerSchema();
	}

	protected class DefaultDbTablePagerSchema extends DefaultTablePagerSchema {

		@Override
		public TablePagerColumnMap getTablePagerColumns(final ComponentParameter cParameter) {
			final TablePagerColumnMap columns = super.getTablePagerColumns(cParameter);
			IDataQuery<?> dQuery;
			if (columns.size() == 0
					&& (dQuery = getDataObjectQuery(cParameter)) instanceof QueryEntitySet) {
				((QueryEntitySet<?>) dQuery).doResultSetMetaData(new ResultSetMetaDataCallback() {

					@Override
					public Object doResultSetMetaData(final ResultSetMetaData metaData)
							throws SQLException {
						for (int i = 1; i <= metaData.getColumnCount(); i++) {
							final String name = metaData.getColumnName(i);
							final String label = metaData.getColumnLabel(i);
							final TablePagerColumn col = new TablePagerColumn(name, label);
							col.setWidth(Math.max(label.length() * 12, 60));
							col.setTextAlign(ETextAlign.center);
							col.setFilter(true);
							columns.add(name, col);
						}
						return null;
					}
				});
			}
			return columns;
		}
	}

	@Override
	protected ColumnData createColumnData(final TablePagerColumn oCol) {
		final DbColumn dbColumn = new DbColumn(oCol.getColumnName());
		dbColumn.setColumnSqlName(oCol.getColumnSqlName());
		return dbColumn;
	}

	protected void doSortSQL(final ComponentParameter cParameter, final QueryEntitySet<?> qs) {
		final DbColumn dbColumn = (DbColumn) getSortColumn(cParameter);
		if (dbColumn != null) {
			final SQLValue sqlValue = qs.getSqlValue();
			sqlValue.setSql(SqlUtils.addOrderBy(qs.getDataSource(), sqlValue.getSql(), dbColumn));
		}
	}

	protected void doFilterSQL(final ComponentParameter cParameter, final QueryEntitySet<?> qs) {
		final Map<String, ColumnData> filterColumns = getFilterColumns(cParameter);
		if (filterColumns != null && filterColumns.size() > 0) {
			doFilterSQLInternal(cParameter, qs, filterColumns);
		}
	}

	protected void doFilterSQLInternal(final ComponentParameter cParameter,
			final QueryEntitySet<?> qs, final Map<String, ColumnData> filterColumns) {
		final ArrayList<Object> params = new ArrayList<Object>();
		final ArrayList<String> lExpr = new ArrayList<String>();
		final TablePagerColumnMap columns = TablePagerUtils.getTablePagerData(cParameter)
				.getTablePagerColumns(cParameter);
		for (final Map.Entry<String, ColumnData> entry : filterColumns.entrySet()) {
			final TablePagerColumn oCol = columns.get(entry.getKey());
			if (oCol == null) {
				continue;
			}

			final Iterator<FilterItem> items = entry.getValue().getFilterItems().iterator();
			if (!items.hasNext()) {
				continue;
			}
			final StringBuilder sb = new StringBuilder();
			final String columnSql = oCol.getColumnSqlName();
			sb.append(columnSql).append(filterItemExpr(items.next(), params));
			if (items.hasNext()) {
				sb.append(columnSql).append(filterItemExpr(items.next(), params));
				sb.insert(0, "(");
				sb.append(")");
			}
			lExpr.add(sb.toString());
		}
		if (lExpr.size() > 0) {
			final DataSource dataSource = qs.getDataSource();
			final String expr = StringUtils.join(lExpr, " and ");

			final SQLValue sqlValue = qs.getSqlValue();
			sqlValue.setSql(SqlUtils.addCondition(dataSource, sqlValue.getSql(), expr));
			sqlValue.addValues(params.toArray());

			final SQLValue countSQL = qs.getQueryDialect().getCountSQL();
			if (countSQL != null) {
				countSQL.setSql(SqlUtils.addCondition(dataSource, countSQL.getSql(), expr));
				countSQL.addValues(params.toArray());
			}
		}
	}

	@Override
	protected void doCount(final ComponentParameter cParameter, final IDataQuery<?> dataQuery) {
		if (dataQuery instanceof QueryEntitySet) {
			doFilterSQL(cParameter, (QueryEntitySet<?>) dataQuery);
		}
		super.doCount(cParameter, dataQuery);
	}

	@Override
	protected List<?> getData(final ComponentParameter cParameter, final int start) {
		final IDataQuery<?> dataQuery = (IDataQuery<?>) cParameter.getRequestAttr(DATA_QUERY);
		if (dataQuery instanceof QueryEntitySet) {
			doSortSQL(cParameter, (QueryEntitySet<?>) dataQuery);
		}
		return super.getData(cParameter, start);
	}
}
