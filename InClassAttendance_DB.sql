
-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 12, 2016 at 06:27 PM
-- Server version: 10.0.20-MariaDB
-- PHP Version: 5.2.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `u125413163_appdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE IF NOT EXISTS `attendance` (
  `classid` bigint(20) NOT NULL,
  `studentid` bigint(20) NOT NULL,
  `attendeddate` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `deviceid` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`classid`,`studentid`,`attendeddate`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`classid`, `studentid`, `attendeddate`, `deviceid`) VALUES
(501, 700648212, '2016-04-26', 'ceacf123990aeb26'),
(501, 700648212, '2016-04-29', 'ceacf123990aeb26'),
(501, 700648999, '2016-04-29', 'ceacf123990aeb26'),
(501, 700648888, '2016-04-29', 'ceacf123990aeb27'),
(501, 700648777, '2016-04-29', 'ceacf123990aeb27'),
(501, 700648779, '2016-04-29', 'ceacf123990aeb31'),
(501, 700648212, '2016-05-06', 'ceacf123990aeb26'),
(777, 700648212, '2016-05-12', 'ceacf123990aeb26');

-- --------------------------------------------------------

--
-- Table structure for table `class_db`
--

CREATE TABLE IF NOT EXISTS `class_db` (
  `classid` bigint(50) NOT NULL,
  `userid` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `semester` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `year` int(4) NOT NULL,
  PRIMARY KEY (`classid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `class_db`
--

INSERT INTO `class_db` (`classid`, `userid`, `semester`, `year`) VALUES
(999, '700648100', 'Spring', 2016),
(501, '700648100', 'Spring', 2016),
(502, '700648100', 'Summer', 2016),
(888, '700648100', 'Spring', 2016),
(666, '700648100', 'Spring', 2016),
(777, '700648100', 'Spring', 2016),
(334, '700648100', 'Spring', 2016);

-- --------------------------------------------------------

--
-- Table structure for table `user_registration`
--

CREATE TABLE IF NOT EXISTS `user_registration` (
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `studentid` bigint(11) NOT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `mobile` bigint(11) NOT NULL,
  `department` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `usertype` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'student',
  `deviceid` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `approval_status` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`studentid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `user_registration`
--

INSERT INTO `user_registration` (`name`, `studentid`, `email`, `mobile`, `department`, `password`, `usertype`, `deviceid`, `approval_status`) VALUES
('nishanth', 700648212, 'nxp82120@ucmo.edu', 9132079602, 'cis', 'nishanth', 'student', 'ceacf123990aeb26', ''),
('nickey', 700648100, 'nishanthpavurapu@yahoo.com', 9703421620, 'cis', 'nishanth', 'professor', 'ceacf123990aeb26', ''),
('7', 0, 'ck', 0, '6', 'v', 'professor', 'bcd36ae75ad11c74', ''),
('t', 7, 'm', 0, 'cu', '7', 'professor', 'bcd36ae75ad11c74', '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
