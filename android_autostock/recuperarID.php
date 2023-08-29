<?php

$mysql = new mysqli("localhost", "root", "", "autostock");

if ($mysql->connect_error) {
    die("Conexión fallida: " . $mysql->connect_error);
}

$response = array();

$query = "SELECT DISTINCT id FROM products"; // Selecciona solo el campo 'id'
$result = $mysql->query($query);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $response[] = $row['id']; // Agrega solo el ID al array de respuesta
    }
} else {
    echo "No se encontraron registros.";
}

$mysql->close();

header('Content-Type: application/json');
echo json_encode($response);

?>
