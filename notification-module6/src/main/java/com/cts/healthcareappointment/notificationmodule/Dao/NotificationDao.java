package com.cts.healthcareappointment.notificationmodule.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.healthcareappointment.notificationmodule.Entity.Notification;

import java.util.List;

@Repository
public interface NotificationDao extends JpaRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
}
