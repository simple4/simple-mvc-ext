<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ext.category.CategoryUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%@ page import="net.simpleframework.common.Convert"%>
<%
	final ComponentParameter nComponentParameter = CategoryUtils.get(
			request, response);
	final String beanId = nComponentParameter.hashId();
	final String categoryName = (String) nComponentParameter
			.getBeanProperty("name");
	final boolean runImmediately = Convert.toBool(nComponentParameter
			.getBeanProperty("runImmediately"));
%>
<div class="Category">
  <%=ComponentRenderUtils.genParameters(nComponentParameter)%>
  <div id="category_<%=beanId%>"></div>
</div>
<script type="text/javascript">

  function $category_action(item) {
  	item = $Target(item); 
  	var act = (item.hasClassName("Category") ? item : item.up(".Category")).action;
  	act.currentBranch = 
  		(item.hasClassName("tafelTreecontent") ? item : item.up(".tafelTreecontent")).branch;
   	return act;
  }
  
	$ready(function() {
		var action = $Actions["<%=categoryName%>"];
		<%=ComponentRenderUtils.genJSON(nComponentParameter, "action")%>
		action.selector = 
      "<%=nComponentParameter.getBeanProperty("selector")%>";
      
   	action.treeAction = $Actions["<%=categoryName%>_tree"];
   	action.treeAction.jsLoadedCallback = action.jsLoadedCallback;
    <% if (runImmediately) { %>action.treeAction();<% } %>
   	
		var ele = $("category_<%=beanId%>").up(".Category");
		ele.action = action;
		
		$category_addMethods(action, "<%=categoryName%>");
	});
</script>