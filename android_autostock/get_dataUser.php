<?PHP
$hostname_localhost ="localhost";
$database_localhost ="autostock";
$username_localhost ="root";
$password_localhost ="";

$json=array();

	if(isset($_GET["email"])){
		$email=$_GET["email"];
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$consulta="select id,name,email,password from users where email= '{$email}'";
		$resultado=mysqli_query($conexion,$consulta);
			
		if($registro=mysqli_fetch_array($resultado)){
			$json['users'][]=$registro;
		//	echo $registro['id'].' - '.$registro['nombre'].' - '.$registro['profesion'].'<br/>';
		}else{
			$resultar["id"]=0;
			$resultar["name"]='no registra';
			$resultar["email"]='no registra';
            $resultar["password"]='no registra';
			$json['users'][]=$resultar;
		}
		
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		$resultar["success"]=0;
		$resultar["message"]='Ws no Retorna';
		$json['users'][]=$resultar;
		echo json_encode($json);
	}
?>