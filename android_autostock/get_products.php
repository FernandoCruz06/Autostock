<?php
$mysql = new mysqli("localhost", "root", "", "autostock");

if ($mysql->connect_error) {
    die("Error al conectar a la base de datos: " . $mysql->connect_error);
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $query = "SELECT * FROM products";
    $result = $mysql->query($query);

    $response = array();

    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $item = array(
                "id" => $row["id"],
                "nombre" => $row["nombre"],
                "categoria" => $row["categoria"],
                "marca" => $row["marca"],
                "cantidad" => $row["cantidad"],
                "proveedor" => $row["proveedor"]
            );
            $response[] = $item;
        }
    }

    header('Content-Type: application/json');
    echo json_encode(array("exito" => 1, "datos" => $response));
} else {
    echo "Método no permitido";
}

$mysql->close();
?>



