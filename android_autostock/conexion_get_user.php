<?php
function retornarConexion() {
    $server="localhost";
    $usuario="root";
    $clave="";
    $base="autostock";
    $con=mysqli_connect($server,$usuario,$clave,$base) or die("Error al conectar a la base de datos: ") ;
    mysqli_set_charset($con,'utf8'); 
    return $con;
}
?>