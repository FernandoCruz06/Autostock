<?php
$mysql = new mysqli("localhost", "root", "", "autostock");

if ($mysql->connect_error) {
    die("Error al conectar a la base de datos");
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Obtener los datos enviados desde la aplicación
    $id = $_POST["id"];

    // Preparar la declaración SQL para eliminar el producto
    $stmt = $mysql->prepare("DELETE FROM products WHERE id = ?");
    $stmt->bind_param("s", $id);
    $stmt->execute();

    // Verificar si se realizó la eliminación correctamente
    if ($stmt->affected_rows > 0) {
        // Se eliminó el producto exitosamente
        echo "data Eliminado";
    } else {
        // No se pudo eliminar el producto
        $response = array('error' => 'No se encontró el producto o no se realizaron cambios');
        echo json_encode($response);
    }

    $stmt->close();
} else {
    // Si no se ha enviado una solicitud POST, devolver un error
    $response = array('error' => 'Método no válido');
    echo json_encode($response);
}

$mysql->close();
?>


