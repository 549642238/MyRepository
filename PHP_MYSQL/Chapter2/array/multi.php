<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Multidimensional Arrays</title>
	</head>
	<body>
		<!-- Script 2.4 - multi.php -->
		<?php
			#Script multi.php
			#Created 2016.8.18
			#Created czh
			#This script for multidimensional arrays
			// Create one array:
			$mexico = array(
				'YU' => 'Yucatan',
				'BC' => 'Baja California',
				'OA' => 'Oaxaca'
			);
			// Create another array:
			$us = array (
				'MD' => 'Maryland',
				'IL' => 'Illinois',
				'PA' => 'Pennsylvania',
				'IA' => 'Iowa'
			);
			// Create a third array:
			$canada = array (
				'QC' => 'Quebec',
				'AB' => 'Alberta',
				'NT' => 'Northwest Territories',
				'YT' => 'Yukon',
				'PE' => 'Prince Edward Island'
			);
			// Combine the arrays:
			$n_america = array(
				'Mexico' => $mexico,
				'United States' => $us,
				'Canada' => $canada
			);
			// Loop through the countries:
			foreach ($n_america as $country => $list) {
				echo "<h2>$country</h2><ul>";
				foreach($list as $k => $v){
					echo "<li>$k - $v</li>\n";
				}
				echo '</ul>';
			}
		?>
	</body>
</html>
