use Patient_Appointment;
CREATE TABLE Availability (
    DoctorId INT,
    Date DATE NOT NULL,
   
    TimeSlot VARCHAR(255) NOT NULL,
    PRIMARY KEY (DoctorId, Date, TimeSlot),
    FOREIGN KEY (DoctorId) REFERENCES Doctor(DoctorId)
);
select * from Availability;
