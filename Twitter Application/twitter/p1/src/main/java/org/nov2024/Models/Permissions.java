package org.nov2024.Models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "permissions")
public class Permissions {

    @Id
    private Integer permissionid;

    private String permissionname;

    public String getPermissionname() {
        return permissionname;
    }

    public void setPermissionname(String permissionname) {
        this.permissionname = permissionname;
    }
}
