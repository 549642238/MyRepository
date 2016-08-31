<?php # Script 18.8 - login.php
	// This is the login page for the site.
	require ('config.inc.php');
	$page_title = 'Login';
	include ('header.html');
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {
		require (MYSQL);
		// Validate the email address:
		if (!empty($_POST['email'])) {
			$e = mysql_real_escape_string ($_POST['email']);
		} else {
			$e = FALSE;
			echo '<p class="error">You forgot to enter your email address!</p>';
		}
		// Validate the password:
		if (!empty($_POST['pass'])) {
			$p = mysql_real_escape_string ($_POST['pass']);
		} else {
			$p = FALSE;
			echo '<p class="error">You forgot to enter your password!</p>';
		}
		if ($e && $p) { // If everything's OK.
			// Query the database:
			$q = "SELECT user_id, first_name, user_level FROM users WHERE (email='$e' AND pass=SHA1('$p')) AND active IS NULL";
			$r = mysql_query ($q) or trigger_error("Query: $q\n<br />MySQL Error: " . mysql_error($dbc));
			if (@mysql_num_rows($r) == 1) { // A match was made.
				// Register the values:
				$_SESSION = mysql_fetch_array ($r, MYSQLI_ASSOC);
				mysql_free_result($r);
				mysql_close($dbc);
				// Redirect the user:
				$url = BASE_URL . 'index.php'; // Define the URL.
				ob_end_clean(); // Delete the buffer.
				header("Location: $url");
				exit(); // Quit the script.
			} else { // No match was made.
				echo '<p class="error">Either the email address and password entered do not match those on file or you have not yet activated your account.</p>';
			}
		} else { // If everything wasn't OK.
			echo '<p class="error">Please try again.</p>';
		}
		mysql_close($dbc);
	} // End of SUBMIT conditional.
?>
<h1>Login</h1>
<p>Your browser must allow cookies in order to log in.</p>
<form action="login.php" method="post">
	<fieldset>
	<p><b>Email Address:</b> <input type="text" name="email" size="20" maxlength="60" /></p>
	<p><b>Password:</b> <input type="password" name="pass" size="20" maxlength="20" /></p>
	<div align="center"><input type="submit" name="submit" value="Login" /></div>
	</fieldset>
</form>
<?php include ('footer.html'); ?>
