<?php
$points = array();

$point_previous = array(
	'x' => $log_entries[0]['stamp'],
	'y' => $log_entries[0]['count'],
);

foreach ($log_entries as $log_entry)
{
	$point_previous['x'] = $log_entry['stamp'] - 1;
	$points[] = $point_previous;
	
	$point_previous = array(
		'x' => $log_entry['stamp'],
		'y' => $log_entry['count'],
	);
	$points[] = $point_previous;
}

// And the last state continues into the now
$point_last = end($points);
$point_last['x'] = $now_stamp;
$points[] = $point_last;

?>
<div id="chart_players_online_count" style="width: 100%; height: 300px"></div>
<script type="text/javascript">
var chart_players_online_count = new Highcharts.Chart({
chart: {
	renderTo: 'chart_players_online_count',
	defaultSeriesType: 'area',
	zoomType: 'x',
	spacingRight: 20
},
title: {
	text: 'Players Online'
},
xAxis: {
	type: 'datetime'
},
yAxis: {
	title: {
		text: null
	},
	min: 0
},
tooltip: {
	enabled: true,
	formatter: function() {
		return '<b>'+ this.y +' Online</b><br/>'+Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x);
	}
},
legend: {
	enabled: false
},
plotOptions: {
	area: {
		lineWidth: 1,
		marker: {
			enabled: false,
				states: {
				hover: {
					enabled: true,
					radius: 4
				}
			}
		},
		shadow: false,
		states: {
			hover: {
				lineWidth: 1                  
			}
		}
	}
},
exporting: {
	enabled: false
},
series: [{
	name: 'points',
	data: <?php echo json_encode($points); ?>
}]
});
</script>