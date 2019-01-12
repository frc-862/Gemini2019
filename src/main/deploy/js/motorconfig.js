
var loadModel = function() {
    var fname = $("#configNames").val();
    $.getJSON("/motor/model", function(model) {
        $("#configModel").html("<dl id='modelFields'></dl>");
        Object.keys(model).sort().forEach(function(fld) {
            $("#modelFields").append(`<dt><input type="checkbox" name="${fld}_enabled" value="${fld}"><span>${fld}</span></dt><dd id='${fld}' class='modelField'>0</dd>`);
        });

        $.getJSON(`/motor/${fname}`, function(values) {
          console.log(values);
          Object.keys(values).forEach(function(key) {
            $(`#${key}`).html(values[key]);
            $(`input[name="${key}_enabled"]`).prop("checked", true);
          });
        });

        $('.modelField').editable(`http://${location.host}/update_motor/${fname}`, {
          callback: function(value, settings, foo) {
            var key = $(this).prop("id");
            $(`input[name="${key}_enabled"]`).prop("checked", true);
          }
        });

        $("input[type='checkbox']").change(function() {
          var key = $(this).prop("value");
          var value = $(`dd#${key}`).text();
          if ($(this).prop("checked")) {
            console.log("enable");
            console.log(key);
            console.log(value);
            $.ajax({
              url: `/motor/config/${fname}/${key}/${value}`,
              type: 'PUT',
              success: function(result) {
                console.log(`${key} is set to ${value} in ${fname}.`);
              }
            });
          } else {
            $.ajax({
              url: `/motor/config/${fname}/${key}`,
              type: 'DELETE',
              success: function(result) {
                console.log(`${key} is deleted from ${fname}.`);
              }
            });
          }
        });

    });
}

$(document).ready(function() {
    loadModel();
});
