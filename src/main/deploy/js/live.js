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

$(document).ready(function() {
   g = new Dygraph(document.getElementById("graph1"), "Timestamp,heading\n0.0,0.0", {});

   $.getJSON("/header", function(headers) {
     data = [];
     g.updateOptions({ 'labels': headers });
     
     ws = new WebSocket("ws://" + location.host + "/datalog");
     ws.onmessage = function(event) {
       data.push(JSON.parse(event.data));
       g.updateOptions({ 'file': data });
     }

     buildLabels();
   });
});

