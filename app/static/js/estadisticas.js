fetch('/api/actividades-por-dia')
    .then(res => res.json())
    .then(data => {
    Highcharts.chart('line-chart', {
        chart: { type: 'line' },
        title: { text: 'Actividades por Día' },
        xAxis: {
        categories: data.dias,
        title: { text: 'Día' }
        },
        yAxis: {
        title: { text: 'Cantidad de Actividades' }
        },
        series: [{
        name: 'Actividades',
        data: data.cantidades
        }]
    });
});

fetch('/api/actividades-por-tipo')
    .then(res => res.json())
    .then(data => {
    Highcharts.chart('pie-chart', {
        chart: { type: 'pie' },
        title: { text: 'Actividades por Tipo' },
        tooltip: {
        pointFormat: '{series.name}: <b>{point.y}</b>'
        },
        plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
            enabled: true,
            format: '<b>{point.name}</b>: {point.y}'
            }
        }
        },
        series: [{
        name: 'Cantidad',
        colorByPoint: true,
        data: data.tipos
        }]
    });
});

// Gráfico de barras - Actividades por mes y franja horaria
fetch('/api/actividades-por-mes')
    .then(res => res.json())
    .then(data => {
    Highcharts.chart('bar-chart', {
        chart: { type: 'column' },
        title: { text: 'Actividades por Mes y Franja Horaria' },
        xAxis: {
        categories: data.meses,
        title: { text: 'Mes' }
        },
        yAxis: {
        min: 0,
        title: { text: 'Cantidad de Actividades' }
        },
        tooltip: {
        shared: true
        },
        plotOptions: {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
        },
        series: [
        { name: 'Mañana', data: data.manana },
        { name: 'Mediodía', data: data.mediodia },
        { name: 'Tarde', data: data.tarde }
        ]
    });
    });
