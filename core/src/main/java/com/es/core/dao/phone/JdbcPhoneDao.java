package com.es.core.dao.phone;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class JdbcPhoneDao implements PhoneDao {
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
    private static final String FIND_ALL_PHONES = "select * from phones offset ? limit ?";
    private static final String INSERT_PHONE_COLORS = "insert into phone2color (phoneId, colorId) values (?, ?)";
    private static final String DELETE_PHONE_COLORS = "delete from phone2color where phoneId = ?";
    private static final String FIND_ALL_COLOR_IDS = "select id from colors";
    private static final String FIND_COLOR_BY_ID = "select * from colors where id = ?";
    private static final String INSERT_COLOR = "insert into colors (id, code) values (?, ?)";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Phone> get(final Long key) {
        try {
            Phone phone = jdbcTemplate.queryForObject(FIND_PHONE_BY_ID, new PhoneRowMapper(), key);
            return Optional.of(phone);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(final Phone phone) {
        if (phone.getId() == null) {
            insert(phone);
        } else {
            try {
                jdbcTemplate.queryForObject(FIND_PHONE_BY_ID, new PhoneRowMapper(), phone.getId());
                update(phone);
            } catch (EmptyResultDataAccessException e) {
                insert(phone);
            }
        }
    }

    private void insert(Phone phone) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(phone);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_PHONE, namedParameters, keyHolder);

        if (phone.getId() == null) {
            phone.setId((Long) keyHolder.getKey());
        }

        insertPhoneColors(phone.getId(), phone.getColors());
    }

    private void update(Phone phone) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(phone);
        namedParameterJdbcTemplate.update(UPDATE_PHONE, namedParameters);
        deleteColors(phone.getId());
        insertPhoneColors(phone.getId(), phone.getColors());
    }

    private void insertPhoneColors(Long phoneId, Set<Color> colors) {
        List<Object[]> batchPhoneColors = new ArrayList<>();

        for (Color color : colors) {
            Object[] phoneColorValues = new Object[]{phoneId, color.getId()};
            batchPhoneColors.add(phoneColorValues);
        }

        insertColors(colors);
        jdbcTemplate.batchUpdate(INSERT_PHONE_COLORS, batchPhoneColors);
    }

    private void insertColors(Set<Color> colors) {
        List<Object[]> batchColors = new ArrayList<>();
        List<Long> colorIds = jdbcTemplate.queryForList(FIND_ALL_COLOR_IDS, Long.class);

        for (Color color : colors) {
            if (!colorIds.contains(color.getId())) {
                Object[] colorValues = new Object[]{color.getId(), color.getCode()};
                batchColors.add(colorValues);
            }
        }

        jdbcTemplate.batchUpdate(INSERT_COLOR, batchColors);
    }

    private void deleteColors(Long phoneId) {
        jdbcTemplate.update(DELETE_PHONE_COLORS, phoneId);
    }

    public List<Phone> findAll(int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException();
        }

        return jdbcTemplate.query(FIND_ALL_PHONES, new PhoneRowMapper(), offset, limit);
    }

    private final class PhoneRowMapper extends BeanPropertyRowMapper<Phone> {

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
