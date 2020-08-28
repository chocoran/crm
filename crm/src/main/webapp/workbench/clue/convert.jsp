<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%
    String id = request.getParameter("id");
    String owner = request.getParameter("owner");
    String company = request.getParameter("company");
    String appellation = request.getParameter("appellation");
    String fullname = request.getParameter("fullname");
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <script type="text/javascript">
        $(function () {
            $("#isCreateTransaction").click(function () {
                if (this.checked) {
                    $("#create-transaction2").show(200);
                } else {
                    $("#create-transaction2").hide(200);
                }
            });

            $(".time").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            $("#showBut").click(function () {
                $("#activityLike").val("");
                $("#searchActivityModal").modal("show");
                activityRShow();
            })

            $("#activityLike").change(function () {
                activityRShow();
            })

            $("#saveTran").click(function () {
                if ($("#isCreateTransaction").prop("checked")){
                    $("#booleanTran").val("true");
                    $("#saveForm").submit();
                }else {
                    window.location.href="workbench/clue/saveTran.do?booleanTran=false&clueId=" + "${param.id}";
                }

            })

            selectAll("stage");

        });

        function activityRShow() {
            $.ajax({
                url: "workbench/clue/showActivityForRelation.do",
                dataType: "json",
                type: "get",
                data: {
                    "id": "${param.id}",
                    "name": $.trim($("#activityLike").val())
                },
                success: function (data) {
                    let html = "";
                    $.each(data, function (k, v) {
                        html += '<tr>';
                        html += '<td><input type="radio" name="id" value="' + v.id + '" onclick="doSelect(\'' + v.id + '\',\'' + v.name + '\')"/></td>';
                        html += '<td>' + v.name + '</td>';
                        html += '<td>' + v.startDate + '</td>';
                        html += '<td>' + v.endDate + '</td>';
                        html += '<td>' + v.owner + '</td>';
                        html += '</tr>';
                    })
                    $("#activityShow").html(html);
                }
            })
        }

        function doSelect(id,name) {
            $("#searchActivityModal").modal("hide");
            $("#activity").val(name);
            $("#activity-id").val(id);
        }

        function selectAll(id) {
            $.ajax({
                url: "workbench/clue/getSelects.do?typeCode=" + id,
                dataType: "json",
                type: "get",
                success: function (data) {
                    let html = "<option></option>";
                    $.each(data, function (k, v) {
                        html += '<option value="' + v.value + '">' + v.text + '</option>';
                    })
                    $("." + id).html(html);
                }
            })

        }
    </script>

</head>
<body>

<!-- 搜索市场活动的模态窗口 -->
<div class="modal fade" id="searchActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">搜索市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" class="form-control" style="width: 300px;"
                                   placeholder="请输入市场活动名称，支持模糊查询" id="activityLike">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tbody id="activityShow">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<div id="title" class="page-header" style="position: relative; left: 20px;">
    <h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
</div>
<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
    新建客户：${param.company}
</div>
<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
    新建联系人：${param.fullname}${param.appellation}
</div>
<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
    <input type="checkbox" id="isCreateTransaction"/>
    为客户创建交易
</div>

<div id="create-transaction2"
     style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;">

    <form id="saveForm" action="workbench/clue/saveTran.do" method="post">
        <input type="hidden" id="booleanTran" name="booleanTran">
        <input type="hidden" id="clueId" name="clueId" value="${param.id}">
        <div class="form-group" style="width: 400px; position: relative; left: 20px;">
            <label for="amountOfMoney">金额</label>
            <input type="text" class="form-control" id="amountOfMoney" name="money">
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="tradeName">交易名称</label>
            <input type="text" class="form-control" id="tradeName" value="${param.company}-" name="name">
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="expectedClosingDate">预计成交日期</label>
            <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="stage">阶段</label>
            <select id="stage" class="form-control stage" name="stage">

            </select>
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="showBut" style="text-decoration: none;"><span
                    class="glyphicon glyphicon-search"></span></a></label>
            <input type="text" class="form-control" id="activity" placeholder="点击上面搜索" readonly>
            <input type="hidden" id="activity-id" name="activityId">
        </div>
    </form>

</div>

<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
    记录的所有者：<br>
    <b>${param.owner}</b>
</div>
<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
    <input class="btn btn-primary" type="button" id="saveTran" value="转换">
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input class="btn btn-default" type="button" value="取消">
</div>
</body>
</html>