use Patient_Appointment;
create table Doctor(DoctorId int primary key, Name varchar(100) not null, 
   Specialization varchar(100),Experience int,FOREIGN KEY (DoctorID) REFERENCES User(UserID));
   
select * from Doctor;
