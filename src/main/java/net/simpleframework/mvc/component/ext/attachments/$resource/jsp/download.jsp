<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.common.web.HttpUtils"%>
<%@ page import="net.simpleframework.common.IoUtils"%>
<%@ page import="net.simpleframework.common.StringUtils"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.io.FileInputStream"%>
<%
	try {
		final File oFile = new File(StringUtils.decodeHexString(request.getParameter("path")));
		OutputStream outputStream = HttpUtils.getBinaryOutputStream(request, response,
				request.getParameter("filename"), oFile.length());
		IoUtils.copyStream(new FileInputStream(oFile), outputStream);
	} finally {
		try {
			out.clear();
			out.clearBuffer();
			out = pageContext.pushBody();
		} catch (Throwable th) {
		}
	}
%>