<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		// 加载函数
		$(function (){
			// 给新建按钮绑定事件
			$("#createDicValueBtn").click(function (){
				// alert("ok");
				window.location.href="settings/dictionary/value/toSaveValue.do";
			});
			// 给编辑按钮绑定事件
			$("#editDicValueBtn").click(function (){
				// 得到所有的被选中的复选框
				var checkedIds = $("#tBody input[type='checkbox']:checked");
				// 前端验证
				if(checkedIds.size() == 0){
					alert("您需要选中一条记录，才能进行编辑");
					return;
				}
				if(checkedIds.size() > 1){
					alert("您每次只能对一条记录进行编辑");
					return;
				}
				// 因为一次只能编辑一条记录，所以直接得到被编辑的value值
				var id = checkedIds[0].value;
				// 页面跳转
				window.location.href="settings/dictionary/value/editDicValue.do?id="+id;
			});
			// 全选全不选功能
			$("#checkedAll").click(function (){
				// alert($("#checkedAll").prop("checked"));
				$("#tBody input[type='checkbox']").prop("checked",$("#checkedAll").prop("checked"));
			});
			$("#tBody input[type='checkbox']").click(function (){
				if($("#tBody input[type='checkbox']:checked").size() == $("#tBody input[type='checkbox']").size()){
					$("#checkedAll").prop("checked",true);
				}else{
					$("#checkedAll").prop("checked",false);
				}
			});
			// 给删除按钮绑定事件
			$("#deleteDicValueBtn").click(function (){
				// alert("ok");
				var checkedIds = $("#tBody input[type='checkbox']:checked");
				if(checkedIds.length==0){
					alert("请至少选择一条记录，再进行删除操作");
					return;
				}
				var idsStr = "";
				$.each(checkedIds,function (){
					idsStr += "id=" + this.value + "&";
				});
				// alert(idsStr);
				idsStr = idsStr.substr(0,idsStr.length-1);
				// alert(idsStr);
				if(window.confirm("确定要删除么？")){
					$.ajax({
						url:'settings/dictionary/value/deleteDicValueByIds.do',
						data: idsStr,
						type:'post',
						dataType:'json',
						success:function (data){
							if(data.code=="1"){
								window.location.href="settings/dictionary/value/index.do";
							}else{
								alert(data.message);
							}
						}
					});
				}
			});
		});
	</script>
</head>
<body>

	<div>
		<div style="position: relative; left: 30px; top: -10px;">
			<div class="page-header">
				<h3>字典值列表</h3>
			</div>
		</div>
	</div>
	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
		<div class="btn-group" style="position: relative; top: 18%;">
		  <button id="createDicValueBtn" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 创建</button>
		  <button id="editDicValueBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
		  <button id="deleteDicValueBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	<div style="position: relative; left: 30px; top: 20px;">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td><input type="checkbox" id="checkedAll"/></td>
					<td>序号</td>
					<td>字典值</td>
					<td>文本</td>
					<td>排序号</td>
					<td>字典类型编码</td>
				</tr>
			</thead>
			<tbody id="tBody">
				<c:forEach items="${dicValueList}" var="dv" varStatus="vs">
					<c:if test="${vs.count%2==0}">
						<tr class="active">
					</c:if>
					<c:if test="${vs.count%2!=0}">
						<tr>
					</c:if>
						<td><input type="checkbox" value="${dv.id}"/></td>
						<td>${vs.count}</td>
						<td>${dv.value}</td>
						<td>${dv.text}</td>
						<td>${dv.orderNo}</td>
						<td>${dv.typeCode}</td>
					</tr>
				</c:forEach>
				<%--<tr class="active">
					<td><input type="checkbox" /></td>
					<td>1</td>
					<td>m</td>
					<td>男</td>
					<td>1</td>
					<td>sex</td>
				</tr>
				<tr>
					<td><input type="checkbox" /></td>
					<td>2</td>
					<td>f</td>
					<td>女</td>
					<td>2</td>
					<td>sex</td>
				</tr>
				<tr class="active">
					<td><input type="checkbox" /></td>
					<td>3</td>
					<td>1</td>
					<td>一级部门</td>
					<td>1</td>
					<td>orgType</td>
				</tr>
				<tr>
					<td><input type="checkbox" /></td>
					<td>4</td>
					<td>2</td>
					<td>二级部门</td>
					<td>2</td>
					<td>orgType</td>
				</tr>
				<tr class="active">
					<td><input type="checkbox" /></td>
					<td>5</td>
					<td>3</td>
					<td>三级部门</td>
					<td>3</td>
					<td>orgType</td>
				</tr>--%>
			</tbody>
		</table>
	</div>
	
</body>
</html>