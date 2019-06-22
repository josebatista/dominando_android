<?php
	
$servername = "localhost";
$username = "root";
$password = "";

$conn = new mysqli($servername, $username, $password);
if ($conn->connect_error) {
	die("Connection Failed: " . $conn->connection_error);
}

$sql = "CREATE DATABASE IF NOT EXISTS hotel_db";
if (!$conn->query($sql) === TRUE) {
	echo "erro ao criar o banco de dados: " . $conn->error;
}

$sql = "CREATE TABLE IF NOT EXISTS hotel_db.devices (
	user VARCHAR(255),
	registration_id VARCHAR(200) PRIMARY KEY
	)";
if($conn->query($sql) === FALSE) {
	echo "erro ao criar a tabela: " . $conn->error;
}

$action = $_POST['action'];
$user = $_POST['user'];

if ($action == "register") {
	$registrationId = $_POST['regId'];
	$stmt = $conn->prepare(
		"INSERT INTO hotel_db.devices (user, registration_id) VALUES (?, ?)"
	);
	$json = json_decode(file_get_contents('php://input'));
	$stmt->bind_param('ss', $user, $registrationId);
	$stmt->execute();
	$stmt->close();
} else if ($action == 'push') {
	$apiKey = "AAAAEybEJ8E:APA91bGiabpzzORwsdTccgI1d_Cw8aSFOM4IPkxvOGj_KR2_shhql-Fr2trtex0mB2PpFAj-9Mw82g6V3bqDhXlW0vsiGAZQ6BAZVAyhxmica_1-BdS8n2CfmNFvyzShWfnE4nbSePEE";
	$message = $_POST['message'];
	$url = "https://fcm.googleapis.com/fcm/send";
	$sql = "SELECT registration_id FROM hotel_db.devices WHERE user = '" . $user . "'";
	$result = $conn->query($sql);
	if ($result && $result->num_rows > 0) {
		while($row = $result->fetch_assoc()) {
			$regId = $row["registration_id"];
			$ch = curl_init($url);
			$json_data = '{ "to": "' . $regId . '", "data": { "message": "' . $message . '" } }';
			curl_setopt($ch, CURLOPT_POST, 1);
			curl_setopt($ch, CURLOPT_POSTFIELDS, $json_data);
			curl_setopt($ch, CURLOPT_HTTPHEADER,
				array("Content-Type: application/json",
				"Authorization: key=" . $apiKey)
			);
			$response = curl_exec($ch);
			echo $response;
		}
	}
}

$conn->close();

?>
