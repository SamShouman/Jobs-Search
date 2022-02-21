package com.example.jobproject2.Models;

public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNb;
    private String role;
    private String description;
    private String salary;
    private String position;
    private String profilePicture;
    private String companyName;

    public User() {}

    public User(String userId, String firstName, String lastName, String email, String password, String mobileNb) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.mobileNb = mobileNb;
    }

    public User(String userId, String firstName, String lastName, String email, String password, String mobileNb, String dob, String role,
                String description, String salary, String position, String profilePicture) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.mobileNb = mobileNb;
        this.role = role;
        this.description = description;
        this.salary = salary;
        this.position = position;
        this.profilePicture = profilePicture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getMobileNb() {
        return mobileNb;
    }

    public void setMobileNb(String mobileNb) {
        this.mobileNb = mobileNb;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salaryId) {
        this.salary = salaryId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String positionId) {
        this.position = positionId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

