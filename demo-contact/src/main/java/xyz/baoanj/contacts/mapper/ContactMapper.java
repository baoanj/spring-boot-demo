package xyz.baoanj.contacts.mapper;

import org.apache.ibatis.annotations.*;
import xyz.baoanj.contacts.entity.Address;
import xyz.baoanj.contacts.entity.Contact;

import java.util.List;

@Mapper
public interface ContactMapper {

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * XML
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    List<Contact> findAllContactsXML();

    int addContactXML(Contact contact);

    int addAddressXML(Address address);


    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * annotation
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    @Select("select contact_id, full_name, phone_number from contacts order by contact_id")
    @Results({
            @Result(column = "contact_id", property = "id"),
            @Result(column = "full_name", property = "fullName"),
            @Result(column = "phone_number", property = "phoneNumber"),
            @Result(column = "contact_id", property = "address",
                    one = @One(select = "xyz.baoanj.contacts.mapping.ContactMapper.getAddress")
            )
    })
    List<Contact> findAllContacts();

    @Insert("insert into contacts (full_name, phone_number) " +
            "values (#{fullName}, #{phoneNumber})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "contact_id")
    int addContact(Contact contact);

    @Insert("insert into address (contact_id, province, city, detail) " +
            "values (#{contactId}, #{province}, #{city}, #{detail})")
    int addAddress(Address address);

    @Select("select contact_id, province, city, detail from address where contact_id=#{contactId}")
    @Results({
            @Result(column = "contact_id", property = "contactId"),
            @Result(column = "province", property = "province"),
            @Result(column = "city", property = "city"),
            @Result(column = "detail", property = "detail")
    })
    Address getAddress(int contactId);
}