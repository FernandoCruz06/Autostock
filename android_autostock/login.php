<?php
$mysql = new mysqli("localhost", "root", "", "autostock");

if ($mysql->connect_error) {
    die("Error al conectar a la base de datos");
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $email = $_POST["email"];
    $password = $_POST["password"];

    // Prepare the SQL statement to fetch the user with the provided email and password
    $stmt = $mysql->prepare("SELECT * FROM users WHERE email = ? AND password = ?");
    $stmt->bind_param("ss", $email, $password);
    $stmt->execute();

    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        // User login successful
        echo "Login successful";
    } else {
        // Invalid credentials
        echo "Invalid credentials";
    }


    $stmt->close();
}

$mysql->close();
?>