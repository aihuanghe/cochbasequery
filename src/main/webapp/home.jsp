<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    %>
    <meta charset="UTF-8">
    <title>切换CRM活动办理查询日期home</title>
    <link type="text/css" rel="stylesheet" href="<%=basePath%>resources/api/bootstrap/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="<%=basePath%>resources/api/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="<%=basePath%>resources/api/bootstrap/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/api/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/api/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/api/bootstrap/plugins/datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js"></script>
</head>
<body>
<input id="path" type="hidden" value="<%=basePath%>" />
<h1 style="text-align: center;">切换CRM活动办理查询日期</h1>
<div>
    <h1>${pageContext.servletContext.contextPath}</h1>
    <form class="form-horizontal" action="setDate" role="form">
        <div class="form-group">
            <label for="date" class="col-sm-2 control-label">切换模式</label>
            <div class="col-sm-10">
                <input id="type" class="form-control" style="width: 200px;" name="type" readonly>
            </div>
        </div>
        <div class="form-group">
            <label for="date" class="col-sm-2 control-label">选择日期</label>
            <div class="col-sm-10">
                <input id="date" class="form-control" style="width: 200px;" name="date" size="16" type="text" value="" readonly>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="button" onclick="setDate();"  class="btn btn-default">切换</button>
                <button type="button" onclick="getDate();" class="btn btn-default">查看当前切换日期</button>
                <button type="button" onclick="autoSwitch();" class="btn btn-default">自动切换日期</button>
            </div>
        </div>

    </form>
</div>
<script>
    var path = $("#path").val();
    $(function () {
        $("#date").datetimepicker({
            format: "yyyy-mm-dd",
            language: 'zh-CN',
            autoclose: true,
            todayBtn: true,
            minuteStep: 10,
            startView:2,
            minView:2,
            clearBtn:true //清除按钮
        });
        $.get({
            url:path+'/getDate',
            dataType:'json',
            success:function (data) {
                console.log(data);
                var msg;
                if (data.autoSwitch=="true"){
                    msg = "自动切换";
                }else{
                    msg = "手动切换";
                }
                $("#type").val(msg);
                var date = data.date;
                $('#date').datetimepicker('update', date);

            }
        })
    });

    function setDate() {
        $.get({
            url:path + '/setDate',
            data: {'date':$("#date").val()},
            success:function (data) {
                $("#type").val("手动切换");
                alert("手动切换时间为："+ data);
            }
        })
    }
    function getDate() {
        $.get({
            url:path + '/getDate',
            dataType:'json',
            success:function (data) {
                console.log(data);
                var msg;
                if (data.autoSwitch=="true"){
                    msg = "自动切换";
                }else{
                    msg = "手动切换";
                }
                var date = data.date;
                alert("切换模式："+msg+",手动切换时间为："+ date);
            }
        })

    }

    function autoSwitch() {
        $.get({
            url:path + '/autoSwitch',
            success:function (data) {
                if(data){
                    alert("切换模式：自动切换成功");
                    $("#type").val("自动切换");
                }else{
                    alert("切换模式：自动切换失败");
                }

            }
        })
    }

</script>
</body>
</html>
