use Patient_Appointment;
create table Patient (
    PatientId INT PRIMARY KEY,
    Name varchar(255),
    Gender ENUM('Male', 'Female', 'Other') NOT NULL,
    Age INT NOT NULL,
    Address VARCHAR(255),
    MedicalHistory TEXT,
    FOREIGN KEY (PatientId) REFERENCES User(UserID)
);
select * from Patient;
drop table Patient;