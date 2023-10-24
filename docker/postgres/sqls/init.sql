CREATE DATABASE dockerdb;

\connect dockerdb

CREATE SCHEMA walletservice;
CREATE SCHEMA liquibase;

DROP SCHEMA IF EXISTS public cascade;

