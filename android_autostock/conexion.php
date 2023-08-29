<?php
    $mysql= new mysqli("localhost","root","","autostock");
    if($mysql->connect_error) {
        die("Error al conectar a la base de datos: " . $mysqli->connect_error);
    }else {
        echo ("Felicidades, conexión exitosa");
    }
?>