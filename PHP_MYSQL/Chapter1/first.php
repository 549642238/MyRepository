<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title> Basic PHP Page</title>
	</head>
	<body>
		<!-- Script 1.2 - first.php -->
		<p>This is standard HTML.</p>
		<?php
			#Script first.php
			#Created 2016.8.17
			#Created czh
			#This script for testing
			echo '<p>nmb ri <b>nisia!</b></p>';
			/*
			// varity
			$file = $_SERVER['SCRIPT_FILENAME'];
			$user = $_SERVER['HTTP_USER_AGENT'];
			$server = $_SERVER['SERVER_SOFTWARE'];
			print "<p>You are running the file:<br/><b>$file</b>.</p>";
			print "<p>You are viewing this page using:<br/><b>$user</b>.</p>";
			print "<p>This server is running:<br/><b>$server</b>.</p>";
			*/
			/*
			// string
			$first_name = "Zhou";
			$last_name = "Runfa";
			$name = $first_name.' '.$last_name;
			print "<p>My name is <em>$name</em></p>";
			*/
			/* number
			$n = 3.1415926;
			$m = 200;
			$l = number_format($m*round($n,2),2);
			print "<p>Result = $l</p>";
			*/
			/*
			// constant
			define('NAME',123);
			print '<p>I\'m '.NAME.'</p>';
			*/
			/*
			// ' and "
			$a = 1;
			print "<p>das$a</p>";
			print '<p>das$a</p>';
			*/
		?>
	</body>
</html>
