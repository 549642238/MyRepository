<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Form Feedback</title>
		<style type = "text/css" title = "text/css" media = "all">
		label{
			font-weight: bold;
			color: #300ACC;
		}
		.error {
			font-weight: bold;
			color: #C00;
		}
		</style>
	</head>
	<body>
		<!-- Script 2.2 - handle_form.php -->
		<?php
			#Script first.php
			#Created 2016.8.18
			#Created czh
			#This script for form handling
			$name = $_REQUEST['name'];
			$age = $_REQUEST['age'];
			$gender = $_REQUEST['gender'];
			$email = $_REQUEST['email'];
			$comments = $_REQUEST['comments'];
			if(empty($name)){
				print '<p class = "error">Name cannot be null!</p>';
			}else{
				if(empty($comments)){		// empty => NULL
					print '<p class = "error">No Comments!</p>';
				}else{
					print "<p>Thank you, <b>$name</b>, for the following comments:<br/><tt>$comments</tt></p>";
				}
			}
			if($gender == "M"){
				print "<p>Hello,Mr!</p>";
			}else if($gender == "F"){
				print "<p>Hello,Mz</p>";
			}else{
				$_REQUEST['gender'] = NULL;
				$gender = NULL;
				print '<p class = "error">You forgot to set your gender!</p>';
			}
			if($email == NULL){
				print '<p class = "error">Your email address is null!</p>';
			}else{
				echo "<p>Your email is $email.</p>";
			}
			if(!isset($age)){		// !isset() => NULL,""
				echo '<p class = "error">Age is null!</p>';
			}else{
				print "<p>age:$age</p>";
			}
		?>
	</body>
</html>
