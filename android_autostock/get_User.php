<?php
header('Content-Type: application/json');

require("conexion_get_user.php");

$conexion = retornarConexion();

$datos = mysqli_query($conexion, "select id,name,email,password from users where email=$_GET[email]");
$resultado = mysqli_fetch_all($datos, MYSQLI_ASSOC);
echo json_encode($resultado);