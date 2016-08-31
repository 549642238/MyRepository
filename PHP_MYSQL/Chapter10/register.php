<?php # Script 9.3 - register.php
$page_title = 'Register';
include ('header.html');
if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$errors = array();
	require ('mysql_connect.php');		//Warning for mysql_real_escape_string
	if(empty($_POST['account'])){
		$errors[] = 'You forgot to enter your account.';
	}else{
		if(!is_numeric($_POST['account'])){
			$errors[] = 'Account must be pure number.';
		}else{
			$ac = mysql_real_escape_string(trim($_POST['account']));
			if(!$ac){
				$errors[] = 'Wrong format in account.';
			}
		}
	}
	if(empty($_POST['first_name'])){
		$errors[] = 'You forgot to enter your first name.';
	}else{
		$fn = mysql_real_escape_string(trim($_POST['first_name']));
		if(!$fn){
			$errors[] = 'Wrong format in first_name.';
		}
	}
	if(empty($_POST['last_name'])){
		$errors[] = 'You forgot to enter your last name.';
	}else{
		$ln = mysql_real_escape_string(trim($_POST['last_name']));
		if(!$ln){
			$errors[] = 'Wrong format in last_name.';
		}
	}
	if(empty($_POST['email'])){
		$errors[] = 'You forgot to enter your email address.';
	}else{
		$e = mysql_real_escape_string(trim($_POST['email']));
		if(!$e){
			$errors[] = 'Wrong format in email.';
		}
	}
	if(empty($_POST['pass1'])){
		$errors[] = 'You forgot to enter your password.';
	}else{
		if($_POST['pass1'] == $_POST['pass2']){
			$p = mysql_real_escape_string(trim($_POST['pass1']));
			if(!$p){
				$errors[] = 'Wrong format in password.';
			}
		}else{
			$errors[] = 'Your password did not match the confirmed password.';
		}
	}
	if(empty($errors)){
		require('mysql_connect.php');
		$q = "SELECT user_id from users where user_id = $ac";
		$r = @mysql_query($q);
		$num = mysql_num_rows($r);
		if($num == 1){
			echo '<h1>Account repeat!</h1>';
			mysql_close($dbc);
			include('footer.html');
			exit();
		}
		$q = "INSERT INTO users(user_id,first_name,last_name,email,pass,registration_date) VALUES('$ac','$fn','$ln','$e',SHA1('$p'),NOW())";
		$r = @mysql_query($q);
		if($r){
			echo '<h1>Thank you!</h1>
			<p>You are now registered.In Chapter 12 you will actually be able to log in!</p><p><br/></p>';
		}else{
			echo '<h1>System error</h1>
			<p class = "error">You could not be registered due to a system error. We apologize for any inconvenience.</p>';
			echo '<p>'.mysqli_error($dbc).'<br/><br/>Query:'.$q.'</p>';
		}
		mysql_close($dbc);
		include('footer.html');
		exit();
	}else{
		echo '<h1>Error!</h1>
		<p class = "error">the following error(s) occurred:<br/>';
		foreach($errors as $msg){
			echo " - $msg<br/>\n";
		}
		echo '</p><p>Please try again.</p><p><br/></p>';
	}
}
?>
<h1>Register</h1>
<form action = "register.php" method = "post">
	<p>Account: <input type="text" name="account" size="15" maxlength="20" value="<?php if(isset($_POST['account'])) echo $_POST['account']; ?>" /></p>
	<p>First Name: <input type="text" name="first_name" size="15" maxlength="20" value="<?php if(isset($_POST['first_name'])) echo $_POST['first_name']; ?>" /></p>
	<p>Last Name: <input type="text" name="last_name" size="15" maxlength="40" value="<?php if(isset($_POST['last_name'])) echo $_POST['last_name']; ?>" /></p>
	<p>Email Address: <input type="text" name="email" size="20" maxlength="60" value="<?php if(isset($_POST['email'])) echo $_POST['email']; ?>" /> </p>
	<p>Password: <input type="password" name="pass1" size="10" maxlength="20" value="<?php if(isset($_POST['pass1'])) echo $_POST['pass1']; ?>" /></p>
	<p>Confirm Password: <input type="password" name="pass2" size="10" maxlength="20" value="<?php if (isset($_POST['pass2'])) echo $_POST['pass2']; ?>" /></p>
	<p><input type="submit" name="submit" value="Register" /></p>
</form>
<?php include ('footer.html') ?>
