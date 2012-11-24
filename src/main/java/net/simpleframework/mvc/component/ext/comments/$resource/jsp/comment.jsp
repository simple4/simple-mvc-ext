<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ext.comments.CommentUtils"%>
<%@ page import="net.simpleframework.mvc.component.ext.comments.ICommentHandler"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%
	final ComponentParameter cParameter = CommentUtils.get(request,
	response);
	final ICommentHandler hdl = (ICommentHandler) cParameter
	.getComponentHandler();
%>
<div class="Comp_Comment">
  <%=ComponentRenderUtils.genParameters(cParameter)%>
  <%=hdl.toCommentHTML(cParameter)%>
</div>
