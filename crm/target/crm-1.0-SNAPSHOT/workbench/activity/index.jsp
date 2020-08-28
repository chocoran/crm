<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            $("#addbut").click(function () {
                $("#createActivityModal").modal("show");
            });

            $("#editbut").click(function () {
                if ($("input[name=id]:checked").length != 1) {
                    alert("请选择一个");
                    return;
                }
                $("#edit-id").val($.trim($("input[name=id]:checked").val()))
                $.ajax({
                    url: "workbench/activity/edit.do",
                    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                    dataType: "json",
                    type: "get",
                    data: {
                        "id": $.trim($("input[name=id]:checked").val())
                    },
                    success: function (data) {
                        $("#edit-name").val(data.activity.name);
                        $("#edit-cost").val(data.activity.cost);
                        $("#edit-startTime").val(data.activity.startDate);
                        $("#edit-endTime").val(data.activity.endDate);
                        $("#edit-describe").val(data.activity.description);
                        $.ajax({
                            url: "workbench/activity/getUsers.do",
                            dataType: "json",
                            type: "get",
                            success: function (data1) {
                                let str = "";
                                $.each(data1, function (k, v) {
                                    str = str + "<option value='" + v.id + "'>" + v.name + "</option>";
                                })
                                $("#edit-owner").html(str);
                                $("#edit-owner").val(data.activity.owner);
                            }
                        })
                    }
                })
                $("#editActivityModal").modal("show");
            });

            $("#edit-save-but").click(function () {
                $.ajax({
                    url: "workbench/activity/editSave.do",
                    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                    dataType: "json",
                    type: "post",
                    data: {
                        "id": $.trim($("#edit-id").val()),
                        "owner": $.trim($("#edit-owner").val()),
                        "name": $.trim($("#edit-name").val()),
                        "startDate": $.trim($("#edit-startTime").val()),
                        "endDate": $.trim($("#edit-endTime").val()),
                        "cost": $.trim($("#edit-cost").val()),
                        "description": $.trim($("#edit-describe").val())
                    },
                    success: function (data) {
                        if (data.success) {
                            alert("修改成功");
                            pageList(1, 5);
                        } else {
                            alert("修改失败");
                        }
                    }
                })
                $("#editActivityModal").modal("hide");
            })

            $.ajax({
                url: "workbench/activity/getUsers.do",
                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                type: "get",
                dataType: "json",
                success: function (data) {
                    let str = "";
                    $.each(data, function (k, v) {
                        str = str + "<option value='" + v.id + "'>" + v.name + "</option>";
                    })
                    $("#create-marketActivityOwner").html(str)
                    $("#create-marketActivityOwner").val("${sessionScope.user.id}")
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

            $("#savebut").click(function () {
                $.ajax({
                    url: "workbench/activity/save.do",
                    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                    dataType: "json",
                    type: "post",
                    data: {
                        "owner": $.trim($("#create-marketActivityOwner").val()),
                        "name": $.trim($("#create-marketActivityName").val()),
                        "startDate": $.trim($("#create-startTime").val()),
                        "endDate": $.trim($("#create-endTime").val()),
                        "cost": $.trim($("#create-cost").val()),
                        "description": $.trim($("#create-describe").val())
                    },
                    success: function (data) {
                        if (data.success == true) {
                            // $("#create-form")[0].reset();
                            $("#createActivityModal").modal("hide");
                            pageList(1, 5);
                        } else {
                            alert("保存失败");
                        }
                    }
                });
            });

            pageList(1, 5);

            $("#showbut").click(function () {
                $("#old-name").val($.trim($("#name").val()));
                $("#old-owner").val($.trim($("#owner").val()));
                $("#old-startTime").val($.trim($("#startTime").val()));
                $("#old-endTime").val($.trim($("#endTime").val()));
                pageList(1, 5);
            });

            $("#show-checkboxTotal").click(function () {
                $("input[name=id]").prop("checked", this.checked)
            });

            $("#showBody").on("click", $("input[name=id]"), function () {
                $("#show-checkboxTotal").prop("checked", $("input[name=id]").length == $("input[name=id]:checked").length);
            })

            $("#delete-but").click(function () {
                if ($("input[name=id]:checked").length == 0) {
                    alert("请选择")
                    return;
                }
                if (!confirm("是否确定执行删除操作")) {
                    return;
                }
                let json = "?";
                for (let i = 0; i < $("input[name=id]:checked").length; i++) {
                    json += "id=" + $($("input[name=id]:checked")[i]).val() + "&";
                }
                json = json.substring(0, json.length - 1);
                $.ajax({
                    url: "workbench/activity/delete.do" + json,
                    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        if (data.success) {
                            pageList(1, 5);
                        } else {
                            alert("删除失败");
                        }
                    }
                })
            })
        })
        ;

        function pageList(pageCount, pageSize) {
            $("#name").val($.trim($("#old-name").val()));
            $("#owner").val($.trim($("#old-owner").val()));
            $("#startTime").val($.trim($("#old-startTime").val()));
            $("#endTime").val($.trim($("#old-endTime").val()));
            $.ajax({
                url: "workbench/activity/pageList.do",
                dataType: "json",
                data: {
                    "name": $.trim($("#name").val()),
                    "owner": $.trim($("#owner").val()),
                    "startDate": $.trim($("#startTime").val()),
                    "endDate": $.trim($("#endTime").val()),
                    "pageCount": pageCount,
                    "pageSize": pageSize
                },
                type: "get",
                success: function (data) {
                    let showBody = "";
                    $("#show-checkboxTotal").prop("checked", false);
                    $.each(data.pageList, function (k, v) {
                        showBody += '<tr class="active">';
                        showBody += '<td><input type="checkbox" name="id" value="' + v.id + '"/></td>';
                        showBody += '<td><a style="text-decoration: none; cursor: pointer;"';
                        showBody += 'onclick="window.location.href=\'workbench/activity/detail.do?id=' + v.id + '\';">' + v.name + '</a></td>';
                        showBody += '<td>' + v.owner + '</td>';
                        showBody += '<td>' + v.startDate + '</td>';
                        showBody += '<td>' + v.endDate + '</td>';
                        showBody += '</tr>';
                    })
                    $("#showBody").html(showBody);
                    let totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;
                    $("#activityPage").bs_pagination({
                        currentPage: pageCount, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,

                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }
                    });
                }
            });
        }
    </script>
</head>
<body>

<input type="hidden" id="old-name">
<input type="hidden" id="old-owner">
<input type="hidden" id="old-startTime">
<input type="hidden" id="old-endTime">
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

                <form class="form-horizontal" role="form" id="create-form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">

                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startTime">
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endTime">
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="savebut">保存</button>
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
                        <label for="edit-owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">

                            </select>
                        </div>
                        <label for="edit-name" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-startTime">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-endTime">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" id="edit-cost">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="edit-save-but">更新</button>
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
                        <input class="form-control" type="text" id="name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="startTime"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="endTime">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="showbut">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="addbut">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editbut"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="delete-but"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="show-checkboxTotal"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="showBody">

                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">

            <div id="activityPage">

            </div>
        </div>

    </div>

</div>
</body>
</html>