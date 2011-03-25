<?php

if ( ! is_admin() ) { // instruction to only load if it is not the admin area
   // register your script location, dependencies and version
   wp_register_script('highcharts',
       get_bloginfo('stylesheet_directory') . '/highcharts.js',
       array('jquery'),
       '2.1.4' );
   // enqueue the script
   wp_enqueue_script('highcharts');
}

$mc_online_log_mysqli = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, 'mcteam_mainserver');
$result = $mc_online_log_mysqli->query("SELECT * FROM onlinelog");

$log_entries = array();
while ($row = $result->fetch_array())
{
	$log_entries[] = array(
		'stamp' => strtotime($row['timestamp'])*1000,
		'count' => (int)$row['playercount'],
		'names' => $row['playernames'],
	);
}

$result = $mc_online_log_mysqli->query("SELECT NOW() as timestamp FROM onlinelog");
$row = $result->fetch_array();
$now_stamp = strtotime($row['timestamp'])*1000;

$mc_online_log_mysqli->close();


get_header(); ?>

		<div id="container">
			<div id="content" role="main">

			<?php
			/* Run the loop to output the page.
			 * If you want to overload this in a child theme then include a file
			 * called loop-page.php and that will be used instead.
			 */
			//get_template_part( 'loop', 'page' );
			?>

			<?php 
			include(STYLESHEETPATH . '/chart_players_online_count.php');
			if (isset($_GET['playername']))
			{
				include(STYLESHEETPATH . '/chart_player_online_status.php');
			}
			?>
			
			<form method="get">
			<input name="playername" type="text" value="<?php echo isset($_GET['playername']) ? htmlspecialchars($_GET['playername']) : 'Enter Player Name';?>" />
			<input type="submit" value="Search" />
			</form>
			
			</div><!-- #content -->
		</div><!-- #container -->

<?php get_sidebar(); ?>
<?php get_footer(); ?>
