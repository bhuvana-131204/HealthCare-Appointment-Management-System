use Patient_Appointment;
CREATE TABLE Notification (
    NotificationId INT PRIMARY KEY AUTO_INCREMENT,
    UserId INT,
    Type ENUM('Frontend') NOT NULL,
    Status BOOLEAN NOT NULL,
    FOREIGN KEY (UserId) REFERENCES User(UserId)
);
SELECT * FROM Notification;