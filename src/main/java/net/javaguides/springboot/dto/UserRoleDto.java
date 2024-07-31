package net.javaguides.springboot.dto;

public class UserRoleDto {
    private Long id;
    private String username;
    private Roles roles;

    public UserRoleDto(Long id, String username, Roles roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public static class Roles {
        private boolean user;
        private boolean superAdmin;
        private boolean staffAdmin;
        private boolean controlAdmin;

        public boolean isUser() {
            return user;
        }

        public void setUser(boolean user) {
            this.user = user;
        }

        public boolean isSuperAdmin() {
            return superAdmin;
        }

        public void setSuperAdmin(boolean superAdmin) {
            this.superAdmin = superAdmin;
        }

        public boolean isStaffAdmin() {
            return staffAdmin;
        }

        public void setStaffAdmin(boolean staffAdmin) {
            this.staffAdmin = staffAdmin;
        }

        public boolean isControlAdmin() {
            return controlAdmin;
        }

        public void setControlAdmin(boolean controlAdmin) {
            this.controlAdmin = controlAdmin;
        }
    }

    // Getters and setters for UserRoleDto
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }
}
