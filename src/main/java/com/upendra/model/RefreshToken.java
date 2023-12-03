package com.upendra.model;

import jakarta.persistence.*;
import java.time.Instant;

import com.upendra.model.User;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

	@Column(nullable = false, updatable = false)
	private Instant expiryTime;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    public RefreshToken() {}

	public RefreshToken(String token, Instant expiryDate, User user) {
		super();
		this.token = token;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Instant getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Instant expiryTime) {
		this.expiryTime = expiryTime;
	}

	@Override
	public String toString() {
		return "RefreshToken [id=" + id + ", token=" + token + ", expiryTime=" + expiryTime + ", user=" + user + "]";
	}
}