$(document).on("click", "#addBtn", function (e) {
    e.preventDefault();
    var masterTasksDto = {taskType : 'MASTER', referenceTasks: $('#masterTasks').val().split(",")};
    var subTasksDto = {taskType : 'SUB', referenceTasks: $('#subTasks').val().split(",")};
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
            todoCreateFunction(data)
        },
        error: function (data) {
            alert("예약 등 에러!")
        }
    });
});

function todoCreateFunction(data) {
    var source = $("#todoList-template").html();
    var template = Handlebars.compile(source);
    var html = template([data]);
    console.log(html);
    $('#todoListTable tbody').append(html);
}


$(document).ready(showTodoList());
// $(document).ready(showDoneList());

function showTodoList() {
    var source = $("#todoList-template").html();
    var template = Handlebars.compile(source);

    $.ajax({
        url: "/api/tasks?complete=false",
        dataType: 'json'
    }).then(function (data) {
        console.log(data);
        var html = template(data);
        console.log(html);
        $('#todoListTable tbody').append(html);
    });
}

function doneTodoList() {
    var source = $("#doneList-template").html();
    var template = Handlebars.compile(source);
    $.ajax({
        url: "/api/tasks?complete=true",
        dataType: 'json'
    }).then(function (data) {
        console.log(data);
        var html = template(data);
        console.log(html);
        $('#doneListTable tbody').append(html);
    });
}

// $.fn.editable.defaults.ajaxOptions = {type: "PUT"};
// $(document).ready(function() {
//     $('#edit-todo').editable({
//         type: 'text',
//         // url: '/',
//         title: 'Modify Todo',
//         success: function(response, newValue) {
//             console.log(newValue);
//         }
//     });
// });

$(document).on("click", "#edit-todo", function (e) {
    var todo = $(this);
    todo.editable({
        type: 'text',
        // url: '/',
        title: 'Modify Todo',
        success: function(response, newValue) {
            console.log(newValue);
        }
    });
});