use Patient_Appointment;
CREATE TABLE MedicalHistory (
    PatientId INT,
    Disease VARCHAR(255),
    Previous_Medication text,
    PRIMARY KEY (PatientId, Disease),
    FOREIGN KEY (PatientId) REFERENCES Patient(PatientId)
);