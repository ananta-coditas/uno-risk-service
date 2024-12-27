package com.genomebrain.backend.service.dto;

public class Demographics {

    private String name;

    private String phone;

    private String otherPhone;

    private String mrn;

    private String email;

    private String dateOfBirth;

    private String sex;

    private String chatTranscriptDate;

    private Integer age;

    private String contactShippingNo;

    private String firstName;

    private String lastName;

    private String chosenName;

    private String currentGender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
	}

	public String getChatTranscriptDate() {
		return chatTranscriptDate;
	}

	public void setChatTranscriptDate(String chatTranscriptDate) {
		this.chatTranscriptDate = chatTranscriptDate;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getContactShippingNo() {
		return contactShippingNo;
	}

	public void setContactShippingNo(String contactShippingNo) {
		this.contactShippingNo = contactShippingNo;
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

    public String getChosenName() {
        return chosenName;
    }

    public void setChosenName(String chosenName) {
        this.chosenName = chosenName;
    }

    public String getCurrentGender() {
        return currentGender;
    }

    public void setCurrentGender(String currentGender) {
        this.currentGender = currentGender;
    }

    @Override
	public String toString() {
		return "Demographics [name=" + name + ", phone=" + phone + ", otherPhone=" + otherPhone + ", mrn=" + mrn
				+ ", email=" + email + ", dateOfBirth=" + dateOfBirth + ", sex=" + sex + ", chatTranscriptDate="
				+ chatTranscriptDate + ", age=" + age + ", contactShippingNo=" + contactShippingNo + "]";
	}

}
