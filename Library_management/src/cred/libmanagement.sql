create database Librarymanagement;
use Librarymanagement;
create table employee(
id int auto_increment primary key,
name varchar(20) not null,
phoneno varchar(10) unique,
email varchar(20),
user_id varchar(20),
password varchar(20)
);
insert into employee values(1,"arun",9985486542,"arun@gmail.com","ak_32","arun12");
insert into employee values(2,"krish",8142688865,"krish6@gmail.com","kris_6","krish12");
insert into employee values(3,"satya",8142698481,"satya42@gmail.com","sat@2","satya123");
insert into employee values(4,"venkat",7989557890,"saivenkat@gmail.com","ven@3","venkat12");
select * from employee;


create table books(
book_id int unique,
name varchar(20),
author varchar(15),
Genre varchar(15),
available int,
borrowed int
);
insert into books values(12,"Hound & Baskerville","arthur canon","Crime Detective",4,2);
insert into books values(13,"Sherlock Homes","canon doyle","Detective",12,8);
insert into books values(14,"Mahabaratam","ganesh","Mythology",15,5);
insert into books values(15,"Ramayanam","valmiki","Mythology",16,4);
select * from books;
