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