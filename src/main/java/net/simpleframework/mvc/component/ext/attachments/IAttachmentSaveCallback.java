package net.simpleframework.mvc.component.ext.attachments;

import java.util.Map;
import java.util.Set;

import net.simpleframework.common.bean.AttachmentFile;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IAttachmentSaveCallback {

	/**
	 * 
	 * @param addQueue
	 * @param deleteQueue
	 */
	void save(Map<String, AttachmentFile> addQueue, Set<String> deleteQueue);
}
