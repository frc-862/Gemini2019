var previousVal = "";

$(document).ready(function() {
   setInterval(function() {
      $.getJSON("http://roborio-862-frc.local:8620/faultlist", function(faults) {
         var strFaults = JSON.stringify(faults);
         if (strFaults != previousVal) {
            faults.forEach(function (fault) {
               $(`#FAULT_${fault}`).addClass("false").removeClass("true");
            });   
            $.get("http://roborio-862-frc.local:8620/faultlog", function(faults) {
               faults = faults.split("\n").reverse().join("\n");
               $("#faultlog").text(faults);
            });
            previousVal = strFaults;
         }
      });   
   }, 1000);
});