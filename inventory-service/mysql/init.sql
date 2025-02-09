-- Creates the Database if not exists
CREATE DATABASE IF NOT EXISTS inventory_service;
-- Create the user if it does not exist
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'mysql';

-- Grant all Privileges to work on the inventory_service db
GRANT ALL PRIVILEGES  ON inventory_service.* TO 'admin'@'%';

-- Flush Privileges to apply the changes
FLUSH PRIVILEGES;