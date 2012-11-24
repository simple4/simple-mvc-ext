package net.simpleframework.mvc.component.ext.attachments;

import static net.simpleframework.common.I18n.$m;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.simpleframework.common.ID;
import net.simpleframework.common.IoUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.bean.AttachmentFile;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.script.MVEL2Template;
import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.MVCContextFactory;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.swfupload.SwfUploadBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractAttachmentHandler extends ComponentHandlerEx implements
		IAttachmentHandler {
	@Override
	public void setSwfUploadBean(final SwfUploadBean swfUpload) {
		swfUpload.setFileSizeLimit("10MB").setMultiFileSelected(true);
	}

	@Override
	public Map<String, AttachmentFile> attachments(final ComponentParameter cParameter)
			throws IOException {
		return getUploadCache(cParameter);
	}

	@Override
	public AttachmentFile getAttachmentById(final ComponentParameter cParameter, final String id)
			throws IOException {
		return attachments(cParameter).get(id);
	}

	@Override
	public void doSave(final ComponentParameter cParameter, final IAttachmentSaveCallback callback) {
		callback.save(getUploadCache(cParameter), getDeleteCache(cParameter));
		// 清除
		clearCache(cParameter);

	}

	protected void clearCache(final ComponentParameter cParameter) {
		final Map<String, AttachmentFile> addQueue = getUploadCache(cParameter);
		addQueue.clear();
		final Set<String> deleteQueue = getDeleteCache(cParameter);
		if (deleteQueue != null) {
			deleteQueue.clear();
		}
	}

	@Override
	public void doDelete(final ComponentParameter cParameter, final String id) {
		if (getUploadCache(cParameter).remove(id) == null) {
			final Set<String> deleteQueue = getDeleteCache(cParameter);
			if (deleteQueue != null) {
				deleteQueue.add(id);
			}
		}
	}

	@Override
	public void upload(final ComponentParameter cParameter, final IMultipartFile multipartFile,
			final Map<String, Object> variables) throws IOException {
		final AttachmentFile af = new AttachmentFile(multipartFile.getFile());
		getUploadCache(cParameter).put(af.getId(), af);
	}

	@SuppressWarnings("unchecked")
	protected Map<String, AttachmentFile> getUploadCache(final ComponentParameter cParameter) {
		final ID ownerId = getOwnerId(cParameter);
		final String key = "Cache_"
				+ (ownerId == null ? cParameter.getBeanProperty("name") : ownerId);
		Map<String, AttachmentFile> cache = (Map<String, AttachmentFile>) cParameter
				.getSessionAttr(key);
		if (cache == null) {
			cParameter.setSessionAttr(key, cache = new LinkedHashMap<String, AttachmentFile>());
		}
		return cache;
	}

	@SuppressWarnings("unchecked")
	protected Set<String> getDeleteCache(final ComponentParameter cParameter) {
		final ID ownerId = getOwnerId(cParameter);
		Set<String> cache = null;
		if (ownerId != null) {
			final String key = "Delete_Cache_" + ownerId;
			cache = (Set<String>) cParameter.getSessionAttr(key);
			if (cache == null) {
				cParameter.setSessionAttr(key, cache = new LinkedHashSet<String>());
			}
		}
		return cache;
	}

	protected String getTempDir(final PageRequestResponse rRequest) {
		return MVCContextFactory.config().getTmpdir().getAbsolutePath() + File.separator;
	}

	@Override
	public String toAttachmentListHTML(final ComponentParameter cParameter) throws IOException {
		final String name = (String) cParameter.getBeanProperty("name");
		final boolean showInsert = StringUtils.hasText((String) cParameter
				.getBeanProperty("insertTextarea"));
		final boolean readonly = (Boolean) cParameter.getBeanProperty("readonly");
		final Set<String> deleteQueue = getDeleteCache(cParameter);
		final StringBuilder sb = new StringBuilder();
		for (final Map.Entry<String, AttachmentFile> entry : attachments(cParameter).entrySet()) {
			final String id = entry.getKey();
			final AttachmentFile attachment = entry.getValue();
			sb.append("<div class='fitem'>");
			sb.append("<div class='l_attach");
			if (!readonly) {
				if (getUploadCache(cParameter).containsKey(id)) {
					sb.append(" l_add' title='").append($m("DefaultAttachmentHandle.0"));
				} else {
					if (deleteQueue != null && deleteQueue.contains(id)) {
						sb.append(" l_delete' title='").append($m("DefaultAttachmentHandle.1"));
					}
				}
			}
			sb.append("'>");

			final StringBuilder size = new StringBuilder();
			size.append("<span class='size'>(").append(IoUtils.toFileSize(attachment.getSize()))
					.append(")</span>");
			sb.append("<table><tr>");
			if (showInsert) {
				sb.append("<td width='20px'><input type='checkbox' id='").append(id)
						.append("' /></td>");
				sb.append("<td><label for='").append(id).append("'>").append(attachment.getTopic())
						.append(size).append("</label>").append("</td>");
			} else {
				sb.append("<td><a onclick=\"$Actions['").append(name).append("_download']('id=");
				sb.append(id).append("');\">").append(attachment.getTopic()).append("</a>")
						.append(size).append("</td>");
			}
			sb.append("<td align='right'><span class='delete2_image' ").append("onclick=\"$Actions['")
					.append(name).append("_delete']('id=").append(id).append("');\"></span></td>");
			sb.append("</tr></table>");
			sb.append("</div></div>");
		}
		return sb.toString();
	}

	@Override
	public String toInsertHTML(final ComponentParameter cParameter) {
		final boolean showInsert = StringUtils.hasText((String) cParameter
				.getBeanProperty("insertTextarea"));
		if (!showInsert) {
			return "";
		}
		final KVMap variables = new KVMap().add("name", cParameter.getBeanProperty("name")).add(
				"beanId", cParameter.hashId());
		return MVEL2Template.replace(variables, IAttachmentHandler.class, "AttachmentInsert.html");
	}
}
