<?php # Script 12.6 - logout.php
	// This page lets the user logout.
	// If no cookie is present, redirect the user:
	session_start(); // Access the existing session.
	if (!isset($_SESSION['user_id'])) {
		// Need the functions:
		require ('login_functions.inc.php');
		redirect_user();
	} else { // Cancel the session:
		$_SESSION = array(); // Clear the variables.
		session_destroy(); // Destroy the session itself.
		setcookie ('PHPSESSID', '', time()-3600, '/', '', 0, 0); // Destroy the cookie.
	}
	/*
	if (!isset($_COOKIE['user_id'])) {
		// Need the function:
		require ('login_functions.inc.php');
		redirect_user();
	} else { // Delete the cookies:
		setcookie ('user_id', '', time()-3600);
		setcookie ('first_name', '', time()-3600);
	}
	*/
	// Set the page title and include the HTML header:
	$page_title = 'Logged Out!';
	include ('header.html');
	// Print a customized message:
	/*
	echo "<h1>Logged Out!</h1>
	<p>You are now logged out, {$_COOKIE['first_name']}!</p>";
	*/
	echo "<h1>Logged Out!</h1>
	<p>You are now logged out!</p>";
	include ('footer.html');
?>
