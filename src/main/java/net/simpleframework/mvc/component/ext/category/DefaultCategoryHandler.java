package net.simpleframework.mvc.component.ext.category;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.NotImplementedException;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.bean.IIdBeanAware;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.js.EJavascriptEvent;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.EWarnType;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ext.category.CategoryUtils.CategoryDictTree;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.propeditor.EInputCompType;
import net.simpleframework.mvc.component.ui.propeditor.InputComp;
import net.simpleframework.mvc.component.ui.propeditor.PropEditorBean;
import net.simpleframework.mvc.component.ui.propeditor.PropField;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultCategoryHandler extends ComponentHandlerEx implements ICategoryHandler {

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
		return ((KVMap) super.getFormParameters(cParameter)).add(CategoryUtils.BEAN_ID,
				cParameter.hashId());
	}

	protected IDataQuery<?> categoryBeans(final ComponentParameter cParameter,
			final Object categoryId) {
		throw NotImplementedException.of(getClass(), "categoryBeans");
	}

	@Override
	public TreeNodes getCategoryTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode parent) {
		Object dataObject = parent != null ? parent.getDataObject() : null;
		if (dataObject instanceof IIdBeanAware) {
			dataObject = ((IIdBeanAware) dataObject).getId();
		}
		final IDataQuery<?> query = categoryBeans(cParameter, dataObject);
		if (query != null && query.getCount() > 0) {
			final boolean draggable = (Boolean) cParameter.getBeanProperty("draggable");
			final TreeNodes nodes = TreeNodes.of();
			Object bean;
			while ((bean = query.next()) != null) {
				final TreeNode treeNode = new TreeNode(treeBean, parent, bean);
				treeNode.setDraggable(draggable);
				treeNode.setAcceptdrop(draggable);
				nodes.add(treeNode);
			}
			return nodes;
		}
		return null;
	}

	@Override
	public TreeNodes getCategoryDictTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode parent) {
		return getCategoryTreenodes(cParameter, treeBean, parent);
	}

	protected String[] getContextMenuKeys() {
		return new String[] { "Add", "Edit", "Delete", "-", "Refresh", "-", "Move", "-", "Expand",
				"Collapse" };
	}

	@Override
	public MenuItems getContextMenu(final ComponentParameter cParameter, final MenuBean menuBean,
			final MenuItem menuItem) {
		if (menuItem == null) {
			final KVMap map = new KVMap()
					.add("Add",
							MenuItem.of(menuBean, $m("Add"), MenuItem.ICON_ADD,
									"$category_action(item).add();"))
					.add("Edit",
							MenuItem.of(menuBean, $m("Edit"), MenuItem.ICON_EDIT,
									"$category_action(item).edit();"))
					.add("Delete",
							MenuItem.of(menuBean, $m("Delete"), MenuItem.ICON_DELETE,
									"$category_action(item).del();"))
					.add("Refresh",
							MenuItem.of(menuBean, $m("Refresh"), MenuItem.ICON_REFRESH,
									"$category_action(item).refresh();"))
					.add("Move",
							MenuItem
									.of(menuBean, $m("Menu.move"))
									.addChild(
											MenuItem.of(menuBean, $m("Menu.up"), MenuItem.ICON_UP,
													"$category_action(item).move(true, false);"))
									.addChild(
											MenuItem.of(menuBean, $m("Menu.up2"), MenuItem.ICON_UP2,
													"$category_action(item).move(true, true);"))
									.addChild(
											MenuItem.of(menuBean, $m("Menu.down"), MenuItem.ICON_DOWN,
													"$category_action(item).move(false, false);"))
									.addChild(
											MenuItem.of(menuBean, $m("Menu.down2"), MenuItem.ICON_DOWN2,
													"$category_action(item).move(false, true);")))
					.add("Expand",
							MenuItem.of(menuBean, $m("Tree.expand"), MenuItem.ICON_EXPAND,
									"$category_action(item).expand();"))
					.add("Collapse",
							MenuItem.of(menuBean, $m("Tree.collapse"), MenuItem.ICON_COLLAPSE,
									"$category_action(item).collapse();"));
			final MenuItems items = MenuItems.of();
			for (final String k : getContextMenuKeys()) {
				if ("-".equals(k)) {
					items.add(MenuItem.sep(menuBean));
				} else {
					items.add((MenuItem) map.get(k));
				}
			}
			return items;
		}
		return null;
	}

	@Override
	public JavascriptForward categoryEdit_onSave(final ComponentParameter cParameter) {
		final JavascriptForward js = refreshTree(cParameter);
		// 关闭窗口
		js.append("$Actions['").append(cParameter.getBeanProperty("name")).append("_edit'].close();");
		return js;
	}

	@Override
	public JavascriptForward onCategoryDelete(final ComponentParameter cParameter,
			final TreeBean treeBean) {
		return refreshTree(cParameter);
	}

	@Override
	public boolean onCategoryDragDrop(final ComponentParameter cParameter, final TreeBean treeBean,
			final Object drag, final Object drop) {
		return false;
	}

	@Override
	public JavascriptForward onCategoryMove(final ComponentParameter cParameter,
			final TreeBean treeBean, final Object form, final Object to, final boolean up) {
		return refreshTree(cParameter);
	}

	protected JavascriptForward refreshTree(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		final String categoryName = (String) cParameter.getBeanProperty("name");
		// 刷新tree
		js.append("$Actions['").append(categoryName).append("'].refresh();");
		return js;
	}

	@Override
	public void categoryEdit_onLoaded(final ComponentParameter cParameter,
			final Map<String, Object> dataBinding, final PageSelector selector) {
	}

	@Override
	public KVMap categoryEdit_attri(final ComponentParameter cParameter) {
		return new KVMap().add(edit_url, ComponentUtils.getResourceHomePath(CategoryBean.class)
				+ "/jsp/category_edit.jsp");
	}

	@Override
	public void categoryEdit_doInit(final ComponentParameter cParameter) {
		// 属性列表
		categoryEdit_createPropEditor(cParameter);
		// 验证
		categoryEdit_createValidation(cParameter);

		// 类目字典
		categoryEdit_createDictTree(cParameter);
		categoryEdit_createDict(cParameter);
		// 保存
		categoryEdit_createSave(cParameter);
	}

	protected AbstractComponentBean categoryEdit_createPropEditor(final ComponentParameter cParameter) {
		final CategoryBean category = (CategoryBean) cParameter.componentBean;
		final String categoryName = (String) cParameter.getBeanProperty("name");

		final PropEditorBean propEditor = (PropEditorBean) cParameter.addComponentBean(
				categoryName + "_propEditor", PropEditorBean.class).setContainerId(
				"idCategoryEdit_" + category.hashId());
		final PropField f1 = new PropField($m("category_edit.0")).addComponents(new InputComp(
				"category_id").setType(EInputCompType.hidden), new InputComp("category_text"));
		final PropField f2 = new PropField($m("category_edit.1")).addComponents(new InputComp(
				"category_name"));
		final PropField f3 = new PropField($m("category_edit.2")).addComponents(new InputComp(
				"category_parentId").setType(EInputCompType.hidden), new InputComp(
				"category_parentText").setType(EInputCompType.textButton).setAttributes("readonly")
				.addEvent(EJavascriptEvent.click, "$Actions['" + categoryName + "_dict']();"));
		final PropField f4 = new PropField($m("Description")).addComponents(new InputComp(
				"category_description").setType(EInputCompType.textarea).setAttributes("rows:6"));
		propEditor.getFormFields().append(f1, f2, f3, f4);

		return propEditor;
	}

	protected AbstractComponentBean categoryEdit_createValidation(final ComponentParameter cParameter) {
		final String categoryName = (String) cParameter.getBeanProperty("name");
		return cParameter
				.addComponentBean(categoryName + "_validation", ValidationBean.class)
				.setTriggerSelector(".CategoryEdit .button2")
				.setWarnType(EWarnType.insertAfter)
				.addValidators(
						new Validator().setSelector("#category_text, #category_name").setMethod(
								EValidatorMethod.required));
	}

	protected AbstractComponentBean categoryEdit_createDictTree(final ComponentParameter cParameter) {
		final String categoryName = (String) cParameter.getBeanProperty("name");
		return cParameter.addComponentBean(categoryName + "_dict_tree", TreeBean.class)
				.setDynamicLoading((Boolean) cParameter.getBeanProperty("dynamicTree"))
				.setCookies((Boolean) cParameter.getBeanProperty("cookies"))
				.setJsLoadedCallback((String) cParameter.getBeanProperty("jsLoadedCallback"))
				.setHandleClass(CategoryDictTree.class);
	}

	protected AbstractComponentBean categoryEdit_createDict(final ComponentParameter cParameter) {
		final String categoryName = (String) cParameter.getBeanProperty("name");
		final String selector = (String) cParameter.getBeanProperty("selector");
		final DictionaryBean dictionary = (DictionaryBean) cParameter
				.addComponentBean(categoryName + "_dict", DictionaryBean.class)
				.setBindingId("category_parentId").setBindingText("category_parentText")
				.setTitle($m("category_edit.2")).setSelector(selector);
		dictionary.addTreeRef(cParameter, categoryName + "_dict_tree");
		return dictionary;
	}

	protected AbstractComponentBean categoryEdit_createSave(final ComponentParameter cParameter) {
		final String categoryName = (String) cParameter.getBeanProperty("name");
		final String selector = (String) cParameter.getBeanProperty("selector");
		return cParameter.addComponentBean(categoryName + "_save", AjaxRequestBean.class)
				.setHandleMethod("doSave").setHandleClass(CategoryAction.class)
				.setSelector(selector + ", #idCategoryEdit_" + cParameter.hashId());
	}

	protected static String PARAM_CATEGORY_ID = "category_id";

	protected static String PARAM_CATEGORY_NAME = "category_name";

	protected static String PARAM_CATEGORY_TEXT = "category_text";

	protected static String PARAM_CATEGORY_PARENTID = "category_parentId";

	protected static String PARAM_CATEGORY_PARENTTEXT = "category_parentText";

	protected static String PARAM_CATEGORY_DESC = "category_description";

	// utils

	protected String getImgBase(final ComponentParameter cParameter,
			final Class<? extends AbstractMVCPage> pageClass) {
		return AbstractMVCPage.get(pageClass).getCssResourceHomePath(cParameter) + "/images/";
	}
}
