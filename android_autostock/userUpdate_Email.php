<?php
$mysql = new mysqli("localhost", "root", "", "autostock");

if ($mysql->connect_error) {
    die("Error al conectar a la base de datos");
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Obtener los datos enviados desde la aplicación
    $email = $_POST['email'];
    $id = $_POST['id'];

    // Aquí debes agregar la lógica para validar la contraseña actual del usuario
    // y asegurarte de que coincida con la contraseña almacenada en la base de datos
    // También debes tomar medidas para proteger contra ataques de inyección SQL

    // Preparar la declaración SQL para actualizar el registro del usuario
    $stmt = $mysql->prepare("UPDATE users SET email = ? WHERE id = ?");
    $stmt->bind_param("ss", $email, $id);
    $stmt->execute();

    // Verificar si se realizó la actualización correctamente
    if ($stmt->affected_rows > 0) {
        // Se actualizó el registro exitosamente
        echo "Se ha actualizado el registro exitosamente";
    } else {
        // No se pudo actualizar el registro
        $response = array('error' => 'No se encontró el usuario o no se realizaron cambios');
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