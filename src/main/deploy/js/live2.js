var firstLoad = true;
var chart = undefined;

$(document).ready(function () {
    $.getJSON("/logs", function (data) {
        fname = data[data.length - 1];

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
                enablePolling: true,
                dataRefreshRate: 3,
                complete: function (mychart) {
                    if (firstLoad) {
                        firstLoad = false;
                        $.each(mychart.series, function(index, val) {
                            val.visible = (index == 0);
                        });
                    }
                }
            },

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
