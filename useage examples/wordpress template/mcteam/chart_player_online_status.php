<?php

function contains($str, $content, $ignorecase=true){
    if ($ignorecase){
        $str = strtolower($str);
        $content = strtolower($content);
    }  
    return strpos($content,$str) ? true : false;
}

$playername = $_GET['playername'];

$points = array();

$point_previous = array(
	'x' => $log_entries[0]['stamp'],
	'y' => 0,
);
/*
foreach ($log_entries as $log_entry)
{
	$point_previous['x'] = $log_entry['stamp'] - 1;
	$points[] = $point_previous;
	
	$point_previous = array(
		'x' => $log_entry['stamp'],
		'y' => $log_entry['count'],
	);
	$points[] = $point_previous;
}*/

foreach ($log_entries as $log_entry)
{
	if (contains(' '.$playername.' ', $log_entry['names']))
	{
		if ($point_previous['y'] == 0)
		{
			// This point changes the player to online
			$point_previous['x'] = $log_entry['stamp'] - 1;
			$points[] = $point_previous;
			
			$point_previous = array(
				'x' => $log_entry['stamp'],
				'y' => 1,
			);
			$points[] = $point_previous;
		}
	}
	else
	{
		if ($point_previous['y'] == 1)
		{
			// This point changes the player to offline
			$point_previous['x'] = $log_entry['stamp'] - 1;
			$points[] = $point_previous;
			
			$point_previous = array(
				'x' => $log_entry['stamp'],
				'y' => 0,
			);
			$points[] = $point_previous;
		}
	}
}

// And the last state continues into the now
$point_last = end($points);
$point_last['x'] = $now_stamp;
$points[] = $point_last;

?>
<div id="chart_player_online_status" style="width: 100%; height: 300px"></div>
<script type="text/javascript">
var chart_player_online_status = new Highcharts.Chart({
chart: {
	renderTo: 'chart_player_online_status',
	defaultSeriesType: 'area',
	zoomType: 'x',
	spacingRight: 20
},
title: {
	text: 'Online Status For <?php echo $playername; ?>'
},
xAxis: {
	type: 'datetime'
},
yAxis: {
	title: {
		text: null
	},
	min: 0,
	max: 1
},
tooltip: {
	enabled: true,
	formatter: function() {
		return '<b>Online:'+ this.y +' </b><br/>'+Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x);
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