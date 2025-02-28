use Patient_Appointment;
CREATE TABLE Consultation (
    ConsultationId INT PRIMARY KEY AUTO_INCREMENT,
    AppointmentId INT,
    Notes TEXT,
    Prescription TEXT,
    FOREIGN KEY (AppointmentId) REFERENCES Appointment(AppointmentId)
);
