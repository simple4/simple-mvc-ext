package net.simpleframework.mvc.component.ext.attachments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AttachmentUtils {

	public static final String BEAN_ID = "attachment_@bid";

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static void doSave(final ComponentParameter cParameter,
			final IAttachmentSaveCallback callback) {
		((IAttachmentHandler) cParameter.getComponentHandler()).doSave(cParameter, callback);
	}

	public static String getDownloadLoc(final String topic, final String absolutePath) {
		return new StringBuilder().append("$Actions.loc('")
				.append(ComponentUtils.getResourceHomePath(AttachmentBean.class))
				.append("/jsp/download.jsp?filename=").append(topic).append("&path=")
				.append(StringUtils.encodeHex(absolutePath.getBytes())).append("');").toString();
	}
}
