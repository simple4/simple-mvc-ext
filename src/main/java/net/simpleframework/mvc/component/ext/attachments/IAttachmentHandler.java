package net.simpleframework.mvc.component.ext.attachments;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.bean.AttachmentFile;
import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;
import net.simpleframework.mvc.component.ui.swfupload.SwfUploadBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IAttachmentHandler extends IComponentHandler {

	/**
	 * 获取附件拥有者的id
	 * 
	 * @param cParameter
	 * @return
	 */
	ID getOwnerId(ComponentParameter cParameter);

	/**
	 * 获取附件列表
	 * 
	 * @param cParameter
	 * @return
	 */
	Map<String, AttachmentFile> attachments(ComponentParameter cParameter) throws IOException;

	/**
	 * 根据id获取AttachmentFile对象
	 * 
	 * @param cParameter
	 * @param id
	 * @return
	 * @throws IOException
	 */
	AttachmentFile getAttachmentById(ComponentParameter cParameter, String id) throws IOException;

	/**
	 * 上传执行操作
	 * 
	 * @param cParameter
	 * @param multipartFile
	 * @param variables
	 */
	void upload(ComponentParameter cParameter, IMultipartFile multipartFile,
			Map<String, Object> variables) throws IOException;

	/**
	 * 插入附件
	 * 
	 * @param cParameter
	 * @param callback
	 */
	void doSave(ComponentParameter cParameter, IAttachmentSaveCallback callback);

	/**
	 * 删除上传的附件
	 * 
	 * @param cParameter
	 * @param id
	 */
	void doDelete(ComponentParameter cParameter, String id);

	/**
	 * 获取下载的javascript 放在onclick下
	 * 
	 * @param id
	 * @return
	 */
	String getDownloadAction(String id);

	/**
	 * 设置上传组件的属性
	 * 
	 * @param swfUpload
	 */
	void setSwfUploadBean(SwfUploadBean swfUpload);

	/**
	 * 附件列表输出HTML
	 * 
	 * @param cParameter
	 * @return
	 */
	String toAttachmentListHTML(ComponentParameter cParameter) throws IOException;

	/**
	 * 获取插入操作的HTML
	 * 
	 * @param cParameter
	 * @return
	 */
	String toInsertHTML(ComponentParameter cParameter);
}
