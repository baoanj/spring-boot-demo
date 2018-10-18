package xyz.baoanj.contacts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.baoanj.contacts.dao.ContactRepository;
import xyz.baoanj.contacts.entity.Address;
import xyz.baoanj.contacts.entity.Contact;
import xyz.baoanj.contacts.mapper.ContactMapper;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepo;
    @Autowired
    private ContactMapper contactMapper;

    public List<Contact> getAllContacts() {
        // Spring JDBC
//        return contactRepo.findAllContacts();

        // MyBatis annotation
//        return contactMapper.findAllContacts();

        // MyBatis XML
        return contactMapper.findAllContactsXML();
    }

    public Contact addContact(String fullName, String phoneNumber, String province,
                              String city, String detail) {
        // Spring JDBC
//        Contact contact = new Contact(fullName, phoneNumber);
//        int id = contactRepo.addContact(contact);
//
//        Address address = new Address(id, province, city, detail);
//        contactRepo.addAddress(address);

        // MyBatis annotation
//        Contact contact = new Contact(fullName, phoneNumber);
//        contactMapper.addContact(contact);
//
//        Address address = new Address(contact.getId(), province, city, detail);
//        contactMapper.addAddress(address);

        // MyBatis XML
        Contact contact = new Contact(fullName, phoneNumber);
        contactMapper.addContactXML(contact);

        Address address = new Address(contact.getId(), province, city, detail);
        contactMapper.addAddressXML(address);

        contact.setAddress(address);

        return contact;
    }
}
