// Create a "close" button and append it to each list item
// var todoListDiv = document.getElementById("todoListDiv");
// var myNodelist = todoListDiv.getElementsByTagName("li");
// var i;
// for (i = 0; i < myNodelist.length; i++) {
//     var completeSpan = document.createElement("SPAN");
//     var txt = document.createTextNode("완료");
//     completeSpan.className = "close";
//     completeSpan.appendChild(txt);
//     myNodelist[i].appendChild(completeSpan);
//
//     var refsSpan = document.createElement("SPAN");
//     var txt2 = document.createTextNode("Refs");
//     refsSpan.className = "btn";
//     refsSpan.appendChild(txt2);
//     myNodelist[i].appendChild(refsSpan);
// }
//
// // Click on a close button to hide the current list item
// var close = document.getElementsByClassName("close");
// var i;
// for (i = 0; i < close.length; i++) {
//     close[i].onclick = function () {
//         var div = this.parentElement;
//         div.style.display = "none";
//     }
// }
//
// // Add a "checked" symbol when clicking on a list item
// var list = document.querySelector('ul');
// list.addEventListener('click', function (ev) {
//     if (ev.target.tagName === 'LI') {
//         ev.target.classList.toggle('checked');
//     }
// }, false);
//
// // Create a new list item when clicking on the "Add" button
// function newElement() {
//     var li = document.createElement("li");
//     var inputValue = document.getElementById("myInput").value;
//     var t = document.createTextNode(inputValue);
//     var allListElements = $('span');
//     console.log($('#subItems').children());
//     li.appendChild(t);
//     if (inputValue === '') {
//         alert("You must write something!");
//     } else {
//         document.getElementById("myUL").appendChild(li);
//     }
//     document.getElementById("myInput").value = "";
//
//     var span = document.createElement("SPAN");
//     var txt = document.createTextNode("\u00D7");
//     span.className = "close";
//     span.appendChild(txt);
//     li.appendChild(span);
//
//     for (i = 0; i < close.length; i++) {
//         close[i].onclick = function () {
//             var div = this.parentElement;
//             div.style.display = "none";
//         }
//     }
// }
//
// var dataSource = [];
//
// $(document).ready(function () {
//     $.ajax({
//         url: '/api/tasks/referenceList',
//         dataType: "json",
//         success: function (data) {
//             dataSource = JSON.stringify(data);
//             console.log("hi" + dataSource);
//         },
//         error: function (xhr, status, error) {
//             // alert("Error");
//         }
//     });
// });
//
// $(function () {
//     dataSource = [{id: 1, todo: '방청소'},
//         {id:2, todo: '집안일'},
//         {id:3, todo: '청소'},
//         {id:4, todo: '빨래'}];
//     var basic = $('#masterTasksInput');
//     basic.keydown(function () {
//         basic.magicsearch({
//             dataSource: dataSource,
//             fields: ['id', 'todo'],
//             id: 'id',
//             format: '%id% · %todo%',
//             multiple: true,
//             multiField: 'todo',
//             multiStyle: {
//                 space: 5,
//                 width: 80
//             }
//         });
//     });
// });

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
        },
        error: function (data) {
            alert("예약 등 에러!")
        }
    });
});
