
/* personinfo*/
create table personinfo
(
    id int primary key auto_increment not null,
    name varchar(25) ,
    birthday datetime not null,
    bloodTypeId int not null,
    height float not null,
    weight float not null,
    bmi float not null,
    CONSTRAINT fk_bloodType FOREIGN KEY(bloodTypeId) REFERENCES bloodtype(id)
)
/* bloodtype表*/
create table bloodtype
(
    id int primary key auto_increment,
    type varchar(10)
)
/* stepinfo 表*/
create table stepinfo
(
    id int primary key auto_increment not null,
    s_count int not null,
    distance float not null,
    calories float not null,
    personId int not null,
    CONSTRAINT fk_personId FOREIGN KEY(personId) REFERENCES personinfo(id)
)