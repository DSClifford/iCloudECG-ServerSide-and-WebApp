package com.utkbiodynamics.dashboard.data;

import java.sql.Date;

public final class User {
    private String role;
    private String firstName;
    private String lastName;
    private String title;
    private boolean male;
    private String email;
    private String location;
    private String phone;
    private Integer newsletterSubscription;
    private String institution;
    private String bio;
    private Date birthDate;
    private String gender;
	private int firstrun;
	private long age;
	private int intage;
	private String password;
	private String uniqueID;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public Integer getNewsletterSubscription() {
        return newsletterSubscription;
    }

    public void setNewsletterSubscription(final Integer newsletterSubscription) {
        this.newsletterSubscription = newsletterSubscription;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(final String website) {
        this.institution = website;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(final String bio) {
        this.bio = bio;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(final boolean male) {
        this.male = male;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public Date getbirthDate() {
		// TODO Auto-generated method stub
		return birthDate;
	}
	public void setbirthDate(Date birthDate) {
		// TODO Auto-generated method stub
		this.birthDate = birthDate;
	}
	public String getGender() {
		// TODO Auto-generated method stub
		return gender;
	}
	public void setGender(String gender) {
		// TODO Auto-generated method stub
		this.gender = gender;
	}
	public void setFirstrun(int i) {
		// TODO Auto-generated method stub
		this.firstrun = i;
	}
	public int getFirstrun() {
		// TODO Auto-generated method stub
		return firstrun;
	}
	public long getPatAge() {
		// TODO Auto-generated method stub
		return age;
	}
	public void setPatAge(Long age) {
		// TODO Auto-generated method stub
		this.age = age;
	}
	public void setIntPatAge(int intage) {
		// TODO Auto-generated method stub
		this.intage=intage;
	}

	public int getIntPatAge() {
		// TODO Auto-generated method stub
		return intage;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
		// TODO Auto-generated method stub
	}
	public String getUniqueID(){
		return uniqueID;
	}

}
