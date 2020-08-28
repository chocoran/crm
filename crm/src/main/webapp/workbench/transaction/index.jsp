<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
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
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">

        $(function () {
            $("#selectAll").click(function () {
                $("input[name=id]").prop("checked",this.checked);
            })

            $("#showBody").on("click",$("input[name=id]"),function () {
                $("#selectAll").prop("checked",$("input[name=id]").length == $("input[name=id]:checked").length)
            })

            pageList(1,5);

            $("#selectBut").click(function () {
                $("#old-name").val($.trim($("#name").val()));
                $("#old-owner").val($.trim($("#owner").val()));
                $("#old-customer").val($.trim($("#customer").val()));
                $("#old-contact").val($.trim($("#contact").val()));
                $("#old-stage").val($.trim($("#stage").val()));
                $("#old-source").val($.trim($("#source").val()));
                $("#old-type").val($.trim($("#type").val()));
                pageList(1,5);

            })
        });

        function pageList(pageCount,pageSize) {
            $("#name").val($.trim($("#old-name").val()));
            $("#owner").val($.trim($("#old-owner").val()));
            $("#customer").val($.trim($("#old-customer").val()));
            $("#contact").val($.trim($("#old-contact").val()));
            $("#stage").val($.trim($("#old-stage").val()));
            $("#source").val($.trim($("#old-source").val()));
            $("#type").val($.trim($("#old-type").val()));
            $("#selectAll").val("");
            $.ajax({
                url: "workbench/tran/showTran.do",
                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                dataType: "json",
                type: "get",
                data: {
                    "name" : $.trim($("#name").val()),
                    "owner" : $.trim($("#owner").val()),
                    "customer" : $.trim($("#customer").val()),
                    "contact" : $.trim($("#contact").val()),
                    "stage" : $.trim($("#stage").val()),
                    "source" : $.trim($("#source").val()),
                    "type" : $.trim($("#type").val()),
                    "pageCount" : pageCount,
                    "pageSize" : pageSize
                },
                success: function (data) {
                    let html = "";
                    $.each(data.pageList,function (k,v) {
                        html += '<tr>';
                        html += '<td><input type="checkbox" name="id" value="' + v.id + '"/></td>';
                        html += '<td><a style="text-decoration: none; cursor: pointer;"';
                        html += 'onclick="window.location.href=\'workbench/tran/toDetail.do?id=' + v.id + '\';">' + v.name + '</a></td>';
                        html += '<td>' + v.customerId + '</td>';
                        html += '<td>' + v.stage + '</td>';
                        html += '<td>' + v.type + '</td>';
                        html += '<td>' + v.owner + '</td>';
                        html += '<td>' + v.source + '</td>';
                        html += '<td>' + v.contactsId + '</td>';
                        html += '</tr>';
                    })
                    $("#showBody").html(html);
                    let totalPages = (data.total%pageSize == 0) ? data.total/pageSize : parseInt(data.total/pageSize) + 1;
                    $("#page").bs_pagination({
                        currentPage: pageCount, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数，需要计算
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,

                        onChangePage : function(event, data){
                            pageList(data.currentPage , data.rowsPerPage);
                        }
                    });

                }

            })
        }

        function editTran(){
            if ($("input[name=id]:checked").length != 1){
                alert("选择一个");
                return;
            }
            window.location.href="workbench/tran/toEdit.do?id=" + $.trim($("input[name=id]:checked").val());
        }

        function deleteTran() {

        }
    </script>
</head>
<body>
<input type="hidden" id="old-name" />
<input type="hidden" id="old-owner" />
<input type="hidden" id="old-customer" />
<input type="hidden" id="old-contact" />
<input type="hidden" id="old-stage" />
<input type="hidden" id="old-source" />
<input type="hidden" id="old-type" />

<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>交易列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="owner">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">客户名称</div>
                        <input class="form-control" type="text" id="customer">
                    </div>
                </div>

                <br>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">阶段</div>
                        <select class="form-control" id="stage">
                            <option></option>
                            <c:forEach items="${applicationScope.stage}" var="st">
                                <option value="${st.value}">${st.text}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">类型</div>
                        <select class="form-control" id="type">
                            <option></option>
                            <c:forEach items="${applicationScope.transactionType}" var="t">
                                <option value="${t.value}">${t.text}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">来源</div>
                        <select class="form-control" id="source">
                            <option></option>
                            <c:forEach items="${applicationScope.source}" var="so">
                                <option value="${so.value}">${so.text}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">联系人名称</div>
                        <input class="form-control" type="contact">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="selectBut">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/save.jsp';"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" onclick="editTran()"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus" onclick="deleteTran()"></span> 删除</button>
            </div>


        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="selectAll"/></td>
                    <td>名称</td>
                    <td>客户名称</td>
                    <td>阶段</td>
                    <td>类型</td>
                    <td>所有者</td>
                    <td>来源</td>
                    <td>联系人名称</td>
                </tr>
                </thead>
                <tbody id="showBody">

                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 20px;">
            <div id="page">

            </div>
        </div>

    </div>

</div>
</body>
</html>