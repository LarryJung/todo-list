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

$(document).ready(showTodoList());
$(document).ready(showDoneList());

function showTodoList() {
    var source = $("#todoList-template").html();
    var template = Handlebars.compile(source);

    $.ajax({
        url: "/api/tasks?complete=false",
        dataType: 'json'
    }).then(function (data) {
        console.log(data);
        var html = template(data);
        $('#todoListTable tbody').append(html);
    });
}

function showDoneList() {
    var source = $("#doneList-template").html();
    var template = Handlebars.compile(source);
    $.ajax({
        url: "/api/tasks?complete=true",
        dataType: 'json'
    }).then(function (data) {
        console.log(data);
        var html = template(data);
        $('#doneListTable tbody').append(html);
    });
}

$(document).on("click", "#closeModal", function (e) {
    e.preventDefault();
    $(this).dialog("remove");
});

$(document).on('click', "#todo-refs-btn", function () {
    $('#masterTasks').multiselect();
    $('#subTasks').multiselect();
    var btn = $(this);
    var id = $('td:first', $(this).parents('tr')).text();
    $.ajax({
        url: "/api/tasks/" + id + "/references",
        dataType: 'json'
    }).then(function (data) {
        alert(JSON.stringify(data, null, '\t'));
        $('#masterSelect').val('');
        $('#subSelect').val('');
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

//
// // ======== paging
// var page = 1;                       //페이지 변수를 1로 초기화
//
// $.ajax({
//     type: 'POST',
//     url: "데이터를 가져올곳 url",
//     dataType: "json",
//     data: {
//         'page': page,
//     },
//     success: function (result) {
//         respone = result.lists;
//         paging = result.paging;             //페이징관련 데이터들을 paging변수에 삽입
//         $("데이터가 삽입될 객체 table").empty();    //데이터가 삽입될 객체를 비워준다. (들어가있던 전데이터들을 지워주기위해)
//         if (respone.length == 0) {                //가져온 데이터가 없으면 목록이 없다는 문구를 삽입.
//             $("데이터가 삽입될 tbody").append("<td colspan=20 style='padding:30px;'>데이터가 없습니다.</td>");
//         } else {
//             // handlebars.js 를 사용해서 append
//             // _.each(respone, function(item){
//             //     var contentHtml = _.template($('데이터가 삽입될 템플릿').html(), item );      //언더스코어를 이용 템플릿을 제작
//             //     $("데이터가 삽입될 tbody").append(contentHtml);
//             //
//             // });
//         }
//
//         //===================== 이제 페이징 처리?
//         $(".pagination").empty();  //페이징에 필요한 객체내부를 비워준다.
//         if (paging.page != 1) {            // 페이지가 1페이지 가아니면
//             $(".pagination").append("<li class=\"goFirstPage\"><a><<</a></li>");        //첫페이지로가는버튼 활성화
//         } else {
//             $(".pagination").append("<li class=\"disabled\"><a><<</a></li>");        //첫페이지로가는버튼 비활성화
//         }
//
//         if (paging.block != 1) {            //첫번째 블럭이 아니면
//             $(".pagination").append("<li class=\"goBackPage\"><a><</a></li>");        //뒤로가기버튼 활성화
//         } else {
//             $(".pagination").append("<li class=\"disabled\"><a><</a></li>");        //뒤로가기버튼 비활성화
//         }
//
//         for (var i = paging.startPage; i <= paging.endPage; i++) {        //시작페이지부터 종료페이지까지 반복문
//             if (paging.page == i) {                            //현재페이지가 반복중인 페이지와 같다면
//                 $(".pagination").append("<li class=\"disabled active\"><a>" + i + "</a></li>");    //버튼 비활성화
//             } else {
//                 $(".pagination").append("<li class=\"goPage\" data-page=\"" + i + "\"><a>" + i + "</a></li>"); //버튼 활성화
//             }
//         }
//
//         if (paging.block < paging.totalBlock) {            //전체페이지블럭수가 현재블럭수보다 작을때
//             $(".pagination").append("<li class=\"goNextPage\"><a>></a></li>");         //다음페이지버튼 활성화
//         } else {
//             $(".pagination").append("<li class=\"disabled\"><a>></a></li>");        //다음페이지버튼 비활성화
//         }
//
//         if (paging.page < paging.totalPage) {                //현재페이지가 전체페이지보다 작을때
//             $(".pagination").append("<li class=\"goLastPage\"><a>>></a></li>");    //마지막페이지로 가기 버튼 활성화
//         } else {
//             $(".pagination").append("<li class=\"disabled\"><a>>></a></li>");        //마지막페이지로 가기 버튼 비활성화
//         }
//     }
// });
//
//
//
//
//
// $(".goFirstPage").click(function(){
//     page = 1;
//     pageFlag = 1;
//     $("상단 ajax를 함수로 만들어 재귀호출");
//     pageFlag = 0;
// });
//
// $(".goBackPage").click(function(){
//     page = Number(paging.startPage) - 1;
//     pageFlag = 1;
//     $("상단 ajax를 함수로 만들어 재귀호출");
//     pageFlag = 0;
// });
//
// $(".goPage").click(function(){
//     page = $(this).attr("data-page");
//     pageFlag = 1;
//     $("상단 ajax를 함수로 만들어 재귀호출");
//     pageFlag = 0;
// });
//
// $(".goNextPage").click(function(){
//     page = Number(paging.endPage) + 1;
//     pageFlag = 1;
//     $("상단 ajax를 함수로 만들어 재귀호출");
//     pageFlag = 0;
// });
//
// $(".goLastPage").click(function(){
//     page = paging.totalPage;
//     pageFlag = 1;
//     $("상단 ajax를 함수로 만들어 재귀호출");
//     pageFlag = 0;
// });




