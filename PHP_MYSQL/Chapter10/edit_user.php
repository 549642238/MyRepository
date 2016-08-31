<?php # Script 10.3 - edit_user.php
	// This page is for editing a user record.
	// This page is accessed through view_users.php.
	$page_title = 'Edit a User';
	include ('header.html');
	echo '<h1>Edit a User</h1>';
	// Check for a valid user ID, through GET or POST:
	if ( (isset($_GET['id'])) && (is_numeric($_GET['id'])) ) { // From view_users.php
		$id = $_GET['id'];
	} elseif ( (isset($_POST['id'])) && (is_numeric($_POST['id'])) ) { // Form submission.
		$id = $_POST['id'];
	} else { // No valid ID, kill the script.
		echo '<p class="error">This page has been accessed in error.</p>';
		include ('footer.html');
		exit();
	}
	require_once ('mysql_connect.php');
	// Check if the form has been submitted:
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {
		$errors = array();
		// Check for a first name:
		if (empty($_POST['first_name'])) {
			$errors[] = 'You forgot to enter your first name.';
		} else {
			$fn = mysql_real_escape_string(trim($_POST['first_name']));
		}
		// Check for a last name:
		if (empty($_POST['last_name'])) {
			$errors[] = 'You forgot to enter your last name.';
		} else {
			$ln = mysql_real_escape_string(trim($_POST['last_name']));
		}
		// Check for an email address:
		if (empty($_POST['email'])) {
			$errors[] = 'You forgot to enter your email address.';
		} else {
			$e = mysql_real_escape_string (trim($_POST['email']));
		}
		if (empty($errors)) { // If everything's OK.
			// Test for unique email address:
			$q = "SELECT user_id FROM users WHERE email='$e' AND user_id != $id";
			$r = @mysql_query($q);
			if (mysql_num_rows($r) == 0) {
				// Make the query:
				$q = "UPDATE users SET first_name='$fn', last_name='$ln', email='$e' WHERE user_id=$id LIMIT 1";
				$r = @mysql_query ($q);
				if (mysql_affected_rows($dbc) == 1) { // If it ran OK.
					// Print a message:
					echo '<p>The user has been edited.</p>';
				} else { // If it did not run OK.
					echo '<p class="error">The user could not be edited due to a system error. We apologize for any inconvenience.</p>'; // Public message.
					echo '<p>' . mysql_error($dbc) . '<br />Query: ' . $q . '</p>'; // Debugging message.
				}
			} else { // Already registered.
				echo '<p class="error">The email address has already been registered.</p>';
			}
		} else { // Report the errors.
			echo '<p class="error">The following error(s) occurred:<br />';
			foreach ($errors as $msg) { // Print each error.
				echo " - $msg<br />\n";
			}
			echo '</p><p>Please try again.</p>';
		} // End of if (empty($errors)) IF.
	}// End of submit conditional.
	// Always show the form...
	// Retrieve the user's information:
	$q = "SELECT first_name, last_name, email FROM users WHERE user_id=$id";
	$r = @mysql_query ($q);
	if (mysql_num_rows($r) == 1) { // Valid user ID, show the form.
		// Get the user's information:
		$row = mysql_fetch_array ($r);
		// Create the form:
		echo '<form action="edit_user.php" method="post">
		<p>First Name: <input type="text" name="first_name" size="15" maxlength="15" value="' . $row[0] . '"/></p>
		<p>Last Name: <input type="text" name="last_name" size="15" maxlength="30" value="' . $row[1] . '"/></p>
		<p>Email Address: <input type="text" name="email" size="20" maxlength="60" value="' . $row[2] . '"/> </p>
		<p><input type="submit" name="submit" value="Submit" /></p>
		<input type="hidden" name="id" value="' . $id . '" />
		</form>';
	} else { // Not a valid user ID.
		echo '<p class="error">This page has been accessed in error.</p>';
	}
	mysql_close($dbc);
	include ('footer.html');
?>
