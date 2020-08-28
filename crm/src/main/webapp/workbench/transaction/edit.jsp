<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.zlz.crm.workbench.domain.Tran" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String stage = ((Tran) request.getSession().getAttribute("editTran")).getStage();
    ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
    request.getSession().setAttribute("possibility",rb.getString(stage));
%>

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
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
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

            $("#edit-clueSource").val("${sessionScope.editTran.source}");
            $("#edit-transactionType").val("${sessionScope.editTran.type}");
            $("#edit-transactionStage").val("${sessionScope.editTran.stage}");

            $.ajax({
                url: "workbench/tran/getUser.do",
                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                dataType: "json",
                type: "get",
                success: function (data) {
                    let html = "";
                    $.each(data,function (k,v) {
                        html += '<option value=\'' + v.name + '\'>' + v.name + '</option>';
                    })
                    $("#edit-transactionOwner").html(html);
                    $("#edit-transactionOwner").val("${sessionScope.editTran.owner}")
                }
            })

            $("#saveBut").click(function () {
                $("#saveForm").submit();
            })

            $("#edit-accountName").typeahead({
                source: function (query, process) {
                    $.get(
                        "workbench/tran/getCustomerName.do",
                        { "name" : query },
                        function (data) {
                            //alert(data);
                            process(data);
                        },
                        "json"
                    );
                },
                delay: 1
            });

            $("#edit-transactionStage").change(function () {
                $.ajax({
                    url: "workbench/tran/getPossibility.do",
                    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                    dataType: "json",
                    type: "post",
                    data: {
                        "stage" : $("#edit-transactionStage").val()
                    },
                    success: function (data) {
                        $("#edit-possibility").val(data);
                    }
                })

            })

            $("#showA").click(function () {
                $("#findMarketActivity").modal("show");
                showA();
            })

            $("#showC").click(function () {
                $("#findContacts").modal("show");
                showC();
            })

            $("#ALike").change(function () {
                showA();
            })

            $("#CLike").change(function () {
                showC()
            })

        })

        function showA() {
            $.ajax({
                url: "workbench/tran/getActivity.do",
                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                dataType: "json",
                type: "get",
                data: {
                    "activityLike" : $.trim($("#ALike").val())
                },
                success: function (data) {
                    let html = "";
                    $.each(data,function (k,v) {
                        html += '<tr>';
                        html += '<td><input type="radio" onclick="changeA(\'' + v.id + '\',\'' + v.name + '\')"/></td>';
                        html += '<td>' + v.name + '</td>';
                        html += '<td>' + v.startDate + '</td>';
                        html += '<td>' + v.endDate + '</td>';
                        html += '<td>' + v.owner + '</td>';
                        html += '</tr>';
                    })
                    $("#aBody").html(html);
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
                    "contactsLike" : $.trim($("#CLike").val())
                },
                success: function (data) {
                    let html = "";
                    $.each(data,function (k,v) {
                        html += '<tr>';
                        html += '<td><input type="radio" onclick="changeC(\'' + v.id + '\',\'' + v.fullname + '\')"/></td>';
                        html += '<td>' + v.fullname + '</td>';
                        html += '<td>' + v.email + '</td>';
                        html += '<td>' + v.mphone + '</td>';
                        html += '</tr>';
                    })
                    $("#cBody").html(html);
                }
            })

        }

        function changeA(id,name){
            $("#edit-activityId").val(id);
            $("#edit-activitySrc").val(name);
            $("#findMarketActivity").modal("hide");
        }

        function changeC(id,name){
            $("#edit-contactsId").val(id);
            $("#edit-contactsName").val(name);
            $("#findContacts").modal("hide");
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
                                   placeholder="请输入市场活动名称，支持模糊查询" id="ALike">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable4" class="table table-hover"
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
                    <tbody id="aBody">


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
                            <input type="text" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询" id="CLike">
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
                    <tbody id="cBody">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div style="position:  relative; left: 30px;">
    <h3>更新交易</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" class="btn btn-primary" id="saveBut">更新</button>
        <button type="button" class="btn btn-default">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form" style="position: relative; top: -30px;" id="saveForm" action="workbench/tran/editTran.do" method="post">
    <input type="hidden" name="id" value="${sessionScope.editTran.id}">
    <div class="form-group">
        <label for="edit-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-transactionOwner" name="owner">

            </select>
        </div>
        <label for="edit-amountOfMoney" class="col-sm-2 control-label">金额</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-amountOfMoney" name="money" value="${sessionScope.editTran.money}">
        </div>
    </div>

    <div class="form-group">
        <label for="edit-transactionName" class="col-sm-2 control-label">名称<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-transactionName" name="name" value="${sessionScope.editTran.name}">
        </div>
        <label for="edit-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control time" id="edit-expectedClosingDate" name="expectedDate"
                   value="${sessionScope.editTran.expectedDate}">
        </div>
    </div>

    <div class="form-group">
        <label for="edit-accountName" class="col-sm-2 control-label">客户名称<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-accountName" name="customerName" value="${sessionScope.editTran.customerId}"
                   placeholder="支持自动补全，输入客户不存在则新建">
        </div>
        <label for="edit-transactionStage" class="col-sm-2 control-label">阶段<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-transactionStage" name="stage">
                <c:forEach items="${applicationScope.stage}" var="st">
                    <option value="${st.value}">${st.text}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="edit-transactionType" class="col-sm-2 control-label">类型</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-transactionType" name="type">
                <c:forEach items="${applicationScope.transactionType}" var="t">
                    <option value="${t.value}">${t.text}</option>
                </c:forEach>
            </select>
        </div>
        <label for="edit-possibility" class="col-sm-2 control-label">可能性</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-possibility" value="${sessionScope.possibility}">
        </div>
    </div>

    <div class="form-group">
        <label for="edit-clueSource" class="col-sm-2 control-label">来源</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-clueSource" name="source">
                <c:forEach items="${applicationScope.source}" var="so">
                    <option value="${so.value}">${so.text}</option>
                </c:forEach>
            </select>
        </div>
        <label for="edit-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                         id="showA"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-activitySrc" value="${sessionScope.editTran.activityId}">
            <input type="hidden" name="activityId" id="edit-activityId">
        </div>
    </div>

    <div class="form-group">
        <label for="edit-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                          id="showC"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-contactsName" value="${sessionScope.editTran.contactsId}">
            <input type="hidden" name="contactsId" id="edit-contactsId">
        </div>
    </div>

    <div class="form-group">
        <label for="create-describe" class="col-sm-2 control-label">描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" name="description" id="create-describe">${sessionScope.editTran.description}</textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" name="contactSummary"
                      id="create-contactSummary">${sessionScope.editTran.contactSummary}</textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control time" id="create-nextContactTime" name="nextContactTime"
                   value="${sessionScope.editTran.nextContactTime}"/>
        </div>
    </div>

</form>
</body>
</html>