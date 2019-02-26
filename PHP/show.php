<?php
	$servername = "localhost";
	$username = "root";
	$password = "";
	$database = "healthcare";
	$conn = mysqli_connect($servername, $username, $password, $database);
	// if (!$conn)
	// 	die("Connection Failed:".mysqli_connect_error());
	// else
	// 	echo "Connected Successfully";

	$table = "health";
	$query = "SELECT * FROM ".$table;
	$result = mysqli_query($conn, $query);
	$rows = array();
	if (mysqli_num_rows($result) > 0){
		while($r = mysqli_fetch_assoc($result)) {
		    $rows[] = $r;
		}
	}
	$jsonarray['array'] = $rows;
	echo (json_encode($jsonarray));
?>