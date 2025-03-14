package com.cts.healthcareappointment.notificationmodule.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.healthcareappointment.notificationmodule.Entity.Notification;
import com.cts.healthcareappointment.notificationmodule.Entity.NotificationStatus;
import com.cts.healthcareappointment.notificationmodule.Entity.NotificationType;

import java.util.List;

public interface NotificationDao extends JpaRepository<Notification, String> {

	@Query("SELECT n FROM Notification n WHERE n.type = :type AND n.status = :status")
	List<Notification> findPendingAppointmentsForDoctor(@Param("type") NotificationType type, @Param("status") NotificationStatus status);
	
    @Query("SELECT n FROM Notification n WHERE n.type = :type AND n.notify.id = :userId AND n.status = :status")
    List<Notification> findConfirmedAppointmentsForDoctor(@Param("type") NotificationType type,@Param("userId") String userId, @Param("status") NotificationStatus status);

    @Query("SELECT n FROM Notification n WHERE n.type = :type AND n.status IN :statuses")
    List<Notification> findAppointmentStatusForPatient(@Param("type") NotificationType type, @Param("statuses") List<NotificationStatus> statuses);
}