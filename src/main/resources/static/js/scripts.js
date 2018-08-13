$(document).on("click", "#addBtn", function (e) {
    e.preventDefault();
    var masterTasksDto;
    var subTasksDto;
    if ($('#masterTasks').val()) {
        masterTasksDto = {taskType: 'MASTER', referenceTasks: $('#masterTasks').val().split(",")};
    }
    if ($('#subTasks').val()) {
        subTasksDto = {taskType: 'SUB', referenceTasks: $('#subTasks').val().split(",")};
    }
    var taskRequestDto = {todo: $('#todo').val(), masterTasksDto: masterTasksDto, subTasksDto: subTasksDto};
    console.log(taskRequestDto);
    $.ajax({
        url: '/api/tasks',
        type: 'POST',
        data: JSON.stringify(taskRequestDto),
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log(data);
            $('#todo').val('');
            todoCreateFunction(data)
        },
        error: function (data) {
            console.log(data);
            alert(JSON.stringify(data, null, '\t'));
        }
    });
});

function todoCreateFunction(data) {
    var source = $("#todoList-template").html();
    var template = Handlebars.compile(source);
    var html = template([data]);
    $('#todoListTable tbody').append(html);
}

function doneCreateFunction(data) {
    var source = $("#doneList-template").html();
    var template = Handlebars.compile(source);
    var html = template([data]);
    $('#doneListTable tbody').append(html);
}

$(document).on('click', "#todo-refs-btn", function () {
    var id = $('td:first', $(this).parents('tr')).text();
    $.ajax({
        url: "/api/tasks/" + id + "/references",
        dataType: 'json'
    }).then(function (data) {
        alert(JSON.stringify(data, null, '\t'));
    });
});

$(document).on("click", "#todo-complete-btn", function (e) {
    e.preventDefault();
    var btn = $(this);
    var id = $('td:first', $(this).parents('tr')).text();
    $.ajax({
        url: "/api/tasks/" + id + "/complete",
        success: function (data) {
            btn.closest('tr').remove();
            doneCreateFunction(data);
        },
        error: function (data) {
            console.log(data);
            alert(JSON.stringify(data, null, '\t'));
        }
    })
});

$.fn.editable.defaults.ajaxOptions = {type: "PUT"};
$(document).on("click", "#edit-todo", function (e) {
    e.preventDefault();
    var todo = $(this);
    var pk = $('td:first', $(this).parents('tr')).text();
    todo.editable({
        type: 'text',
        pk: pk,
        url: '/api/tasks/' + pk,
        title: 'Modify Todo',
        success: function (response, newValue) {
            console.log(response);
            $(this).parents('tr').find('td:eq(3)').html(response.modifiedDate);
        }
    });
});

// ============================ paging
$(document).ready(function() {
    todoTable = $('#todoTable');
    doneTable = $('#doneTable');
    var todoSource = $("#todoList-template").html();
    var doneSource = $("#doneList-template").html();
    var todoTemplate = Handlebars.compile(todoSource);
    var doneTemplate = Handlebars.compile(doneSource);
    doPagination(0, false, todoTable, todoTemplate);
    doPagination(0, true, doneTable, doneTemplate);
});

$(document).on('click', ".goFirstPage", function() {
    console.log(this);
});

$(document).on('click', ".goBackPage", function() {
    console.log(this);
});

$(document).on('click', ".goPage", function() {
    console.log(this);
    var page = $(this).attr('data-page');
    if($(this).closest("ul").attr("id") == "todoPagination") {
        todoTable = $('#todoTable');
        var todoSource = $("#todoList-template").html();
        var todoTemplate = Handlebars.compile(todoSource);
        doPagination(page, false, todoTable, todoTemplate);
    }
    if($(this).closest("ul").attr("id") == "donePagination") {
        doneTable = $('#doneTable');
        var doneSource = $("#doneList-template").html();
        var doneTemplate = Handlebars.compile(doneSource);
        doPagination(page, true, doneTable, doneTemplate);
    }
});

$(document).on('click', ".goNextPage", function(){
    var paging;
    console.log($(this).closest("goPage").text());
    // if($(this).closest("ul").attr("id") == "todoPagination") {
    //     $.ajax({
    //         url: '/api/tasks/' + '?' + $.param({"page": page, "size": 5, "complete": complete}),
    //         dataType: "json",
    //         success: function (result) {
    //             response = result.list;
    //             console.log(response);
    //             paging = result.paging;
    //         }
    //     });
    //     todoTable = $('#todoTable');
    //     var todoSource = $("#todoList-template").html();
    //     var todoTemplate = Handlebars.compile(todoSource);
    //     doPagination(Number(paging.endPage) + 1, false, todoTable, todoTemplate);
    // }
    // if($(this).closest("ul").attr("id") == "donePagination") {
    //     doneTable = $('#doneTable');
    //     var doneSource = $("#doneList-template").html();
    //     var doneTemplate = Handlebars.compile(doneSource);
    //     doPagination(Number(paging.endPage) + 1, true, doneTable, doneTemplate);
    // }
});
$(document).on('click', ".goLastPage", function() {
    console.log(this);
});
//
// $(document).on('click', ".goFirstPage", makeTable(0));
// $(document).on('click', ".goBackPage", makeTable(Number(paging.startPage) - 1));
// $(document).on('click', ".goPage", makeTable($(this).attr("data-page")));
// $(document).on('click', ".goNextPage", makeTable(Number(paging.endPage) + 1));
// $(document).on('click', ".goLastPage", makeTable(paging.totalPage));
//
// function makeTable(page) {
//     console.log("테이블을 만들자 몇페이지? "+ page);
//     var complete;
//     table = $(this).closest('table');
//     var source;
//     if ($(this).closest('ul').attr('id') == "todoPagination") {
//         complete = false;
//         source = $("#todoList-template").html();
//     }
//     if ($(this).closest('ul').attr('id') == "donePagination") {
//         complete = true;
//         source = $("#doneList-template").html();
//     }
//     var template = Handlebars.compile(source);
//     console.log("complete : " + complete);
//     console.log("template" + template);
//     doPagination(page, complete, table, template)
// }

function doPagination(page, complete, table, template) {
    $.ajax({
        url: '/api/tasks/' + '?' + $.param({"page": page, "size": 5, "complete": complete}),
        dataType: "json",
        success: function (result) {
            response = result.list;
            console.log(response);
            paging = result.paging;
            console.log(paging);
            table.find("tr:not(:first)").remove();
            if(response.length != 0){
                var html = template(response);
                table.children('tbody').append(html);
            }
            var pagination;
            if(table.attr('id') == "todoTable") {
                pagination = $("#todoPagination");
            }
            if(table.attr('id') == "doneTable") {
                pagination = $("#donePagination");
            }
            pagination.empty();
            if (paging.page != 0) {                                                         // 페이지가 1페이지 가아니면
                pagination.append("<li class=\"goFirstPage\"><a><<</a></li>");        //첫페이지로가는버튼 활성화
            } else {
                pagination.append("<li class=\"disabled\"><a><<</a></li>");           //첫페이지로가는버튼 비활성화
            }
            console.log("paging block : " + paging.block);
            if (paging.block != 0) {
                pagination.append("<li class=\"goBackPage\"><a><</a></li>");        //뒤로가기버튼 활성화
            } else {
                pagination.append("<li class=\"disabled\"><a><</a></li>");          //뒤로가기버튼 비활성화
            }
            console.log("paging.page = " + paging.page)
            for (var i = paging.startPage; i <= paging.endPage; i++) {                 //시작페이지부터 종료페이지까지 반복문
                if (paging.page == i) {                                                 //현재페이지가 반복중인 페이지와 같다면
                    pagination.append("<li class=\"disabled active\"><a>" + i + "</a></li>");    //버튼 비활성화
                } else {
                    pagination.append("<li class=\"goPage\" data-page=\"" + i + "\"><a>" + i + "</a></li>"); //버튼 활성화
                }
            }
            console.log("paging block : " + paging.block +" paging total block" +paging.totalBlock);
            if (paging.block < paging.totalBlock) {
                pagination.append("<li class=\"goNextPage\"><a>></a></li>");         //다음페이지버튼 활성화
            } else {
                pagination.append("<li class=\"disabled\"><a>></a></li>");           //다음페이지버튼 비활성화
            }

            console.log("paging block : " + paging.page +" paging total block" +paging.totalPage);
            if (paging.page < paging.totalPage) {                                      //현재페이지가 전체페이지보다 작을때
                pagination.append("<li class=\"goLastPage\"><a>>></a></li>");    //마지막페이지로 가기 버튼 활성화
            } else {
                pagination.append("<li class=\"disabled\"><a>>></a></li>");        //마지막페이지로 가기 버튼 비활성화
            }
        }
    });
}

