package com.es.core.dao.phone;

import com.es.core.dao.AbstractJdbcDao;
import com.es.core.model.color.Color;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcPhoneDao extends AbstractJdbcDao<Phone> implements PhoneDao {
    private static final String FIND_PHONE_BY_ID = "select * from phones where id = ?";
    private static final String FIND_PHONE_COLORS = "select * from colors " +
            "join phone2color on colors.id = phone2color.colorId " +
            "where phone2color.phoneId = ?";
    private static final String INSERT_PHONE = "insert into phones (id, brand, model, price, displaySizeInches, " +
            "weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, " +
            "displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, " +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "values (:id, :brand, :model, :price, :displaySizeInches, :weightGr, :lengthMm, :widthMm, :heightMm, " +
            ":announced, :deviceType, :os, :displayResolution, :pixelDensity, :displayTechnology, " +
            ":backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, :batteryCapacityMah, " +
            ":talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";
    private static final String UPDATE_PHONE = "update phones set brand = :brand, model = :model, price = :price, " +
            "displaySizeInches = :displaySizeInches, weightGr = :weightGr, lengthMm = :lengthMm, widthMm = :widthMm, " +
            "heightMm = :heightMm, announced = :announced, deviceType = :deviceType, os = :os, " +
            "displayResolution = :displayResolution, pixelDensity = :pixelDensity, " +
            "displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, " +
            "frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, " +
            "batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, " +
            "standByTimeHours = :standByTimeHours, bluetooth = :bluetooth, positioning = :positioning, " +
            "imageUrl = :imageUrl, description = :description where id = :id";
    private static final String FIND_ALL_PHONES = "select * from phones " +
            "join stocks on phones.id = stocks.phoneId " +
            "where stocks.stock > 0 and phones.price is not null offset ? limit ?";
    private static final String INSERT_PHONE_COLORS = "insert into phone2color (phoneId, colorId) values (?, ?)";
    private static final String DELETE_PHONE_COLORS = "delete from phone2color where phoneId = ?";

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Phone> get(Long key) {
        return super.get(FIND_PHONE_BY_ID, new PhoneRowMapper(), key);
    }

    @Override
    public void save(final Phone phone) {
        Optional<Phone> phoneOptional = get(phone.getId());

        if (phoneOptional.isPresent()) {
            update(phone);
        } else {
            insert(phone);
        }
    }

    private void insert(Phone phone) {
        Long newId = (Long) super.save(INSERT_PHONE, new BeanPropertySqlParameterSource(phone),
                new GeneratedKeyHolder());

        if (phone.getId() == null) {
            phone.setId(newId);
        }
    }

    private void update(Phone phone) {
        super.save(UPDATE_PHONE, new BeanPropertySqlParameterSource(phone));
    }

    @Override
    public void updatePhoneColors(Long phoneId, Set<Color> colors) {
        super.delete(DELETE_PHONE_COLORS, phoneId);

        List<Object[]> batchPhoneColors = colors.stream()
                .map(color -> new Object[]{phoneId, color.getId()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(INSERT_PHONE_COLORS, batchPhoneColors);
    }

    @Override
    public List<Phone> findAll(int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException();
        }

        return jdbcTemplate.query(FIND_ALL_PHONES, new PhoneRowMapper(), offset, limit);
    }

    @Override
    public List<Phone> findAll(String query, int offset, int limit, String sortBy, String orderBy) {
        if (offset < 0 || limit < 0 || sortBy == null || orderBy == null) {
            throw new IllegalArgumentException();
        }

        String sqlQuery = createSqlQueryFindPhones(query, offset, limit, sortBy, orderBy);
        return jdbcTemplate.query(sqlQuery, new PhoneRowMapper());
    }

    @Override
    public int countPhones(String query) {
        String sqlQuery = createSqlQueryCountPhones(query);
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class);
    }

    private String createSqlQueryFindPhones(String query, int offset, int limit, String sortBy, String orderBy) {
        if (query != null && !query.isEmpty()) {
            return String.format("select * from phones as p join stocks as s on p.id = s.phoneId where s.stock > 0 " +
                    "and p.price is not null and (lower(p.brand) like '%%%1$s%%' or lower(p.model) like '%%%1$s%%') " +
                    "order by lower(%2$s) %3$s offset %4$d limit %5$d", query, sortBy, orderBy, offset, limit);
        }

        return String.format("select * from phones as p join stocks as s on p.id = s.phoneId where s.stock > 0 " +
                "and p.price is not null order by lower(%s) %s offset %d limit %d", sortBy, orderBy, offset, limit);
    }

    private String createSqlQueryCountPhones(String query) {
        if (query != null && !query.isEmpty()) {
            return String.format("select count(*) from phones as p join stocks as s on p.id = s.phoneId " +
                    "where s.stock > 0 and p.price is not null and (lower(p.brand) like '%%%1$s%%' " +
                    "or lower(p.model) like '%%%1$s%%')", query);
        }

        return "select count(*) from phones as p join stocks as s on p.id = s.phoneId " +
                        "where s.stock > 0 and p.price is not null";
    }

    private class PhoneRowMapper extends BeanPropertyRowMapper<Phone> {

        public PhoneRowMapper() {
            super(Phone.class);
        }

        @Override
        public Phone mapRow(ResultSet rs, int rowNumber) throws SQLException {
            Phone phone = super.mapRow(rs, rowNumber);
            Set<Color> colors = new HashSet<>(jdbcTemplate.query(FIND_PHONE_COLORS,
                    new BeanPropertyRowMapper<>(Color.class), phone.getId()));
            phone.setColors(colors);

            return phone;
        }
    }
}
