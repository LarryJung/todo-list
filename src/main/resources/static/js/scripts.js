// Create a "close" button and append it to each list item
var todoListDiv = document.getElementById("todoListDiv");
var myNodelist = todoListDiv.getElementsByTagName("li");
var i;
for (i = 0; i < myNodelist.length; i++) {
    var completeSpan = document.createElement("SPAN");
    var txt = document.createTextNode("완료");
    completeSpan.className = "close";
    completeSpan.appendChild(txt);
    myNodelist[i].appendChild(completeSpan);

    var refsSpan = document.createElement("SPAN");
    var txt2 = document.createTextNode("Refs");
    refsSpan.className = "btn";
    refsSpan.appendChild(txt2);
    myNodelist[i].appendChild(refsSpan);
}

// Click on a close button to hide the current list item
var close = document.getElementsByClassName("close");
var i;
for (i = 0; i < close.length; i++) {
    close[i].onclick = function () {
        var div = this.parentElement;
        div.style.display = "none";
    }
}

// Add a "checked" symbol when clicking on a list item
var list = document.querySelector('ul');
list.addEventListener('click', function (ev) {
    if (ev.target.tagName === 'LI') {
        ev.target.classList.toggle('checked');
    }
}, false);

// Create a new list item when clicking on the "Add" button
function newElement() {
    var li = document.createElement("li");
    var inputValue = document.getElementById("myInput").value;
    var t = document.createTextNode(inputValue);
    li.appendChild(t);
    if (inputValue === '') {
        alert("You must write something!");
    } else {
        document.getElementById("myUL").appendChild(li);
    }
    document.getElementById("myInput").value = "";

    var span = document.createElement("SPAN");
    var txt = document.createTextNode("\u00D7");
    span.className = "close";
    span.appendChild(txt);
    li.appendChild(span);

    for (i = 0; i < close.length; i++) {
        close[i].onclick = function () {
            var div = this.parentElement;
            div.style.display = "none";
        }
    }
}

function checkUnique() {
    var inputValue = document.getElementById("myInput").value;
    // ajax로 유효성 검사
}


var dataSource = [];

$(document).ready(function () {
    $.ajax({
        url: '/api/tasks/referenceList',
        dataType: "json",
        success: function (data) {
            dataSource = JSON.stringify(data);
            console.log("hi" + dataSource);
        },
        error: function (xhr, status, error) {
            // alert("Error");
        }
    });
});

$(function () {
    var basic = $('#basic');
    basic.keydown(function () {
        basic.magicsearch({
            dataSource: dataSource,
            fields: ['id', 'todo'],
            id: 'id',
            format: '%id% · %todo%',
            multiple: true,
            multiField: 'todo',
            multiStyle: {
                space: 5,
                width: 80
            }
        });
    });
});
