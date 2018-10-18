package xyz.baoanj.contacts.entity;

public class Address {
    private int contactId;
    private String province;
    private String city;
    private String detail;

    public Address() {}

    public Address(int contactId, String province, String city, String detail) {
        this.contactId = contactId;
        this.province = province;
        this.city = city;
        this.detail = detail;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
