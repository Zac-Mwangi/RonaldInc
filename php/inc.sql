-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 07, 2021 at 11:43 PM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inc`
--

-- --------------------------------------------------------

--
-- Table structure for table `estate`
--

CREATE TABLE `estate` (
  `estate_id` int(200) NOT NULL,
  `estate_name` varchar(200) NOT NULL,
  `location` int(200) NOT NULL,
  `landlord` int(200) NOT NULL,
  `commission` varchar(200) NOT NULL,
  `status` int(200) NOT NULL DEFAULT 1 COMMENT '0 (inactive), 1 (active)',
  `date_created` timestamp(6) NOT NULL DEFAULT current_timestamp(6),
  `added_by` int(200) NOT NULL,
  `deactivation_date` timestamp(6) NULL DEFAULT NULL,
  `deactivated_by` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `estate`
--

INSERT INTO `estate` (`estate_id`, `estate_name`, `location`, `landlord`, `commission`, `status`, `date_created`, `added_by`, `deactivation_date`, `deactivated_by`) VALUES
(1, 'Savanna', 0, 0, '0', 1, '0000-00-00 00:00:00.000000', 0, NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `payment_transactions`
--

CREATE TABLE `payment_transactions` (
  `payment_id` int(200) NOT NULL,
  `tenant_id` int(200) NOT NULL,
  `Amount` int(200) NOT NULL,
  `balance` int(200) NOT NULL,
  `year` int(200) NOT NULL,
  `month` int(200) NOT NULL,
  `payment_method` varchar(200) NOT NULL,
  `reference` varchar(200) NOT NULL,
  `date_processed` date NOT NULL DEFAULT current_timestamp(),
  `processed_by` int(200) NOT NULL,
  `approved_status` int(200) NOT NULL COMMENT 'mpesa and bank',
  `approved_by` int(200) NOT NULL,
  `approve_date` timestamp(6) NULL DEFAULT NULL,
  `type` int(200) NOT NULL DEFAULT 0 COMMENT '0 rent, 1 reverse',
  `reverse_status` int(200) NOT NULL DEFAULT 0 COMMENT 'o not reversed, i reversed',
  `reverse_date` date DEFAULT NULL,
  `reversed_for` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payment_transactions`
--

INSERT INTO `payment_transactions` (`payment_id`, `tenant_id`, `Amount`, `balance`, `year`, `month`, `payment_method`, `reference`, `date_processed`, `processed_by`, `approved_status`, `approved_by`, `approve_date`, `type`, `reverse_status`, `reverse_date`, `reversed_for`) VALUES
(1, 3, 200, -1800, 2021, 10, '2', 'GH', '2021-10-07', 1, 0, 0, NULL, 0, 0, NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `room_id` int(200) NOT NULL,
  `room_number` varchar(200) NOT NULL,
  `estate_id` int(200) NOT NULL,
  `monthly_price` int(200) NOT NULL,
  `deposit_price` int(200) NOT NULL,
  `room_type` int(200) NOT NULL,
  `status` int(200) NOT NULL DEFAULT 0 COMMENT '0 is an empty room',
  `date_registered` timestamp(6) NOT NULL DEFAULT current_timestamp(6) ON UPDATE current_timestamp(6),
  `added_by` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`room_id`, `room_number`, `estate_id`, `monthly_price`, `deposit_price`, `room_type`, `status`, `date_registered`, `added_by`) VALUES
(1, '101', 1, 2000, 1220, 2, 1, '2021-10-07 19:55:44.601231', 1),
(2, '192', 1, 250, 280, 3, 0, '2021-10-07 19:50:29.160295', 1);

-- --------------------------------------------------------

--
-- Table structure for table `sys_meta`
--

CREATE TABLE `sys_meta` (
  `meta_id` int(11) NOT NULL,
  `meta_key` varchar(255) DEFAULT NULL,
  `meta_value_int` int(200) NOT NULL,
  `meta_value` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `sys_meta`
--

INSERT INTO `sys_meta` (`meta_id`, `meta_key`, `meta_value_int`, `meta_value`) VALUES
(10, 'status', 0, 'Inactive'),
(11, 'status', 1, 'Active'),
(12, 'room_type', 1, 'Single'),
(13, 'room_type', 2, 'Bedsitter'),
(14, 'room_type', 3, 'One Bedroom'),
(15, 'room_type', 4, 'Two Bedroom'),
(16, 'room_type', 5, 'Business Premises'),
(17, 'room_status', 0, 'Empty'),
(18, 'room_status', 1, 'Occupied'),
(19, 'room_status', 2, 'Inactive'),
(20, 'deposit_status', 1, 'Paid'),
(21, 'deposit_status', 2, 'Unpaid'),
(22, 'payment_method', 1, 'Cash Payment'),
(23, 'payment_method', 2, 'Mpesa'),
(24, 'payment_method', 3, 'KCB'),
(25, 'payment_method', 4, 'Equity'),
(29, 'company name', 0, 'Ronald Inc.');

-- --------------------------------------------------------

--
-- Table structure for table `tenants`
--

CREATE TABLE `tenants` (
  `tenant_id` int(200) NOT NULL,
  `tenant_name` varchar(200) NOT NULL,
  `alt_name_1` varchar(200) NOT NULL,
  `alt_name_2` varchar(200) NOT NULL,
  `id_number` int(200) NOT NULL,
  `phone_number` varchar(200) NOT NULL,
  `deposit_amount` int(200) NOT NULL,
  `estate_id` int(200) NOT NULL,
  `room_id` int(200) NOT NULL,
  `date_added` timestamp(6) NOT NULL DEFAULT current_timestamp(6) ON UPDATE current_timestamp(6),
  `added_by` int(200) NOT NULL,
  `status` int(200) NOT NULL DEFAULT 1 COMMENT 'active(1)',
  `date_registered` datetime(6) NOT NULL DEFAULT current_timestamp(6),
  `deactivation_date` date DEFAULT NULL,
  `deactivated_by` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tenants`
--

INSERT INTO `tenants` (`tenant_id`, `tenant_name`, `alt_name_1`, `alt_name_2`, `id_number`, `phone_number`, `deposit_amount`, `estate_id`, `room_id`, `date_added`, `added_by`, `status`, `date_registered`, `deactivation_date`, `deactivated_by`) VALUES
(3, 'Tt', '', '', 0, '', 0, 1, 1, '2021-10-07 19:55:44.568332', 1, 1, '2021-10-07 22:55:44.568332', NULL, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `estate`
--
ALTER TABLE `estate`
  ADD PRIMARY KEY (`estate_id`);

--
-- Indexes for table `payment_transactions`
--
ALTER TABLE `payment_transactions`
  ADD PRIMARY KEY (`payment_id`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`room_id`);

--
-- Indexes for table `sys_meta`
--
ALTER TABLE `sys_meta`
  ADD PRIMARY KEY (`meta_id`);

--
-- Indexes for table `tenants`
--
ALTER TABLE `tenants`
  ADD PRIMARY KEY (`tenant_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `estate`
--
ALTER TABLE `estate`
  MODIFY `estate_id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `payment_transactions`
--
ALTER TABLE `payment_transactions`
  MODIFY `payment_id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `room_id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `sys_meta`
--
ALTER TABLE `sys_meta`
  MODIFY `meta_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `tenants`
--
ALTER TABLE `tenants`
  MODIFY `tenant_id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
