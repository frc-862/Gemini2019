var data = [];
var ws = undefined;
var chart = undefined;

function change(el) {
}

function buildLabels() {
}

function changeData(fname) {
    chart.update({
        data: {
            csvURL: `http://${location.host}${fname}`
        }
    });
}

$(document).ready(function () {
    $.getJSON("/logs", function (data) {
        var options = "";
        for (var i = 0; i < data.length; ++i) {
            options += "<option value='/log/" + data[i] + "'>" + data[i] + "</option>";
        }
        var fname = data[0];
        $("#lognames").html(options);
        chart = Highcharts.chart('graph1', {
            chart: {
                zoomType: 'x'
            },

            title: {
                text: 'Lightning Robotics Onboard Logging'
            },

            boost: {
                useGPUTranslations: true
            },

            data: {
                csvURL: `http://${location.host}/log/${fname}`,
                complete: function (mychart) {
                  $.each(mychart.series, function(index, val) {
                      val.visible = (index == 0);
                  });
                }
            },

            //subtitle: {
            //text: 'Source: thesolarfoundation.com'
            //},

            xAxis: {
                title: {
                    text: "Seconds"
                }
            },

            yAxis: {
                title: {
                    text: ''
                }
            },

            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle'
            },

            events: {
            },

            //plotOptions: {
            //series: {
            //label: {
            //connectorAllowed: false
            //},
            //pointStart: 2010
            //}
            //},

            responsive: {
                rules: [{
                    condition: {
                        maxWidth: 500
                    },
                    chartOptions: {
                        legend: {
                            layout: 'horizontal',
                            align: 'center',
                            verticalAlign: 'bottom'
                        }
                    }
                }]
            }
        });
    });


});
