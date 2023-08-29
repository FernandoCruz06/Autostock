<?php
$mysql = new mysqli("localhost", "root", "", "autostock");

if ($mysql->connect_error) {
    die("Error al conectar a la base de datos");
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Obtener el email y la nueva contraseña enviados desde la aplicación
    $email = $_POST['email'];
    $newPassword = $_POST['newPassword'];

    // Aquí puedes agregar la lógica para actualizar la contraseña del usuario en la base de datos
    // Por ejemplo, supongamos que tienes una tabla "users" con una columna "email" y una columna "password"

    // Preparar la declaración SQL para actualizar la contraseña del usuario
    $stmt = $mysql->prepare("UPDATE users SET password = ? WHERE email = ?");
    $stmt->bind_param("ss", $newPassword, $email);
    $stmt->execute();

    // Verificar si se realizó la actualización correctamente
    if ($stmt->affected_rows > 0) {
        // Se actualizó la contraseña exitosamente
        echo "Se ha actualizado la contraseña exitosamente";
    } else {
        // No se pudo actualizar la contraseña
        $response = array('error' => 'No se pudo actualizar la contraseña');
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
