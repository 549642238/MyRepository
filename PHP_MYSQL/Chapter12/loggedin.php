<?php # Script 12.4 - loggedin.php
	// The user is redirected here from login.php.
	// If no cookie is present, redirect the user:
	session_start(); // Start the session.
	if (!isset($_SESSION['user_id'])) {
		// Need the functions:
		require ('login_functions.inc.php');
		redirect_user();
	}
	/*
	if (!isset($_COOKIE['user_id'])) {
		// Need the functions:
		require ('login_functions.inc.php');
		redirect_user();
	}
	*/
	// Set the page title and include the HTML header:
	$page_title = 'Logged In!';
	include ('header.html');
	// Print a customized message:
	/*
	echo "<h1>Logged In!</h1>
	<p>You are now logged in, {$_COOKIE ['first_name']}!</p>
	<p><a href=\"logout.php\">Logout</a></p>";
	*/
	echo "<h1>Logged In!</h1>
	<p>You are now logged in, {$_SESSION ['first_name']}!</p>
	<p><a href=\"logout.php\">Logout</a></p>";
	include ('footer.html');
?>