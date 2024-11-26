package org.nov2024.Models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;

	@Column(nullable = false, unique = true)
    private String username;
    
    private String email;

    private String password;
    
    private Date createdt;
    
    private String createby;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "manage_user_roles",
			joinColumns = @JoinColumn(name = "userid"),
			inverseJoinColumns = @JoinColumn(name = "roleid")
	)
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "producer")
	private Set<ManageSubscription> producers = new HashSet<>();

	@OneToMany(mappedBy = "subscriber")
	private Set<ManageSubscription> subscribers = new HashSet<>();

    // Constructors, getters, and setters

    // Constructors
    public User() {}

    public User(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedt() {
		return createdt;
	}

	public void setCreatedt(Date createdt) {
		this.createdt = createdt;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<ManageSubscription> getProducers() {
        return producers;
    }

    public void setProducers(Set<ManageSubscription> producers) {
        this.producers = producers;
    }

    public Set<ManageSubscription> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<ManageSubscription> subscribers) {
        this.subscribers = subscribers;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}