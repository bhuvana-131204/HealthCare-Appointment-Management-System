package com.healthcare.management.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
		@Id
		@Column(name="Notification_id",nullable=false,unique=true)
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int notification_id; 
		
		@ManyToOne
		@JoinColumn(name="user_id")
		private User notify;
		
		@Column(name="Type",nullable=false)
		private NotificationType type;
		
		@Column(name="Message",nullable=false)
		private String message;
		
		@Column(name="SeenStatus",nullable=false)
		private boolean seenStatus;
		
}
