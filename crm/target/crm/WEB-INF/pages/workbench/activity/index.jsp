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
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <!--  PAGINATION plugin -->
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<script type="text/javascript">
	// 页面展现按条件分页查询
	function queryActivityForPageByCondition(pageNo,pageSize){
		// alert("ok");
		var name = $("#query-name").val();
		var owner = $("#query-owner").val();
		var startDate = $("#query-startDate").val();
		var endDate = $("#query-endDate").val();

		$.ajax({
			url:"workbench/activity/queryActivityForPageByCondition.do",
			data:{
				pageNo:pageNo,
				pageSize:pageSize,
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate
			},
			type:"post",
			dataType:"json",
			success:function (data){
				// alert(data);
				var htmlStr = "";
				$.each(data.activityList,function (index,obj){
					htmlStr += "<tr class='active'>";
					// 复选框在选择时需要传递对象的id，用于指定某行记录
					htmlStr += "<td><input type='checkbox' value='" + obj.id + "'/></td>";
					htmlStr += "<td><a style='text-decoration: none; cursor: pointer;' onclick= window.location.href='workbench/activity/detail.do?id=" +obj.id+ "';>" + obj.name + "</a></td>";
					htmlStr += "<td>" + obj.owner + "</td>";
					htmlStr += "<td>" + obj.startDate + "</td>";
					htmlStr += "<td>" + obj.endDate + "</td>";
					htmlStr += "</tr>"
				});
				$("#tBody").html(htmlStr);

				// 计算总页数
				var totalPages = 1;
				if(data.totalRows%pageSize==0){
					totalPages = data.totalRows/pageSize;
				}else{
					// javaScript不会自动取整，所以要手动取整
					totalPages = parseInt(data.totalRows/pageSize) + 1;
				}
				// alert(totalPages);
				//显示翻页信息
				$("#demo_pag1").bs_pagination({
					currentPage:pageNo,//当前页
					rowsPerPage:pageSize,//每页显示条数
					totalRows:data.totalRows,//总条数
					totalPages: totalPages,//总页数
					visiblePageLinks:5,//显示的翻页卡片数
					showGoToPage:true,//是否显示"跳转到第几页"
					showRowsPerPage:true,//是否显示"每页显示条数"
					showRowsInfo:true,//是否显示"记录的信息"
					//每次切换页号都会自动触发此函数，函数能够返回切换之后的页号和每页显示条数
					onChangePage: function(e,pageObj) { // returns page_num and rows_per_page after a link has clicked
						//alert(pageObj.currentPage);
						//alert(pageObj.rowsPerPage);
						queryActivityForPageByCondition(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});
			}
		});
	}
	$(function(){
		// alert("ok");
		// 当容器加载完成，展现页面，按条件分页查询
		queryActivityForPageByCondition(1,10);
		//当容器加载完成，对容器调用工具函数
		$(".mydate").datetimepicker({
			language:'zh-CN',//语言
			format:'yyyy-mm-dd',//日期格式
			minView:'month',//日期选择器上最小能选择的日期的视图
			initialDate:new Date(),// 日历的初始化显示日期，此处默认初始化当前系统时间
			autoclose:true,//选择日期之后，是否自动关闭日历
			todayBtn:true,//是否显示当前日期的按钮
			clearBtn:true,//是否显示清空按钮
			// container: '#createActivityModal' container属性可以指定单个绑定，默认全部绑定
		});
		// 给查询按钮绑定单击事件
		$("#queryActivityBtn").click(function (){
			//显示所有符合条件的数据的第一页，默认每页显示10条
			queryActivityForPageByCondition(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
		});
		// 给创建按钮绑定事件
		$("#createActivityBtn").click(function (){
			// 重置创建市场活动的表单，每次打开前清空表单中的内容
			$("#activityForm")[0].reset();
			// 显示创建的模态窗口
			$("#createActivityModal").modal("show");
		});
		// 给创建模态窗口的保存按钮添加单击事件
		$("#saveCreateActivityBtn").click(function (){
			// alert("ok");
			// 收集参数
			var owner = $("#create-marketActivityOwner").val();
			var name = $.trim($("#create-marketActivityName").val());
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endDate").val();
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-description").val());

			// 表单验证
			if(owner == ""){
				alert("所有者不能为空");
				return;
			}
			if(name == ""){
				alert("活动名不能为空");
				return;
			}
			if(startDate != "" && endDate != ""){
				if(startDate > endDate) {
					alert("结束时间不能比开始时间小");
					return;
				}
			}
			var regExp = /^(([1-9]\d*)|0)$/;
			if(!regExp.test(cost)){
				alert("成本只能是非负整数");
				return;
			}

			// 发送请求
			$.ajax({
				url:"workbench/activity/saveCreateActivity.do",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:"post",
				dataType:"json",
				success:function (data){
					if(data.code="1"){
						// alert("ok");
						// 关闭模态窗口
						$("#createActivityModal").modal("hide");
						// 刷新市场活动列表
						queryActivityForPageByCondition(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						// alert("no");
						alert(data.message);
					}
				}
			});
		});
		// 给修改按钮添加单击事件
		$("#editActivityBtn").click(function (){
			// alert("ok");
			// alert($("#tBody input[type='checkbox']:checked").size());
			var checkBox = $("#tBody input[type='checkbox']:checked");
			if(checkBox.size() == 0){
				alert("您需要选中一条记录，才能进行修改");
				return;
			}
			if(checkBox.size() > 1){
				alert("您每次只能选中一条记录进行修改");
				return;
			}
			var activityId = checkBox[0].value;
			// 首先使用ajax把模态窗口中的表单添加数据
			$.ajax({
				url:'workbench/activity/editActivity.do',
				data: {
					id:activityId
				},
				type:'post',
				dataType:'json',
				success:function (data){
					// 将得到的数据更新到修改的模态窗口中
					$("#edit-id").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startDate").val(data.startDate);
					$("#edit-endDate").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-description").val(data.description);
				}
			});
			// 显示模态窗口
			$("#editActivityModal").modal("show");

		});
		// 给更新按钮添加单击事件
		$("#saveEditActivityBtn").click(function (){
			// 记录活动id的按钮在隐藏域中，用户看不见
			var id = $("#edit-id").val();
			var owner = $("#edit-marketActivityOwner").val();
			var name = $("#edit-marketActivityName").val();
			var startDate = $("#edit-startDate").val();
			var endDate = $("#edit-endDate").val();
			var cost = $("#edit-cost").val();
			var description = $("#edit-description").val();
			$.ajax({
				url:'workbench/activity/saveEditActivity.do',
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if(data.code == '1'){
						// 关闭模态窗口
						$("#editActivityModal").modal("hide");
						//刷新市场活动列表，保持页号和每页显示条数都不变
						queryActivityForPageByCondition($("#demo_pag1").bs_pagination('getOption', 'currentPage'),$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						alert(data.message);
						// 模态窗口不关闭，可以不写
						$("#editActivityModal").modal("show");
					}
				}
			});
		});
		// 全选全不选功能
		$("#chked_all").click(function (){
			// alert("ok");
			$("#tBody input[type='checkbox']").prop("checked",$("#chked_all").prop("checked"));
		});
		// on 可以将事件绑定到页面元素，这个页面元素可以还没有创建，以下的"input[type='checkbox']"是后端返回的数据
		$("#tBody").on("click","input[type='checkbox']",function () {
			if ( $("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
				$("#chked_all").prop("checked",true);
			}else{
				$("#chked_all").prop("checked",false);
			}
		});
		// 给删除按钮添加单击事件
		$("#deleteActivityBtn").click(function (){
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			if(checkedIds.length == 0){
				alert("您需要选中至少一条记录，才能进行删除");
				return;
			}
			var idsStr = "";
			$.each(checkedIds,function(){
				idsStr += "id=" + this.value + "&";
			});
			idsStr = idsStr.substr(0,idsStr.length-1);
			// alert(idsStr);
			if(window.confirm("确定删除么？")){
				$.ajax({
					url:'workbench/activity/deleteActivityByIds.do',
					data:idsStr,
					type:'post',
					dataType:'json',
					success:function (data){
						if(data.code == "1"){
							window.location.href="workbench/activity/index.do";
						}else{
							alert(data.message);
						}
					}
				});
			}
		});
		// 给导入按钮添加单击事件
		$("#importActivityListBtn").click(function (){
			$("#importActivityModal").modal("show");
		});
		// 给导入模态窗口中的导入按钮添加事件
		$("#importActivityBtn").click(function (){
			// alert("ok");
			// 验证上传文件是否符合要求
			// 验证是否为xls文件
			var fileName = $("#activityFile").val();
			suffix = fileName.substr(fileName.lastIndexOf(".") + 1).toUpperCase();
			// alert(suffix);
			if(suffix != 'XLS'){
				alert("上传的文件必须是Excel文件");
				return;
			}
			// 验证文件必须小于5M
			var file = $("#activityFile")[0].files[0]; // 第一个0表示得到dom对象，第二个0表示得到第一个文件
			if(file.size > 5*1024*1024){
				alert("上传的文件必须小于5M");
				return;
			}
			// 因为使用的是模态窗口，所以需要建立一个表单对象，用于提交数据
			var formdata = new FormData();
			// 将文件绑定
			formdata.append("file",file);
			// 表单对象也可以追加参数，在这里试验一下，与业务无关
			formdata.append("username","tom");
			$.ajax({
				url:'workbench/activity/importActivity.do',
				data:formdata,
				type:'post',
				dataType:'json',
				// processData属性默认为true，即将上传的表单的所有参数都按字符串处理，我们上传的是Excel文件，所以用false
				processData:false,
				// contentType属性默认为true，即将上传的的所有参数按照urlencoded编码，需要按流传递，所以置为false
				contentType:false,
				success:function(data){
					alert(data);
				}
			});
		});
		// 给批量导出按钮添加单击事件
		$("#exportActivityAllBtn").click(function (){
			// alert("ok");
			window.location.href="workbench/activity/exportAllActivity.do";
		});
		// 给选择导出按钮添加单击事件
		$("#exportActivityXzBtn").click(function (){
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			if(checkedIds.size() == 0){
				alert("请选择您要导出的记录");
				return;
			}
			var idsStr = "";
			$.each(checkedIds,function (){
				idsStr += "id=" + this.value + "&";
			});
			idsStr = idsStr.substr(0,idsStr.length-1);
			// alert(idsStr);
			window.location.href="workbench/activity/exportActivitySelective.do?" + idsStr;

		});
	});

</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form" id="activityForm">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-marketActivityName">
							</div>
						</div>

						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="create-startDate">
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="create-endDate">
							</div>
						</div>
						<div class="form-group">

							<label for="create-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-cost">
							</div>
						</div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveCreateActivityBtn" type="button" class="btn btn-primary">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="edit-startDate" value="2020-10-10">
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="edit-endDate" value="2020-10-20">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveEditActivityBtn" type="button" class="btn btn-primary">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 导入市场活动的模态窗口 -->
	<div class="modal fade" id="importActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
				</div>
				<div class="modal-body" style="height: 350px;">
					<div style="position: relative;top: 20px; left: 50px;">
						请选择要上传的文件：<small style="color: gray;">[仅支持.xls格式]</small>
					</div>
					<div style="position: relative;top: 40px; left: 50px;">
						<input type="file" id="activityFile">
					</div>
					<div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
						<h3>重要提示</h3>
						<ul>
							<li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
							<li>给定文件的第一行将视为字段名。</li>
							<li>请确认您的文件大小不超过5MB。</li>
							<li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
							<li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
							<li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
							<li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
						</ul>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
				</div>
			</div>
		</div>
	</div>


	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">名称</div>
							<input class="form-control" type="text" id="query-name">
						</div>
					</div>

					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">所有者</div>
							<input class="form-control" type="text" id="query-owner">
						</div>
					</div>


					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">开始日期</div>
							<input class="form-control mydate" type="text" id="query-startDate" />
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">结束日期</div>
							<input class="form-control mydate" type="text" id="query-endDate">
						</div>
					</div>

					<button id="queryActivityBtn" type="button" class="btn btn-default">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<button id="createActivityBtn" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 创建</button>
					<button id="editActivityBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
					<button id="deleteActivityBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
					<button id="importActivityListBtn" type="button" class="btn btn-default" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
					<button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
					<button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
				</div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
					<tr style="color: #B3B3B3;">
						<td><input type="checkbox" id="chked_all"/></td>
						<td>名称</td>
						<td>所有者</td>
						<td>开始日期</td>
						<td>结束日期</td>
					</tr>
					</thead>
					<tbody id="tBody">
					<%--<tr class="active">
                        <td><input type="checkbox" /></td>
                        <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                        <td>zhangsan</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                    </tr>
                    <tr class="active">
                        <td><input type="checkbox" /></td>
                        <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                        <td>zhangsan</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                    </tr>--%>
					</tbody>
				</table>
				<!--创建容器-->
				<div id="demo_pag1"></div>
			</div>
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>

		</div>

	</div>
</body>
</html>