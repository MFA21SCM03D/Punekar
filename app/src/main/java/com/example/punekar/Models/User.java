package com.example.punekar.Models;

public class User {
    String name;
    String email;
    String gender;
    String contact;
    String url;
    String userID;
    String skills;
    String institution;
    String institutionDegree;
    String institutionDuration;
    String certificationName;
    String certificationInstitution;
    String certificationDuration;
    String interests;
    String qualification;
    int views;
    int collab;
//    String status;


    public User() {
    }

    public User(String name, String email, String gender, String contact, String url, String userID,
                String skills, String institution, String institutionDegree,
                String institutionDuration, String certificationName, String certificationInstitution,
                String certificationDuration, String interests, String qualification, int views, int collab) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.contact = contact;
        this.url = url;
        this.userID = userID;
        this.skills = skills;
        this.institution = institution;
        this.institutionDegree = institutionDegree;
        this.institutionDuration = institutionDuration;
        this.certificationName = certificationName;
        this.certificationInstitution = certificationInstitution;
        this.certificationDuration = certificationDuration;
        this.interests = interests;
        this.qualification = qualification;
        this.views = views;
        this.collab = collab;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getContact() {
        return contact;
    }

    public String getUrl() {
        return url;
    }

    public String getUserID() {
        return userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getCollab() {
        return collab;
    }

    public void setCollab(int collab) {
        this.collab = collab;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getInstitution() {
        return institution;
    }

    public String getInstitutionDegree() {
        return institutionDegree;
    }

    public String getInstitutionDuration() {
        return institutionDuration;
    }

    public String getCertificationName() {
        return certificationName;
    }

    public String getCertificationInstitution() {
        return certificationInstitution;
    }

    public String getCertificationDuration() {
        return certificationDuration;
    }

    public String getInterests() {
        return interests;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public void setInstitutionDegree(String institutionDegree) {
        this.institutionDegree = institutionDegree;
    }

    public void setInstitutionDuration(String institutionDuration) {
        this.institutionDuration = institutionDuration;
    }

    public void setCertificationName(String certificationName) {
        this.certificationName = certificationName;
    }

    public void setCertificationInstitution(String certificationInstitution) {
        this.certificationInstitution = certificationInstitution;
    }

    public void setCertificationDuration(String certificationDuration) {
        this.certificationDuration = certificationDuration;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    //    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
}
