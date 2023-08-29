<?php
$mysql = new mysqli("localhost", "root", "", "autostock");

if ($mysql->connect_error) {
    die("Error al conectar a la base de datos");
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Obtener los datos enviados desde la aplicación
    $id = $_POST["id"];
    $nombre = $_POST["nombre"];
    $cantidad = $_POST["cantidad"];

    // Verificar y obtener los valores opcionales si están definidos
    $categoria = isset($_POST["categoria"]) ? $_POST["categoria"] : null;
    $marca = isset($_POST["marca"]) ? $_POST["marca"] : null;
    $proveedor = isset($_POST["proveedor"]) ? $_POST["proveedor"] : null;

    // Obtener el registro actual para no sobrescribir los valores que no se modificaron
    $stmt_select = $mysql->prepare("SELECT nombre, categoria, marca, cantidad, proveedor FROM products WHERE id = ?");
    $stmt_select->bind_param("s", $id);
    $stmt_select->execute();
    $stmt_select->bind_result($nombre_actual, $categoria_actual, $marca_actual, $cantidad_actual, $proveedor_actual);
    $stmt_select->fetch();
    $stmt_select->close();

    // Verificar y actualizar los valores que se modificaron
    if (empty($nombre)) {
        $nombre = $nombre_actual;
    }
    if (empty($categoria)) {
        $categoria = $categoria_actual;
    }
    if (empty($marca)) {
        $marca = $marca_actual;
    }
    if (empty($cantidad)) {
        $cantidad = $cantidad_actual;
    }
    if (empty($proveedor)) {
        $proveedor = $proveedor_actual;
    }

    // Preparar la declaración SQL para actualizar el registro del producto
    $stmt = $mysql->prepare("UPDATE products SET nombre = ?, categoria = ?, marca = ?, cantidad = ?, proveedor = ? WHERE id = ?");
    $stmt->bind_param("ssssss", $nombre, $categoria, $marca, $cantidad, $proveedor, $id);
    $stmt->execute();

    // Verificar si se realizó la actualización correctamente
    if ($stmt->affected_rows > 0) {
        // Se actualizó el registro exitosamente
        echo "Se ha actualizado el registro exitosamente";
    } else {
        // No se pudo actualizar el registro
        $response = array('error' => 'Error al actualizar');
        echo json_encode($response);
    }

    $stmt->close();
} else {
    // Si no se ha enviado una solicitud POST, devolver un error
    $response = array('error' => 'Error al actualizar');
    echo json_encode($response);
}

$mysql->close();
?>
