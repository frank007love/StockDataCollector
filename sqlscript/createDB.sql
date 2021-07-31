-- phpMyAdmin SQL Dump
-- version 2.11.11.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 08, 2012 at 12:55 AM
-- Server version: 5.0.77
-- PHP Version: 5.1.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `stock_summary`
--
CREATE DATABASE `stock_summary` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `stock_summary`;

-- --------------------------------------------------------

--
-- Table structure for table `mops_invoice_incoming`
--

CREATE TABLE IF NOT EXISTS `mops_invoice_incoming` (
  `stock` varchar(6) collate utf8_unicode_ci NOT NULL,
  `year` varchar(4) collate utf8_unicode_ci NOT NULL,
  `month` varchar(2) collate utf8_unicode_ci NOT NULL,
  `invoice` varchar(128) collate utf8_unicode_ci default NULL,
  `bi` varchar(128) collate utf8_unicode_ci default NULL,
  `cbi` varchar(128) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`stock`,`year`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `mops_invoice_incoming`
--

