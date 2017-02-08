-- phpMyAdmin SQL Dump
-- version 4.6.4deb1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Feb 07, 2017 at 08:54 PM
-- Server version: 5.7.17-0ubuntu0.16.10.1
-- PHP Version: 7.0.15-1+deb.sury.org~yakkety+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `TicTacToeDB`
--

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

CREATE TABLE `players` (
  `id` int(100) NOT NULL,
  `fname` varchar(255) NOT NULL,
  `lname` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `score` int(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `picpath` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `players`
--

INSERT INTO `players` (`id`, `fname`, `lname`, `username`, `score`, `password`, `picpath`) VALUES
(1, 'sara', 'shrief', 'sara', 2, '123456', 'person1.png'),
(2, 'salma', 'mohamed', 'salma', 70, '1234', 'person5.png'),
(3, 'noor', 'ahmed', 'noor', 30, '123123', 'person5.png'),
(4, 'mohamed', 'ahmed', 'mohamed', 100, '122223', 'person4.png'),
(5, 'kareem', 'karm', 'kareem', 120, '12345454', 'person3.png'),
(6, 'hadeer', 'mohamed', 'hader', 10, '123456', 'person2.png'),
(7, 'reham', 'mohamed', 'reham', 10, '12345', 'person7.png'),
(8, 'rahama', 'mohamed', 'reham', 111, '12345', 'person6.png'),
(10, 'rahama', 'mohamed', 'reham', 111, '12345', 'person1.png'),
(12, 'amira', 'mohiey', 'amira', 0, '12345', 'person2.png');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `players`
--
ALTER TABLE `players`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
