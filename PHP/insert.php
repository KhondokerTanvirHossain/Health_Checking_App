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
	$bmp = "";
	$so2 = "";
	$tem = "";
	if (isset($_POST['bmp']))
		$bmp = $_POST['bmp'];
	if (isset($_POST['so2']))
		$so2 = $_POST['so2'];
	if (isset($_POST['tem']))
		$tem = $_POST['tem'];
	$json = array();
	if (!empty($bmp) && !empty($so2) && !empty($tem)){
		$query = "insert into ".$table." (bpm, so2, tem) values ('$bmp', '$so2', '$tem')";
		$inserted = mysqli_query($conn, $query);
		if ($inserted != null){
			$json['success'] = 1;
			$json['message'] = "Successfully Inserted";
		}
		else{
			$json['success'] = 0;
			$json['message'] = "Data insertion failed";
		}
	}
	else{
		$json['success'] = -1;
		$json['message'] = "Data Not Found";
	}
	echo json_encode($json);	
?>