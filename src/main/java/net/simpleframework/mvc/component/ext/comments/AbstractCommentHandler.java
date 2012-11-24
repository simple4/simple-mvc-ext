package net.simpleframework.mvc.component.ext.comments;

import net.simpleframework.common.bean.BeanUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.script.MVEL2Template;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractCommentHandler extends ComponentHandlerEx implements ICommentHandler {

	protected static String PARAM_COMMENT = "comment";

	protected static String PARAM_PARENTID = "parentId";

	@Override
	public JavascriptForward addComment(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward("$comment_do_callback(").append(
				comments(cParameter).getCount()).append(");");
		return js;
	}

	@Override
	public JavascriptForward deleteComment(final ComponentParameter cParameter, final Object id) {
		final JavascriptForward js = new JavascriptForward("$comment_do_callback(").append(
				comments(cParameter).getCount()).append(");");
		return js;
	}

	@Override
	public Object getProperty(final ComponentParameter cParameter, final Object o, final String name) {
		return BeanUtils.getProperty(o, name);
	}

	@Override
	public String toCommentHTML(final ComponentParameter cParameter) {
		final KVMap variables = new KVMap().add("name", cParameter.getBeanProperty("name"))
				.add("hashId", cParameter.hashId())
				.add("showSmiley", cParameter.getBeanProperty("showSmiley"))
				.add("count", comments(cParameter).getCount());
		return MVEL2Template.replace(variables, ICommentHandler.class, "Comment.html");
	}
}
