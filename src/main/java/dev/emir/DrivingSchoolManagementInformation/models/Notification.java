package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 1000)
    private String content;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime sentAt;

    private boolean isRead = false;

    private boolean toAll;

    @ManyToOne
    private Student student;

    public Notification(){}

    public Notification(Long id, String title, String content, LocalDateTime sentAt, boolean toAll, Student student) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.sentAt = sentAt;
        this.toAll = toAll;
        this.student = student;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isToAll() {
        return toAll;
    }

    public void setToAll(boolean toAll) {
        this.toAll = toAll;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
