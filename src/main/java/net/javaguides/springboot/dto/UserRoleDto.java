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
        private boolean USER;
        private boolean SUPER_ADMIN;
        private boolean STAFF_ADMIN;
        private boolean CONTROL_ADMIN;

        public boolean isUser() {
            return USER;
        }

        public void setUser(boolean user) {
            this.USER = user;
        }

        public boolean isSuperAdmin() {
            return SUPER_ADMIN;
        }

        public void setSuperAdmin(boolean superAdmin) {
            this.SUPER_ADMIN = superAdmin;
        }

        public boolean isStaffAdmin() {
            return STAFF_ADMIN;
        }

        public void setStaffAdmin(boolean staffAdmin) {
            this.STAFF_ADMIN = staffAdmin;
        }

        public boolean isControlAdmin() {
            return CONTROL_ADMIN;
        }

        public void setControlAdmin(boolean controlAdmin) {
            this.CONTROL_ADMIN = controlAdmin;
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
