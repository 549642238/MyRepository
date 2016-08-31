<?php # Script 9.4 - view_users.php
	$page_title = 'View the Current Users';
	include('header.html');
	echo '<h1>Registered Users</h1>';
	require('mysql_connect.php');
	$q = "Select Concat(last_name,',',first_name) as name,DATE_FORMAT(registration_date,'%M %d,%Y') as dr,user_id from users order by registration_date asc";
	$r = @mysql_query($q);
	$num = mysql_num_rows($r);
	if($r){
		if($num > 0){
			echo "<p>There are currently $num registered users.</p>";
			echo '<table align = "center" cellspace = "3" width = "75%">
			<tr><td align = "left"><b>Edit</b></td><td align = "left"><b>Delete</b></td><td align = "left"><b>Name</b></td><td align = "left"><b>Date Registered</b></td></tr>';
			while($row = mysql_fetch_array($r)){
				echo '<tr><td align = "left"><a href = "edit_user.php?id='.$row['user_id'].'">Edit</a></td><td align = "left"><a href = "delete_user.php?id='.$row['user_id'].'">Delete</a></td><td align = "left">'.$row['name'].'</td><td align = "left">'.$row['dr'].'</td></tr>';
			}
			echo '</table>';
			mysql_free_result($r);
		}else{
			echo '<p class ="error">There are currently no registered users.</p>';
		}
	}else{
		echo '<p>'.mysql_error($dbc).'<br/><br/>Query:'.$q.'</p>';
	}
	mysql_close($dbc);
	include('footer.html');
?>
