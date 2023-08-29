<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {

    require_once 'conexion.php'; // Llama a la conexión a la base de datos
    $name = $_POST["name"];
    $email = $_POST["email"];
    $password = $_POST["password"];

    $query = "INSERT INTO users (name, email, password)
              VALUES ('$name', '$email', '$password')";

    $resultado = $mysql->query($query);

    if ($resultado == true) {
        echo (" El usuario se insertó de forma exitosa");
    } else {
        echo ("Error al insertar el usuario");
    }
}
?>
