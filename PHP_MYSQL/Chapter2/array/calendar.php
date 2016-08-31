<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Calendar</title>
	</head>
	<body>
		<form action = "calendar.php" method = "post">
		<!-- Script 2.3 - calendar.php -->
		<?php
			#Script calendar.php
			#Created 2016.8.18
			#Created czh
			#This script for calendar
			echo "<form action = \"calendar.php\" method = \"post\">";
			$months = array(1 => "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
			$days = range(1,31);
			$years = range(2000,2050);
			echo "<select name = \"month\">";
			foreach($months as $key => $value){
				echo "<option value = \"$key\"> $value</option>\n";
			}
			echo "</select>";
			echo "<select name=\"day\">";
			for($day = 1;$day<=31;$day++){
				echo "<option value = \"$day\"> $day</option>\n";
			}
			/*
			foreach ($days as $value) {
				echo "<option value=\"$value\"> $value</option>\n";
			}
			*/
			echo '</select>';
			echo '<select name="year">';
			foreach ($years as $value) {
			echo "<option value=\"$value\"> $value</option>\n";
			}
			echo '</select>';
		?>
		</form>
	</body>
</html>
