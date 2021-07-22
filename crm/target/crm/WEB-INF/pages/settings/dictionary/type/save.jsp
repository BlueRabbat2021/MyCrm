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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function () {
			// 验证编码不能为空，首先对编码文本框添加失去焦点(光标)事件
			$("#create-code").blur(function(){
				checkCode();
			});
			// 因为重复用到这里的验证，所以将其抽象为函数
			// 并且给这个函数设定返回值，当验证全部成功时返回true，否则返回false
			function checkCode(){
				// 获得用户输入的文本
				var code = $.trim($("#create-code").val())
				// code是主键，不能为空
				if(code==""){
					$("#codeMsg").text("编码不能为空");
					// 这里必须使用return false，一是因为下面还会对span进行操作，会被覆盖，二是因为需要返回一个不成功的结果
					return false;
				}else{
					$("#codeMsg").text("");
				}
				// 不仅需要前端验证用户是否输入，并且因为code是主键，所以需要后端验证输入的code是否重复
				//////////// 注意当使用ajax进行返回值时，要定义全局变量，不能在回调函数中直接return，因为函数的作用域不同，
				// 如果在回调函数中使用return，结束的只是回调函数，如果想要结束整个js，则需要定义全局变量，在回调函数外return
				var ref = false;
				$.ajax({
					// 这里写一个控制层地址去验证编码是否重复
					url:"settings/dictionary/type/checkCode.do",
					data:{
						code:code
					},
					// async是确定是否同步，默认为true即为异步请求
					async:false,
					type:"post",
					dataType:"json",
					success:function(data){
						if(data.code=="1"){
							$("#codeMsg").text("");
							ref = true;
						}else{
							$("#codeMsg").text(data.message);
							ref = false;
						}
					}
				});
				return ref;
			}
			// 对保存按钮添加单击事件，单击保存时要根据用户的输入信息验证保存
			$("#saveCreateDicTypeBtn").click(function(){
				// 得到用户的输入信息
				var code = $.trim($("#create-code").val());
				var name = $.trim($("#create-name").val());
				var description = $.trim($("#create-description").val());
				// 当单击保存时，首先要调用checkCode函数确认主键不能为空，并验证主键不重复
				// 通过以下代码验证可知，checkCode()函数的返回值一直为false，造成这种情况的原因是：
				// 在调用checkCode()函数时，里面使用了ajax，而当进行到ajax时，相当于新开了一个线程，ajax没有完全执行完成时，
				// 返回值ref默认为false，所以无论编码code字段是否符合要求，最后的返回值都是false，想要解决这个问题，需要在使用ajax时
				// 定义async值为false，也就是同步执行
				// alert(checkCode());
				if(checkCode()){ // checkCode()函数返回true表示主键验证符合要求
					$.ajax({
						url:"settings/dictionary/type/saveCreateDicType.do",
						data:{
							code:code,
							name:name,
							description:description
						},
						type:"post",
						dataType:"json",
						success:function (data){
							if(data.code=="0"){
								alert(data.message);
							}else{
								// 添加成功直接跳转到字典类型主页面，也就是数据显示页面
								window.location.href="settings/dictionary/type/index.do";
							}
						}
					});
				}
			});
		});
	</script>
</head>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>新增字典类型</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="saveCreateDicTypeBtn" type="button" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">
					
		<div class="form-group">
			<label for="create-code" class="col-sm-2 control-label">编码<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-code" style="width: 200%;">
				<span id="codeMsg" style="color: red"></span>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-name" class="col-sm-2 control-label">名称</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-name" style="width: 200%;">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-description" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 300px;">
				<textarea class="form-control" rows="3" id="create-description" style="width: 200%;"></textarea>
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>