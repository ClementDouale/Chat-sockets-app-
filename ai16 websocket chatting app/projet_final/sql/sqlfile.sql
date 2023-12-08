/*Create the database*/
DROP DATABASE IF EXISTS ai16-db;
CREATE DATABASE ai16-db;
USE ai16-db;

/*Drop tables to recreate them*/
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS channels;
DROP TABLE IF EXISTS connectedUsers;

/*Table to manage users*/
CREATE TABLE users(
id int unsigned not null auto_increment,
last_name varchar(50),
first_name varchar(50),
mail varchar(50),
hash_pwd varchar(255),
registration_date datetime not null,
admin boolean,
PRIMARY KEY (id));

/*Table to manage channels */
CREATE TABLE channels(
id int unsigned not null auto_increment,
channel_name varchar(50),
max_users int(5),
PRIMARY KEY (id));

/*Table to manage the connection of users to channels */
CREATE TABLE connected_users(
user int unsigned not null,
channel int unsigned not null,
PRIMARY KEY (user, channel),
FOREIGN KEY (user) REFERENCES users(id),
FOREIGN KEY (channel) REFERENCES channels(id));
