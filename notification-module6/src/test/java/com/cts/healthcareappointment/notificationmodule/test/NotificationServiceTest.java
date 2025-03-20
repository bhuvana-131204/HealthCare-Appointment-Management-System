package com.cts.healthcareappointment.notificationmodule.test;

import com.cts.healthcareappointment.notificationmodule.Dao.NotificationDao;
import com.cts.healthcareappointment.notificationmodule.Entity.Notification;
import com.cts.healthcareappointment.notificationmodule.Entity.NotificationStatus;
import com.cts.healthcareappointment.notificationmodule.Entity.NotificationType;
import com.cts.healthcareappointment.notificationmodule.service.NotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationDao notificationDao;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNotificationsByUserId() {
        String userId = "U001";

        Notification notification = new Notification(
                UUID.randomUUID().toString(),
                NotificationType.APPOINTMENT,
                "Your appointment is confirmed.",
                false,
                NotificationStatus.Confirmed,
                101,
                userId,
                null
        );

        List<Notification> notifications = Arrays.asList(notification);

        when(notificationDao.findByUserId(userId)).thenReturn(notifications);

        List<Notification> result = notificationService.getNotificationsByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(notification, result.get(0));
        verify(notificationDao, times(1)).findByUserId(userId);
    }

    @Test
    void testNotifyAfterBooking() {
        Notification bookingNotification = new Notification(
                UUID.randomUUID().toString(),
                null,
                "Your appointment is confirmed.",
                false,
                null,
                101,
                "U001",
                null
        );

        notificationService.notifyAfterBooking(bookingNotification);

        verify(notificationDao, times(1)).save(bookingNotification);
        assertEquals(NotificationType.APPOINTMENT, bookingNotification.getType());
        assertEquals(NotificationStatus.Confirmed, bookingNotification.getStatus());
        assertEquals(false, bookingNotification.isSeenStatus());
    }

    @Test
    void testNotifyAfterCancellation() {
        Notification cancellationNotification = new Notification(
                UUID.randomUUID().toString(),
                null,
                "Your appointment has been canceled.",
                false,
                null,
                101,
                "U001",
                null
        );

        notificationService.notifyAfterCancellation(cancellationNotification);

        verify(notificationDao, times(1)).save(cancellationNotification);
        assertEquals(NotificationType.APPOINTMENT, cancellationNotification.getType());
        assertEquals(NotificationStatus.Cancelled, cancellationNotification.getStatus());
    }

    @Test
    void testNotifyAppointmentCompletion() {
        Notification notification = new Notification(
                UUID.randomUUID().toString(),
                NotificationType.APPOINTMENT,
                "Your appointment is confirmed.",
                false,
                NotificationStatus.Confirmed,
                101,
                "U001",
                null
        );

        List<Notification> notifications = Arrays.asList(notification);

        when(notificationDao.findAll()).thenReturn(notifications);

        notificationService.notifyAppointmentCompletion();

        verify(restTemplate, times(1)).postForObject(anyString(), eq(notification), eq(Void.class));
        verify(notificationDao, times(1)).save(notification);

        assertEquals(NotificationStatus.Completed, notification.getStatus());
        assertEquals(true, notification.isSeenStatus());
    }

    @Test
    void testCreateNotification() {
        Notification newNotification = new Notification(
                UUID.randomUUID().toString(),
                NotificationType.EMAIL,
                "Your appointment is booked.",
                false,
                NotificationStatus.Booked,
                101,
                "U002",
                null
        );

        when(notificationDao.save(newNotification)).thenReturn(newNotification);

        Notification createdNotification = notificationService.createNotification(newNotification);

        assertEquals(newNotification, createdNotification);
        verify(notificationDao, times(1)).save(newNotification);
    }

    @Test
    void testGetAllNotifications() {
        Notification notification1 = new Notification(
                UUID.randomUUID().toString(),
                NotificationType.APPOINTMENT,
                "Your appointment is confirmed.",
                false,
                NotificationStatus.Confirmed,
                101,
                "U001",
                null
        );

        Notification notification2 = new Notification(
                UUID.randomUUID().toString(),
                NotificationType.EMAIL,
                "Your appointment is booked.",
                true,
                NotificationStatus.Booked,
                102,
                "U002",
                null
        );

        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationDao.findAll()).thenReturn(notifications);

        List<Notification> result = notificationService.getAllNotifications();

        assertEquals(2, result.size());
        assertEquals(notification1, result.get(0));
        assertEquals(notification2, result.get(1));
        verify(notificationDao, times(1)).findAll();
    }
}
