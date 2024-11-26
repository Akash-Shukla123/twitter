package org.nov2024.Models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    private Integer roleid;

    @Column(unique = true)
    private String rolename;
    
    private String roledesc;

/*    @ManyToMany(mappedBy = "roles")
    private Set<User> users;*/

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "manage_role_permission",
            joinColumns = @JoinColumn(name = "roleid"),
            inverseJoinColumns = @JoinColumn(name = "permissionid")
    )
    private Set<Permissions> permissions;

    // Constructors, getters, and setters

    // Constructors
    public Role() {}

    public Role(String rolename) {
        this.setRolename(rolename);
    }

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRoledesc() {
		return roledesc;
	}

	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

    public Set<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permissions> permissions) {
        this.permissions = permissions;
    }
}