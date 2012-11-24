package net.simpleframework.mvc.component.ext.category.ctx;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.ado.db.common.DbColumn;
import net.simpleframework.common.ado.EOrder;
import net.simpleframework.common.bean.IDescriptionBeanAware;
import net.simpleframework.common.bean.IIdBeanAware;
import net.simpleframework.common.bean.INameBeanAware;
import net.simpleframework.common.bean.IOrderBeanAware;
import net.simpleframework.common.bean.ITextBeanAware;
import net.simpleframework.common.bean.ITreeBeanAware;
import net.simpleframework.ctx.ado.IBeanManagerAware;
import net.simpleframework.ctx.ado.ITreeBeanManagerAware;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentHandleException;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.category.DefaultCategoryHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class CategoryBeanAwareHandler<T extends IIdBeanAware> extends
		DefaultCategoryHandler {

	protected abstract IBeanManagerAware<T> beanMgr();

	@SuppressWarnings("unchecked")
	@Override
	public JavascriptForward onCategoryMove(final ComponentParameter cParameter,
			final TreeBean treeBean, final Object form, final Object to, final boolean up) {
		final JavascriptForward js = super.onCategoryMove(cParameter, treeBean, form, to, up);
		final DbColumn oorder = new DbColumn("oorder");
		oorder.setOrder(EOrder.desc);
		beanMgr().exchange((T) form, (T) to, oorder, up);
		return js;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onCategoryDragDrop(final ComponentParameter cParameter, final TreeBean treeBean,
			final Object drag, final Object drop) {
		final T _drag = (T) drag;
		T _drop = null;
		if (drop != null && drop.getClass().equals(drag.getClass())) {
			_drop = (T) drop;
		}
		if (_drag instanceof ITreeBeanAware) {
			((ITreeBeanAware) _drag).setParentId(_drop == null ? null : _drop.getId());
			beanMgr().update(_drag);
			return true;
		}
		return false;
	}

	@Override
	public void categoryEdit_onLoaded(final ComponentParameter cParameter,
			final Map<String, Object> dataBinding, final PageSelector selector) {
		super.categoryEdit_onLoaded(cParameter, dataBinding, selector);
		T parent = null;
		final IBeanManagerAware<T> mgr = beanMgr();
		final T t = mgr.getBean(cParameter.getParameter(PARAM_CATEGORY_ID));
		if (t != null) {
			dataBinding.put(PARAM_CATEGORY_ID, t.getId());
			if (t instanceof INameBeanAware) {
				dataBinding.put(PARAM_CATEGORY_NAME, ((INameBeanAware) t).getName());
			}
			if (t instanceof ITextBeanAware) {
				dataBinding.put(PARAM_CATEGORY_TEXT, ((ITextBeanAware) t).getText());
			}
			if (t instanceof IDescriptionBeanAware) {
				dataBinding.put(PARAM_CATEGORY_DESC, ((IDescriptionBeanAware) t).getDescription());
			}
			if (t instanceof ITreeBeanAware) {
				parent = mgr.getBean(((ITreeBeanAware) t).getParentId());
			}
		}
		if (parent == null && mgr instanceof ITreeBeanManagerAware) {
			parent = mgr.getBean(cParameter.getParameter(PARAM_CATEGORY_PARENTID));
		}
		if (parent != null) {
			dataBinding.put(PARAM_CATEGORY_PARENTID, parent.getId());
			if (parent instanceof ITextBeanAware) {
				dataBinding.put(PARAM_CATEGORY_PARENTTEXT, ((ITextBeanAware) parent).getText());
			}
		}
		onLoaded_dataBinding(cParameter, dataBinding, selector, t);
	}

	protected void onLoaded_dataBinding(final ComponentParameter cParameter,
			final Map<String, Object> dataBinding, final PageSelector selector, final T t) {
	}

	protected void onSave_setProperties(final ComponentParameter cParameter, final T t,
			final boolean insert) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public JavascriptForward categoryEdit_onSave(final ComponentParameter cParameter) {
		final JavascriptForward js = super.categoryEdit_onSave(cParameter);
		final IBeanManagerAware<T> mgr = beanMgr();
		T t = mgr.getBean(cParameter.getParameter(PARAM_CATEGORY_ID));
		final boolean insert = t == null;
		if (insert) {
			t = mgr.createBean();
		}
		onSave_setProperties(cParameter, t, insert);
		if (t instanceof INameBeanAware) {
			((INameBeanAware) t).setName(cParameter.getParameter(PARAM_CATEGORY_NAME));
		}
		if (t instanceof ITextBeanAware) {
			((ITextBeanAware) t).setText(cParameter.getParameter(PARAM_CATEGORY_TEXT));
		}
		if (t instanceof IDescriptionBeanAware) {
			((IDescriptionBeanAware) t).setDescription(cParameter.getParameter(PARAM_CATEGORY_DESC));
		}
		if (t instanceof ITreeBeanAware) {
			final T parent = mgr.getBean(cParameter.getParameter(PARAM_CATEGORY_PARENTID));
			((ITreeBeanAware) t).setParentId(parent == null ? null : parent.getId());
		}
		synchronized (mgr) {
			if (insert) {
				if (t instanceof IOrderBeanAware) {
					((IOrderBeanAware) t).setOorder(mgr.max("oorder", null) + 1);
				}
				mgr.insert(t);
			} else {
				mgr.update(t);
			}
		}
		return js;
	}

	@SuppressWarnings("unchecked")
	protected <M extends ITreeBeanAware> void onDelete_assert(final ComponentParameter cParameter,
			final T t) {
		final IBeanManagerAware<T> mgr = beanMgr();
		if (mgr instanceof ITreeBeanManagerAware
				&& ((ITreeBeanManagerAware<M>) mgr).queryChildren((M) t).getCount() > 0) {
			throw ComponentHandleException.of($m("BeanCategoryHandle.0"));
		}
	}

	@Override
	public JavascriptForward onCategoryDelete(final ComponentParameter cParameter,
			final TreeBean treeBean) {
		final JavascriptForward js = super.onCategoryDelete(cParameter, treeBean);
		final IBeanManagerAware<T> mgr = beanMgr();
		final T t = mgr.getBean(cParameter.getParameter(PARAM_CATEGORY_ID));
		if (t != null) {
			onDelete_assert(cParameter, t);
			mgr.delete(t.getId());
		}
		return js;
	}
}
