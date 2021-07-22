<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <base href="<%=basePath%>">
    <title>演示bs_typeahead插件</title>
    <script type="text/javascript">
       /* $(function () {
            //当容器加载完成，对容器调用工具函数
            var name2id={};
            $("#customerName").typeahead({
                //source:['阿里巴巴','京东商城','腾讯','动力节点','字节跳动']
                source:function (query,process) {
                    // 每次键盘弹起，都会执行本函数；可以在函数中生成一个数据源(字符串数组)，交给source参数使用；
                    // source每次拿到数据源(字符串数组)，都会根据关键字比对,比对成功的字符串都会列容器下方，从而实现自动补全。
                    // query：是用户在容器中输入的关键字
                    // process：是一个bs_typeahead插件提供的函数，能够把一个json的字符串数组交给source使用。
                    // 向后台发送异步请求，查询客户名称组成字符串数组，以json字符串的形式返回，交给source使用
                    $.ajax({
                        url:'workbench/transaction/typeahead.do',
                        data:{
                            customerName:query
                        },
                        type:'post',
                        dataType:'json',
                        success:function (data) {//data就是json的字符串数组
                            var customerNameArr=[];
                            //遍历data复杂类型数组
                            $.each(data,function (index,obj) {
                                //生成简单类型数组
                                customerNameArr.push(obj.name);
                                //把obj的name和id赋值给name2id，把name作为name2id属性名，id作为name2id的属性值
                                name2id[obj.name]=obj.id;
                            });
                            process(customerNameArr);
                        }
                    });
                },
                afterSelect:function (item) {//用户选中一项之后，自动触发本函数；
                                             //item：选中项，补全之后的名字
                    alert(name2id[item]);
                }
            });
        });*/
       $(function(){
           // 定义一个变量存储json数组
           var name2id = {};
           $("#customerName").typeahead({
               // typeahead函数中有一个source属性，可以设定自动补全的内容
               // source后面加上数据源(字符串数组)，当用户在文本域中输入时，source会根据用户输入的关键字与数据源做对比，将对比
               // 成功的字符串罗列在容器下方，从而实现自动补全
               // source: ['阿里巴巴','动力节点','字节跳动','国庆节']
               // 上面写死了，我们需要从后端获取到数据
               // query：表示在文本域中输入的值，内部带有keyup事件
               // process：是bs.typeahead插件提供的函数，可以把json字符串数组交给source使用
               source:function (query,process){
                   // 向后台发送异步请求，查询客户名称组成的字符串数组，以json字符串的形式返回，交给source使用
                   $.ajax({
                       url:'workbench/transaction/typeahead.do',
                       data:{
                           customerName:query
                       },
                       type:'post',
                       dataType:'json',
                       success:function (data){
                           // alert(data);
                           var customerNameArr = [];
                           $.each(data,function (index,obj){
                               // 在js中往数组中添加元素使用push方法
                               customerNameArr.push(obj.name);
                               // 把obj的name和id值赋值给name2id，obj.name作为属性名，obj.id作为属性值
                               name2id[obj.name] = obj.id;
                           });
                           process(customerNameArr);
                       }
                   });
               },
               afterSelect:function (item){ // 用户选择容器下面自动补全的数据后自动触发此函数
                   // item就是被选中的名字
                   alert(name2id[item]); // 通过这种方式可以得到选中项对应的id
               }
           });
       })
    </script>
</head>
<body>
<input type="text" id="customerName">
</body>
</html>
