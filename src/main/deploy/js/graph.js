var g = undefined;
var data = [];
var ws = undefined;

function change(el) {
  g.setVisibility(parseInt(el.id), el.checked);
}

function buildLabels() {
  if (g.getLabels() === null) {
    setTimeout(buildLabels, 100);
  } else {
    console.log(g.getLabels());
    
    $("#series").empty();
    g.getLabels().forEach(function(column, i) {
      if (i > 0) {
        $("#series").append(`<input type=checkbox id="${i-1}" ${(i == 1) ? "checked" : ""} onClick="change(this)"><label for="${i-1}">${column}</label><br/>`);
        g.setVisibility(i - 1, (i == 1) ? true : false);
      }
    });
  }
}

function changeData(fname) {
  console.log("changeData(" + fname + ")");

  if (typeof(g) === "undefined") {
    g = new Dygraph(document.getElementById("graph1"), fname, {});
  }

  if (fname === "live") {
    $.getJSON("/header", function(headers) {
      data = [];
      g.updateOptions({ labels: headers, file: data });
      ws = new WebSocket("ws://" + location.host + "/datalog");
      ws.onmessage = function(event) {
        data.push(JSON.parse(event.data));
        g.updateOptions({ 'file': data });
      }
    });
  } else {
    if (ws != undefined) {
      ws.close();
      ws = undefined;
    }
    console.log("Load " + fname);
    $.get(fname, function(csv) {
      g.updateOptions({file: csv});
      console.log("Loaded " + fname);
      data = [];
      buildLabels();
    });
  }
}

$(document).ready(function() {
  $.getJSON("/logs", function(data) {
    console.log(data);
    var options = "";
    for(var i = 0; i < data.length; ++i) {
      options += "<option value='/log/" + data[i] + "'>" + data[i] + "</option>";
    }
    options += "<option value='live'>Live Data</option>";
    $("#lognames").html(options);
  });
});
