package org.nov2024.Models;

import java.sql.Date;

import jakarta.persistence.*;


@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageid;

    private String content;
    
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "producerid")
    private User producerId;
    
    private Date createdt;

    // Constructors, getters, and setters

    // Constructors
    public Message() {}

    public Message(String content, User producerId) {
        this.setContent(content);
        this.setProducerId(producerId);
    }

	public Date getCreatedt() {
		return createdt;
	}

	public void setCreatedt(Date createdt) {
		this.createdt = createdt;
	}

    public User getProducerId() {
        return producerId;
    }

    public void setProducerId(User producerId) {
        this.producerId = producerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMessageid() {
        return messageid;
    }

    public void setMessageid(Integer messageid) {
        this.messageid = messageid;
    }
}