use Patient_Appointment;
CREATE TABLE Appointment (
    AppointmentId INT PRIMARY KEY AUTO_INCREMENT,
    DoctorId INT,
    PatientId int,
    TimeSlot DATETIME NOT NULL,
    FOREIGN KEY (DoctorId) REFERENCES Doctor(DoctorId),
    FOREIGN KEY (PatientId) REFERENCES Patient(PatientId)
);
select * from Appointment;
