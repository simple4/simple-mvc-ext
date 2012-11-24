package net.simpleframework.mvc.component.ext.comments;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.html.HtmlEncoder;
import net.simpleframework.common.html.HtmlUtils;
import net.simpleframework.common.html.element.SpanElement;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.MVCContextFactory;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.EWarnType;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.dictionary.SmileyUtils;
import net.simpleframework.mvc.component.ui.pager.AbstractPagerHandler;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.PagerBean;
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CommentLoaded extends DefaultPageHandler {

	@Override
	public void beforeComponentRender(final PageParameter pParameter) {
		super.beforeComponentRender(pParameter);

		final ComponentParameter cParameter = CommentUtils.get(pParameter);
		final CommentBean commentBean = (CommentBean) cParameter.componentBean;
		final String commentName = (String) cParameter.getBeanProperty("name");
		final String hashId = cParameter.hashId();

		// 提交与验证
		pParameter.addComponentBean(commentName + "_validation", ValidationBean.class)
				.setTriggerSelector("#submit_" + hashId).setWarnType(EWarnType.insertAfter)
				.addValidators(new Validator(EValidatorMethod.required, "#textarea_" + hashId));
		pParameter.addComponentBean(commentName + "_submit", AjaxRequestBean.class)
				.setConfirmMessage($m("Confirm.Post")).setHandleMethod("addComment")
				.setHandleClass(CommentAction.class).setAttr("$comment", commentBean);

		if ((Boolean) cParameter.getBeanProperty("showSmiley")) {
			// 表情
			pParameter.addComponentBean(commentName + "_smiley", DictionaryBean.class)
					.setBindingId("textarea_" + hashId).addSmiley(pParameter);
		}

		final String role = (String) cParameter.getBeanProperty("role");
		final IPagePermissionHandler permission = MVCContextFactory.permission();
		final boolean mgr = permission.isMember(permission.getLoginId(cParameter), role);
		if (mgr) {
			pParameter.addComponentBean(commentName + "_delete", AjaxRequestBean.class)
					.setConfirmMessage($m("Confirm.Delete")).setHandleMethod("doDelete")
					.setHandleClass(CommentAction.class).setAttr("$comment", commentBean);
		}

		// pager
		pParameter.addComponentBean(commentName + "_pager", PagerBean.class)
				.setPagerBarLayout(EPagerBarLayout.bottom).setContainerId("pager_" + hashId)
				.setHandleClass(CommentList.class).setAttr("$comment", commentBean)
				.setAttr("$role", mgr);
	}

	public static class CommentAction extends DefaultAjaxRequestHandler {

		public IForward addComment(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$comment");
			return ((ICommentHandler) nComponentParameter.getComponentHandler())
					.addComment(nComponentParameter);
		}

		public IForward doDelete(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$comment");
			return ((ICommentHandler) nComponentParameter.getComponentHandler()).deleteComment(
					nComponentParameter, nComponentParameter.getParameter("id"));
		}
	}

	public static class CommentList extends AbstractPagerHandler {

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$comment");
			return ((ICommentHandler) nComponentParameter.getComponentHandler())
					.getFormParameters(nComponentParameter);
		}

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$comment");
			return ((ICommentHandler) nComponentParameter.getComponentHandler())
					.comments(nComponentParameter);
		}

		private String replace(String content) {
			content = HtmlEncoder.text(content);
			content = HtmlUtils.stripScripts(content);
			content = SmileyUtils.replaceSmiley(content);
			content = HtmlUtils.convertHtmlLines(content);
			content = HtmlUtils.autoLink(content);
			return content;
		}

		@Override
		public String toPagerHTML(final ComponentParameter cParameter, final List<?> data) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$comment");
			final boolean mgr = (Boolean) cParameter.componentBean.getAttr("$role");
			final String componentName = (String) nComponentParameter.getBeanProperty("name");
			final ICommentHandler hdl = (ICommentHandler) nComponentParameter.getComponentHandler();
			final IPagePermissionHandler permission = MVCContextFactory.permission();
			final StringBuilder sb = new StringBuilder();
			for (final Object o : data) {
				final Object id = hdl.getProperty(nComponentParameter, o, ICommentHandler.ATTRI_ID);
				final String content = Convert.toString(hdl.getProperty(nComponentParameter, o,
						ICommentHandler.ATTRI_CONTENT));
				final Date createDate = (Date) hdl.getProperty(nComponentParameter, o,
						ICommentHandler.ATTRI_CREATEDATE);
				final Object userId = hdl.getProperty(nComponentParameter, o,
						ICommentHandler.ATTRI_USERID);
				sb.append("<div class='oitem'><table><tr>");
				sb.append("<td class='icon'>");
				sb.append("<img class='photo_icon' src='")
						.append(permission.getPhotoUrl(nComponentParameter, userId)).append("' />");
				final Object oUser = permission.getUser(userId);
				sb.append("<div class='icon_d'>").append(oUser).append("</div>");
				sb.append("</td><td>");
				final Object p = hdl.getCommentById(cParameter,
						hdl.getProperty(nComponentParameter, o, ICommentHandler.ATTRI_PARENTID));
				if (p != null) {
					final String reply = (String) hdl.getProperty(nComponentParameter, p,
							ICommentHandler.ATTRI_CONTENT);
					sb.append("<div class='rc'>");
					final Object userId2 = hdl.getProperty(nComponentParameter, p,
							ICommentHandler.ATTRI_USERID);
					final Date createDate2 = (Date) hdl.getProperty(nComponentParameter, p,
							ICommentHandler.ATTRI_CREATEDATE);
					sb.append("<div class='r_desc'>");
					sb.append(Convert.toDateString(createDate2)).append(SpanElement.SEP)
							.append(permission.getUser(userId2));
					sb.append("</div>");
					sb.append(replace(reply));
					sb.append("</div>");
				}
				sb.append("<div class='mc'>").append(replace(content)).append("</div>");
				sb.append("<div class='desc'>");
				sb.append(Convert.toDateString(createDate));
				if (mgr) {
					sb.append(SpanElement.SEP).append("<a onclick=\"$Actions['").append(componentName)
							.append("_delete']('id=").append(id).append("');\">").append($m("Delete"))
							.append("</a>");
				}
				sb.append(SpanElement.SEP).append("<a onclick=\"$comment_reply_click('").append(id)
						.append("', '").append(oUser).append("');\">").append($m("CommentList.0"))
						.append("</a>");
				sb.append("</div>");
				sb.append("</td>");
				sb.append("</tr></table></div>");
			}
			return sb.toString();
		}
	}
}
