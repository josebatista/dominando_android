<?php
	if ($_FILES["hotel_photo"]["error"] > 0) {
		//Bad request
		http_response_code(400);
	} else {
		$id = $_POST["id"];
		$imagePath = "upload/" . $id . "___*";
		$files = glob($imagePath); //obtendo todos os arquivos que comecar com $imagePath

		$fileDestination = "upload/" . $id . "___" . uniqid(rand(), true) . ".jpg";
		move_uploaded_file(
			$_FILES["hotel_photo"]["tmp_name"],
			$fileDestination
		);
		$servername = "localhost";
		$username = "root";
		$password = "";
		$conn = new mysqli($servername, $username, $password);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		}

		$stmt = $conn->prepare("UPDATE hotel_db.Hotel SET photo_url=? WHERE id=?");
		$stmt->bind_param("si", $fileDestination, $id);
		$stmt->execute();
		$stmt->close();
		$jsonRetorno = array("id" => (int)$id, "photo_url" => $fileDestination);
		foreach( $files as $file) {
			if (is_file($file)) {
				unlink($file); //removendo os arquivos antigos daquele hotel (caso existam)
			}
		}
		echo json_encode($jsonRetorno);
		http_response_code(200);
		$conn->close();
	}
?>

