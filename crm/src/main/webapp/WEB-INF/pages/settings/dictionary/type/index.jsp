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
		$(function () {
			// alert("ok");


			// 给新建按钮绑定事件
			$("#createDicTypeBtn").click(function(){
				// alert("ok");
				// 发送请求
				window.location.href="settings/dictionary/type/toSave.do";
			});



			// 给编辑按钮绑定事件
			$("#editDicTypeBtn").click(function(){
				// 获得除全选外所有的被选中的复选框
				// alert($("#tBody input[type='checkbox']:checked").size());
				var chkedCodes = $("#tBody input[type='checkbox']:checked");
				// 对被选中的复选框个数进行验证
				if(chkedCodes.size()==0){
					alert("您需要选中一条记录，才能进行编辑操作");
					return;
				}
				if(chkedCodes.length>1){
					alert("每次只能编辑一条记录");
					return;
				}
				// 程序走到这里，说明只选择了一条记录
				// 现在需要将该记录的编码传递到后端
				// 首先获得记录的编码
				var code = chkedCodes[0].value;
				// alert(code)
				// 因为没有返回的数据，也不需要对返回的数据进行处理，所以不需要使用ajax，只需要进行页面跳转就好
				// 有一个注意的点是：因为要传递参数，所以需要在地址后面添加参数
				window.location.href="settings/dictionary/type/editDicType.do?code="+code;
			});


			// 给删除按钮绑定事件,因为是批量删除，所有有全选和全不选的功能，
			// 首先实现全选全不选的功能
			// 给全选按钮绑定事件
			$("#chkedAll").click(function(){
				$("#tBody input[type='checkbox']").prop("checked",$("#chkedAll").prop("checked"));
			});
			// 给除全选外其他所有的复选框绑定事件
			$("#tBody input[type='checkbox']").click(function(){
				// 如果其他复选框的个数等于被选中个数，将全选框选中
				if($("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
					$("#chkedAll").prop("checked",true);
				}else{
					$("#chkedAll").prop("checked",false);
				}
			});



			// 给删除按钮绑定单击事件
			$("#deleteDicTypeBtn").click(function() {
				var chkedCodes = $("#tBody input[type='checkbox']:checked");
				if (chkedCodes.size() == 0) {
					alert("必须选中至少一条记录才能使用删除功能")
					return;
				}
				// 程序走到这里说明至少选中了一条记录
				// 因为这里得到的chkedCodes数组中有重名的参数，所以可以使用字符串的形式在地址后传递
				var codeStr = "";
				$.each(chkedCodes, function () {
					// this表示每次从chkedCodes数组中取出的复选框对象
					codeStr += "code=" + this.value + "&";
				})
				// alert(codeStr);
				codeStr = codeStr.substr(0, codeStr.length - 1);
				// alert(codeStr);
				if (window.confirm("确定删除么?")) {
					// alert("ok");
					$.ajax({
						url: "settings/dictionary/type/deleteDicTypeByCodes.do",
						data: codeStr,
						type: "post",
						dataType: 'json',
						success: function (data) {
							if (data.code == "1") {
								window.location.href = "settings/dictionary/type/index.do";
							} else {
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
				<h3>字典类型列表</h3>
			</div>
		</div>
	</div>
	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
		<div class="btn-group" style="position: relative; top: 18%;">
		  <button id="createDicTypeBtn" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 创建</button>
		  <button id="editDicTypeBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
		  <button id="deleteDicTypeBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	<div style="position: relative; left: 30px; top: 20px;">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td><input type="checkbox" id="chkedAll"/></td>
					<td>序号</td>
					<td>编码</td>
					<td>名称</td>
					<td>描述</td>
				</tr>
			</thead>
			<tbody id="tBody">
			<%--使用jstl也就是jsp的基本标签库从后端获得数据--%>
			<%--使用之前一定要记得导入<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
			<%--
				var属性也就是dicTypeList中的元素，可以使用"dt."的形式获得元素中的内容
				varStatus属性可以作为记录的序号，好处是不用将序号保存至数据库，因为如果序号保存在数据库，删除中
				间记录时会出现序号不连续的情况
			--%>
			<!--从request获取dicTypeList，遍历dicTypeList，显示所有的数据-->
			<c:forEach items="${dicTypeList}" var="dt" varStatus="vs">
				<%--为了增加用户体验，可以针对不同行设置不同的背景颜色--%>
				<c:if test="${vs.count%2!=0}">
					<tr class="active">
						<%---这里的输入域的value属性是用来接收用户是否选中的，如果选中就会获得该记录的编码，用于以后验证--%>
						<td><input type="checkbox" value="${dt.code}"></td>
						<%--这是专门用来排序的--%>
						<td>${vs.count}</td>
						<td>${dt.code}</td>
						<td>${dt.name}</td>
						<td>${dt.description}</td>
					</tr>
				</c:if>
				<c:if test="${vs.count%2==0}">
					<tr>
							<%---这里的输入域的value属性是用来接收用户是否选中的，如果选中就会获得该记录的编码，用于以后验证--%>
						<td><input type="checkbox" value="${dt.code}"></td>
							<%--这是专门用来排序的--%>
						<td>${vs.count}</td>
						<td>${dt.code}</td>
						<td>${dt.name}</td>
						<td>${dt.description}</td>
					</tr>
				</c:if>
			</c:forEach>
			</tbody>
		</table>
	</div>
	
</body>
</html>