-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 29-08-2023 a las 19:33:25
-- Versión del servidor: 10.4.27-MariaDB
-- Versión de PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `autostock`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `categoria` varchar(255) DEFAULT NULL,
  `marca` varchar(255) DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `proveedor` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `products`
--

INSERT INTO `products` (`id`, `nombre`, `categoria`, `marca`, `cantidad`, `proveedor`) VALUES
(2, 'Balatas delanteras', 'Seguridad', 'BREMBO', 250, 'BREMBO'),
(5, 'Balatas delanteras', 'Frenos', 'BREMBO', 15, 'BREMBO'),
(6, 'Vujia', 'Akron ', 'Velvi', 123, 'Auto zone'),
(8, 'nxh', 'vdg', 'bfg', 0, 'itv'),
(9, 'Frenos ', 'Seguridad', 'Michelin', 42, 'Global Tires');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` int(5) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`) VALUES
(2, 'Miguel Gutierrez', 'MiguelG@gmail.com', '123'),
(6, 'Andros Miguel Gutiérrez ', 'miguelgutierrez@micorreo.upp.edu.mx', '111111111'),
(7, 'Andros ', 'gutierrez.androsmiguel23@gmail.com', '23'),
(9, 'Naydelin', 'Nay@gmail.com', '123'),
(10, 'Fulanito', 'Fulano@gmail.com', '111'),
(11, 'Karlita', 'Karlita@gmail.com', '222'),
(12, 'Fernando Jesús', 'fer@gmail.com', '12345'),
(13, 'Fernando Cruz', 'fernandoj.cm7u7@gmail.com', '777'),
(14, 'RyanRH', 'ruaryanhr@gmail.com', 'RyanRH'),
(15, 'Marcos Yamir ', 'myamir@upp.edu.mx', 'Marcos'),
(17, 'Oscar', 'Oscar@gmail.com', '12345');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
