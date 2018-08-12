$(document).on("click", "#addBtn", function (e) {
    e.preventDefault();
    var masterTasksDto;
    if($('#masterTasks').val()) {
      masterTasksDto = {taskType : 'MASTER', referenceTasks: $('#masterTasks').val().split(",")};
    }
    var subTasksDto;
    if($('#subTasks').val()) {
     subTasksDto = {taskType : 'SUB', referenceTasks: $('#subTasks').val().split(",")};
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
            todoCreateFunction(data)
        },
        error: function (data) {
            alert("예약 등 에러!")
        }
    });
});

function todoCreateFunction(data) {
    var modalSource = $('#modal-template').html();
    var modalTemplate = Handlebars.compile(modalSource);
    console.log(data.sub_tasks[0]);
    console.log(data.master_tasks[0]);
    var subModalHtml = modalTemplate([data.sub_tasks]);
    var masterModalHtml = modalTemplate([data.master_tasks]);
    console.log(subModalHtml);
    console.log(masterModalHtml);
    var source = $("#todoList-template").html();
    var template = Handlebars.compile(source);
    var html = template([data]);

    html.find('#modalBody').append(masterModalHtml);
    html.find('#modalBody').append(subModalHtml);

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

$(document).on("click", "#edit-todo", function (e) {
    var todo = $(this);
    var currentRow = todo.closest('tr');
    var col1=currentRow.find("td:eq(0)").text();
    console.log(col1);
    todo.editable({
        type: 'text',
        title: 'Modify Todo',
        // url: '/api/tasks/'+col1,
        success: function(response, newValue) {
            console.log(newValue);
        }
    });
});

$(document).on("click", "#edit-createdDate", function (e) {
    var createdDate = $(this);
    var currentRow = createdDate.closest('tr');
    var col1=currentRow.find("td:eq(0)").text();
    createdDate.editable({
        type: 'datetime',
        url: '/api/tasks/',
        pk: col1,
        title: 'Created Date',
        format: 'yyyy-mm-dd hh:ii:ss',
        viewformat: 'yyyy-mm-dd hh:ii',
        success: function(response, newValue) {
            console.log(newValue);
        }
    });
});

$(document).on("click", "#edit-modifiedDate", function (e) {
    var modifiedDate = $(this);
    modifiedDate.editable({
        type: 'datetime',
        // url: '/',
        title: 'Created Date',
        // format: 'yyyy-mm-dd hh:ii',
        viewformat: 'yyyy-mm-dd hh:ii',
        success: function(response, newValue) {
            console.log(newValue);
        }
    });
});

