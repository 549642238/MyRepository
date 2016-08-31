<?php # Script 9.2 - mysqli_connect.php
// This file contains the database access information.
// This file also establishes a connection to MySQL,
// selects the database, and sets the encoding
DEFINE ('DB_USER', 'root');
DEFINE ('DB_PASSWORD', '12345');
DEFINE ('DB_HOST', 'localhost');
DEFINE ('DB_NAME', 'ch18');

// Make the connection:
$dbc = @mysql_connect (DB_HOST, DB_USER, DB_PASSWORD) OR die('Could not connect: ' . mysql_error($dbc));
mysql_select_db(DB_NAME);
mysql_query("set names 'utf8' ");
