package xyz.baoanj.contacts.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import xyz.baoanj.contacts.entity.Address;
import xyz.baoanj.contacts.entity.Contact;

@Repository
public class ContactRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public ContactRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Contact> findAllContacts() {
        return jdbc.query(
                "select contact_id, full_name, phone_number " +
                        "from contacts order by contact_id",
                (ResultSet rs, int rowNum) -> {
                    Contact contact = new Contact();
                    contact.setId(rs.getInt(1));
                    contact.setFullName(rs.getString(2));
                    contact.setPhoneNumber(rs.getString(3));
                    contact.setAddress(this.findAddress(rs.getInt(1)));
                    return contact;
                }
        );
    }

    // return AUTO_INCREMENT id
    public int addContact(Contact contact) {
        final String sql = "insert into contacts (full_name, phone_number) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update((Connection con) -> {
            PreparedStatement ps = jdbc.getDataSource().getConnection()
                    .prepareStatement(sql, new String[]{"full_name", "phone_number"});
            ps.setString(1, contact.getFullName());
            ps.setString(2, contact.getPhoneNumber());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public Address findAddress(int contactId) {
        return jdbc.queryForObject(
                "select province, city, detail from address " +
                        "where contact_id = " + contactId,
                (ResultSet rs, int rowNum) -> {
                    Address addr = new Address();
                    addr.setProvince(rs.getString(1));
                    addr.setCity(rs.getString(2));
                    addr.setDetail(rs.getString(3));
                    return addr;
                }
        );
    }

    public void addAddress(Address address) {
        jdbc.update(
                "insert into address " +
                        "(contact_id, province, city, detail) " +
                        "values (?, ?, ?, ?)",
                address.getContactId(),
                address.getProvince(),
                address.getCity(),
                address.getDetail()
        );
    }

}