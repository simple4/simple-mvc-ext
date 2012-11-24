package net.simpleframework.mvc.component.ext.comments;

import net.simpleframework.common.ID;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ICommentHandler extends IComponentHandler {

	/**
	 * 获取评论的拥有者id
	 * 
	 * @param cParameter
	 * @return
	 */
	ID getOwnerId(ComponentParameter cParameter);

	/**
	 * 获取评论的内容列表
	 * 
	 * @param cParameter
	 * @return
	 */
	IDataQuery<?> comments(ComponentParameter cParameter);

	/**
	 * 获取评论对象
	 * 
	 * @param cParameter
	 * @param id
	 * @return
	 */
	Object getCommentById(ComponentParameter cParameter, Object id);

	/**
	 * 发表评论
	 * 
	 * @param cParameter
	 * @return
	 */
	JavascriptForward addComment(ComponentParameter cParameter);

	/**
	 * 删除评论
	 * 
	 * @param cParameter
	 * @param id
	 * @return
	 */
	JavascriptForward deleteComment(ComponentParameter cParameter, Object id);

	static final String ATTRI_CONTENT = "content";

	static final String ATTRI_USERID = "userId";

	static final String ATTRI_CREATEDATE = "createDate";

	static final String ATTRI_ID = "id";

	static final String ATTRI_PARENTID = "parentId";

	/**
	 * 获取评论对象的相关属性
	 * 
	 * @param cParameter
	 * @param o
	 * @param name
	 * @return
	 */
	Object getProperty(ComponentParameter cParameter, Object o, String name);

	/**
	 * 获取评论的内容列表显示
	 * 
	 * @param cParameter
	 * @return
	 */
	String toCommentHTML(ComponentParameter cParameter);
}
