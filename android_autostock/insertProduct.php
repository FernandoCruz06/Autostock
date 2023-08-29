<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    
    $mysql = new mysqli("localhost", "root", "", "autostock");
    if ($mysql->connect_error) {
        die("Error al conectar a la base de datos: " . $mysql->connect_error);
    }

    $nombre = $_POST["nombre"];
    $categoria = $_POST["categoria"];
    $marca = $_POST["marca"];
    $cantidad = $_POST["cantidad"];
    $proveedor = $_POST["proveedor"];

    $query = "INSERT INTO products (nombre, categoria, marca, cantidad, proveedor)
              VALUES ('$nombre', '$categoria', '$marca', '$cantidad', '$proveedor')";

    if ($mysql->query($query) === TRUE) {
        echo "datas insertados";
    } else {
        echo "Error al insertar los datos: " . $mysql->error;
    }

    $mysql->close();
}
?>
