<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery\bs_typeahead\bootstrap3-typeahead.min.js"></script>


    <script type="text/javascript">
        $(function () {
            $(".time").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            $("#customerName").typeahead({
                source: function (query, process) {
                    $.get(
                        "workbench/tran/getCustomerName.do",
                        { "customerName" : query },
                        function (data) {
                            //alert(data);
                            process(data);
                        },
                        "json"
                    );
                },
                delay: 1
            });


            $.ajax({
                url: "workbench/tran/getUser.do",
                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                dataType: "json",
                type: "get",
                success: function (data) {
                    let html = "";
                    $.each(data,function (k,v) {
                        html += '<option value="' + v.name + '">' + v.name + '</option>';
                    })
                    $("#owner").html(html);
                    $("#owner").val("${sessionScope.user.name}");
                }
            })

            $("#showActivity").click(function () {
                $("#findMarketActivity").modal("show");
                $("#activityLike").val("");
                showA();
            })

            $("#showContacts").click(function () {
                $("#findContacts").modal("show");
                $("#contactsLike").val("");
                showC();
            })

            $("#activityLike").change(function () {
                showA();
            })

            $("#contactsLike").change(function () {
                showC();
            })

            $("#stage").change(function () {
                $.ajax({
                    url: "workbench/tran/getPossibility.do",
                    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                    dataType: "json",
                    type: "get",
                    data: {
                        "stage": $.trim($("#stage").val())
                    },
                    success: function (data) {
                        $("#possibility").val(data);
                    }
                })
            })

            $("#saveBut").click(function () {
                $("#saveForm").submit();
            })

        })

        function addActivity(id, name) {
            $("#findMarketActivity").modal("hide");
            $("#activityName").val(name);
            $("#activityId").val(id);
        }

        function addContacts(id, name) {
            $("#findContacts").modal("hide");
            $("#contactsName").val(name);
            $("#contactsId").val(id);
        }

        function showA() {
            $.ajax({
                url: "workbench/tran/getActivity.do",
                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                dataType: "json",
                type: "get",
                data: {
                    "activityLike": $.trim($("#activityLike").val())
                },
                success: function (data) {
                    let html = "";
                    $.each(data, function (k, v) {
                        html += '<tr>';
                        html += '<td><input type="radio" name="activity" onclick="addActivity(\'' + v.id + '\',\'' + v.name + '\')"/></td>';
                        html += '<td>' + v.name + '</td>';
                        html += '<td>' + v.startDate + '</td>';
                        html += '<td>' + v.endDate + '</td>';
                        html += '<td>' + v.owner + '</td>';
                        html += '</tr>';
                    })
                    $("#showABody").html(html);
                }
            })
        }

        function showC() {
            $.ajax({
                url: "workbench/tran/getContacts.do",
                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                dataType: "json",
                type: "get",
                data: {
                    "contactsLike": $.trim($("#contactsLike").val())
                },
                success: function (data) {
                    let html = "";
                    $.each(data, function (k, v) {
                        html += '<tr>';
                        html += '<td><input type="radio" name="activity" onclick="addContacts(\'' + v.id + '\',\'' + v.fullname + '\')"/></td>';
                        html += '<td>' + v.fullname + '</td>';
                        html += '<td>' + v.email + '</td>';
                        html += '<td>' + v.mphone + '</td>';
                        html += '</tr>';
                    })
                    $("#showCBody").html(html);
                }
            })
        }

    </script>
</head>
<body>

<!-- 查找市场活动 -->
<div class="modal fade" id="findMarketActivity" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找市场活动</h4>
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
                <table id="activityTable3" class="table table-hover"
                       style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                    </tr>
                    </thead>
                    <tbody id="showABody">


                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- 查找联系人 -->
<div class="modal fade" id="findContacts" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找联系人</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询"
                                   id="contactsLike">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>邮箱</td>
                        <td>手机</td>
                    </tr>
                    </thead>
                    <tbody id="showCBody">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div style="position:  relative; left: 30px;">
    <h3>创建交易</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" class="btn btn-primary" id="saveBut">保存</button>
        <button type="button" class="btn btn-default">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form" style="position: relative; top: -30px;" id="saveForm"
      action="workbench/tran/saveTran.do" method="post">
    <div class="form-group">
        <label for="owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="owner" name="owner">

            </select>
        </div>
        <label for="money" class="col-sm-2 control-label">金额</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="money" name="money"/>
        </div>
    </div>

    <div class="form-group">
        <label for="name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="name" name="name"/>
        </div>
        <label for="expectedDate" class="col-sm-2 control-label">预计成交日期<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control time" id="expectedDate" name="expectedDate"/>
        </div>
    </div>

    <div class="form-group">
        <label for="customerName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="customerName" name="customerName" placeholder="支持自动补全，输入客户不存在则新建">
        </div>
        <label for="stage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="stage" name="stage">
                <option></option>
                <c:forEach items="${applicationScope.stage}" var="st">
                    <option value="${st.value}">${st.text}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="type" class="col-sm-2 control-label">类型</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="type" name="type">
                <option></option>
                <c:forEach items="${applicationScope.transactionType}" var="t">
                    <option value="${t.value}">${t.text}</option>
                </c:forEach>
            </select>
        </div>
        <label for="possibility" class="col-sm-2 control-label">可能性</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="possibility">
        </div>
    </div>

    <div class="form-group">
        <label for="source" class="col-sm-2 control-label">来源</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="source" name="source">
                <option></option>
                <c:forEach items="${applicationScope.source}" var="so">
                    <option value="${so.value}">${so.text}</option>
                </c:forEach>
            </select>
        </div>
        <label for="activityName" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                     id="showActivity"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="activityName">
            <input type="hidden" id="activityId" name="activityId"/>
        </div>
    </div>

    <div class="form-group">
        <label for="contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                     id="showContacts"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="contactsName">
            <input type="hidden" name="contactsId" id="contactsId">
        </div>
    </div>

    <div class="form-group">
        <label for="description" class="col-sm-2 control-label">描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="description" name="description"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="contactSummary" class="col-sm-2 control-label">联系纪要</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="contactSummary" name="contactSummary"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control time" id="nextContactTime" name="nextContactTime">
        </div>
    </div>

</form>
</body>
</html>