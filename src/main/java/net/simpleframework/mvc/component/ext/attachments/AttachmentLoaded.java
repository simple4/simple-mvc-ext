package net.simpleframework.mvc.component.ext.attachments;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.bean.AttachmentFile;
import net.simpleframework.common.html.js.JavascriptUtils;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.TextForward;
import net.simpleframework.mvc.component.ComponentHandleException;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.ui.swfupload.AbstractSwfUploadHandler;
import net.simpleframework.mvc.component.ui.swfupload.SwfUploadBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AttachmentLoaded extends DefaultPageHandler {

	@Override
	public void beforeComponentRender(final PageParameter pParameter) {
		super.beforeComponentRender(pParameter);

		final ComponentParameter cParameter = AttachmentUtils.get(pParameter);
		final AttachmentBean attachmentBean = (AttachmentBean) cParameter.componentBean;
		final String beanId = attachmentBean.hashId();
		final String attachmentName = (String) cParameter.getBeanProperty("name");

		SwfUploadBean swfUpload = null;
		final boolean readonly = (Boolean) cParameter.getBeanProperty("readonly");
		if (!readonly) {
			swfUpload = (SwfUploadBean) pParameter
					.addComponentBean(attachmentName + "_swfUpload", SwfUploadBean.class)
					.setJsCompleteCallback("$Actions['" + attachmentName + "_list']();")
					.setContainerId("attachment_" + attachmentBean.hashId())
					.setHandleClass(SwfUploadAction.class).setAttr("$attachment", attachmentBean);
			((IAttachmentHandler) cParameter.getComponentHandler()).setSwfUploadBean(swfUpload);
		}

		// 附件列表
		pParameter.addComponentBean(attachmentName + "_list", AjaxRequestBean.class)
				.setUpdateContainerId("attachment_list_" + beanId).setHandleMethod("doList")
				.setHandleClass(AttachmentAction.class).setAttr("$attachment", attachmentBean)
				.setAttr("$swfupload", swfUpload);

		// 删除附件条目
		pParameter.addComponentBean(attachmentName + "_delete", AjaxRequestBean.class)
				.setHandleMethod("doDelete").setHandleClass(AttachmentAction.class)
				.setAttr("$attachment", attachmentBean).setAttr("$swfupload", swfUpload);

		// 下载
		pParameter.addComponentBean(attachmentName + "_download", AjaxRequestBean.class)
				.setHandleMethod("doDownload").setHandleClass(AttachmentAction.class)
				.setAttr("$attachment", attachmentBean).setAttr("$swfupload", swfUpload);

		// 选取
		final String insertTextarea = (String) cParameter.getBeanProperty("insertTextarea");
		if (StringUtils.hasText(insertTextarea)) {
			pParameter.addComponentBean(attachmentName + "_selected", AjaxRequestBean.class)
					.setHandleMethod("doSelect").setHandleClass(AttachmentAction.class)
					.setAttr("$attachment", attachmentBean).setAttr("$swfupload", swfUpload);
		}
	}

	public static class SwfUploadAction extends AbstractSwfUploadHandler {

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$attachment");
			return ((IAttachmentHandler) nComponentParameter.getComponentHandler())
					.getFormParameters(nComponentParameter);
		}

		@Override
		public void upload(final ComponentParameter cParameter, final IMultipartFile multipartFile,
				final Map<String, Object> variables) throws IOException {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$attachment");
			((IAttachmentHandler) nComponentParameter.getComponentHandler()).upload(
					nComponentParameter, multipartFile, variables);
		}
	}

	public static class AttachmentAction extends DefaultAjaxRequestHandler {

		@Override
		public Object getBeanProperty(final ComponentParameter cParameter, final String beanProperty) {
			if ("selector".equals(beanProperty)) {
				final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(
						cParameter, "$swfupload");
				if (nComponentParameter.componentBean != null) {
					return nComponentParameter.getBeanProperty("selector");
				}
			}
			return super.getBeanProperty(cParameter, beanProperty);
		}

		public IForward doDelete(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$attachment");
			final String attachmentName = (String) nComponentParameter.getBeanProperty("name");

			((IAttachmentHandler) nComponentParameter.getComponentHandler()).doDelete(
					nComponentParameter, nComponentParameter.getParameter("id"));
			return new JavascriptForward("$Actions['" + attachmentName + "_list']();");
		}

		public IForward doList(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$attachment");
			try {
				return new TextForward(
						((IAttachmentHandler) nComponentParameter.getComponentHandler())
								.toAttachmentListHTML(nComponentParameter));
			} catch (final IOException e) {
				throw ComponentHandleException.of(e);
			}
		}

		public IForward doDownload(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$attachment");
			final JavascriptForward js = new JavascriptForward();
			try {
				final AttachmentFile af = ((IAttachmentHandler) nComponentParameter
						.getComponentHandler()).getAttachmentById(nComponentParameter,
						nComponentParameter.getParameter("id"));
				if (af != null) {
					js.append(AttachmentUtils.getDownloadLoc(af.getTopic(), af.getAttachment()
							.getAbsolutePath()));
				} else {
					js.append("alert(\"").append($m("AttachmentLoaded.0")).append("\");");
				}
			} catch (final IOException e) {
				throw ComponentHandleException.of(e);
			}
			return js;
		}

		public IForward doSelect(final ComponentParameter cParameter) {
			final ComponentParameter nComponentParameter = ComponentParameter.getByAttri(cParameter,
					"$attachment");
			final JavascriptForward js = new JavascriptForward();
			final String insertTextarea = (String) nComponentParameter
					.getBeanProperty("insertTextarea");
			final String[] idArr = StringUtils.split(nComponentParameter.getParameter("ids"), ";");
			final StringBuilder sb = new StringBuilder();
			if (idArr != null) {
				try {
					final IAttachmentHandler attachmentHdl = (IAttachmentHandler) nComponentParameter
							.getComponentHandler();
					for (final String id : idArr) {
						final AttachmentFile af = attachmentHdl
								.getAttachmentById(nComponentParameter, id);
						sb.append("<a style='line-height:21px;' href='javascript:void(0);' onclick=\"")
								.append(attachmentHdl.getDownloadAction(id)).append("\">")
								.append(af.getTopic()).append("</a><br />");
					}
				} catch (final IOException e) {
					throw ComponentHandleException.of(e);
				}
			}
			js.append("$win($Actions['").append(cParameter.getBeanProperty("name"))
					.append("'].trigger).close();");
			js.append("$Actions.setValue(\"").append(insertTextarea).append("\", \"")
					.append(JavascriptUtils.escape(sb.toString())).append("\", true);");
			return js;
		}
	}
}
