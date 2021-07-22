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
$(function(){
	// alert("ok");
	// 绑定单击事件
	$("#loginBtn").click(function (){
		// 获得用户输入信息
		// 使用trim()方法去掉前后空格，增加用户体验
		var loginAct = $.trim($("#loginAct").val());
		var loginPwd = $.trim($("#loginPwd").val());
		// 获得复选框中的选中状态
		var isRemPwd = $("#isRemPwd").prop("checked");
		/*alert(loginAct);
		alert(loginPwd);
		alert(isRemPwd);*/

		// 对用户输入信息进行验证，首先是前端验证，验证输入信息是否为空
		if(loginAct==""){
			alert("用户名不能为空")
			return;
		}
		if(loginPwd==""){
			alert("密码不能为空")
			return;
		}

		$.ajax({
			// url是要访问的资源文件地址，需要从后端控制层接收data中的数据，并对请求进行处理
			url:"settings/qx/user/login.do",
			data:{
				loginAct:loginAct,
				loginPwd:loginPwd,
				isRemPwd:isRemPwd
			},
			type:"post",
			dataType:"json",
			success:function(mydata){
				// alert(mydata);
				if(mydata.code=="1"){
					// 转到业务主页面
					window.location.href="workbench/index.do";
				}else{
					// alert(mydata.message);
					$("#msg").text(mydata.message);
				}
			}
		});
	});
});
</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>

	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" id="loginAct" value="${cookie.loginAct.value}" type="text" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" id="loginPwd" value="${cookie.loginPwd.value}" type="password" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">
								<input type="checkbox" id="isRemPwd" checked="true">
							</c:if>
							<c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">
								<input type="checkbox" id="isRemPwd">
							</c:if>
							十天内免登录
						</label>
						&nbsp;&nbsp;
						<span id="msg" style="color: red"></span>
					</div>
					<button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>