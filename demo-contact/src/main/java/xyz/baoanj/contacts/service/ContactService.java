package xyz.baoanj.contacts.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import xyz.baoanj.contacts.entity.Contact;

import java.util.List;

public interface ContactService {

    @Cacheable(value = "contactListCache", key = "'all'", cacheManager = "cacheManagerContact")
    public List<Contact> getAllContacts();

    @CacheEvict(value = "contactListCache", key = "'all'", cacheManager = "cacheManagerContact")
    @CachePut(value = "contactCache", key = "#result.id", cacheManager = "cacheManagerContact")
    public Contact addContact(String fullName, String phoneNumber, String province,
                              String city, String detail);
}
