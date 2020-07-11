package com.es.core.dao.phone;

import com.es.core.dao.AbstractJdbcDao;
import com.es.core.model.color.Color;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class JdbcPhoneDao extends AbstractJdbcDao<Phone> implements PhoneDao {
    private static final String FIND_PHONE_BY_ID = "select * from phones where id = :id";
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
            "where stocks.stock > 0 and phones.price is not null offset :offset limit :limit";
    private static final String INSERT_PHONE_COLORS = "insert into phone2color (phoneId, colorId) " +
            "values (:phoneId, :colorId)";
    private static final String DELETE_PHONE_COLORS = "delete from phone2color where phoneId = :phoneId";

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Phone> get(Long id) {
        return super.get(FIND_PHONE_BY_ID, new MapSqlParameterSource("id", id),
                new PhoneRowMapper(namedParameterJdbcTemplate));
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
        super.delete(DELETE_PHONE_COLORS, new MapSqlParameterSource("phoneId", phoneId));

        SqlParameterSource[] sqlParameterSources = colors.stream()
                .map(color -> new MapSqlParameterSource()
                        .addValue("phoneId", phoneId)
                        .addValue("colorId", color.getId())).toArray(MapSqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(INSERT_PHONE_COLORS, sqlParameterSources);
    }

    @Override
    public List<Phone> findAll(int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException();
        }

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit);
        return namedParameterJdbcTemplate.query(FIND_ALL_PHONES, sqlParameterSource,
                new PhoneRowMapper(namedParameterJdbcTemplate));
    }

    @Override
    public List<Phone> findAll(String query, int offset, int limit, String sortBy, String orderBy) {
        if (offset < 0 || limit < 0 || sortBy == null || orderBy == null) {
            throw new IllegalArgumentException();
        }

        String sqlQuery = createSqlQueryFindPhones(query, offset, limit, sortBy, orderBy);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("query", "%" + query + "%");

        return namedParameterJdbcTemplate.query(sqlQuery, sqlParameterSource,
                new PhoneRowMapper(namedParameterJdbcTemplate));
    }

    @Override
    public int countPhones(String query) {
        String sqlQuery = createSqlQueryCountPhones(query);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("query", "%" + query + "%");

        return namedParameterJdbcTemplate.queryForObject(sqlQuery, sqlParameterSource, Integer.class);
    }

    private String createSqlQueryFindPhones(String query, int offset, int limit, String sortBy, String orderBy) {
        if (query != null && !query.isEmpty()) {
            return String.format("select * from phones as p join stocks as s on p.id = s.phoneId where s.stock > 0 " +
                    "and p.price is not null and (lower(p.brand) like :query or lower(p.model) like :query) " +
                    "order by lower(%s) %s offset %d limit %d", sortBy, orderBy, offset, limit);
        }

        return String.format("select * from phones as p join stocks as s on p.id = s.phoneId where s.stock > 0 " +
                "and p.price is not null order by lower(%s) %s offset %d limit %d", sortBy, orderBy, offset, limit);
    }

    private String createSqlQueryCountPhones(String query) {
        if (query != null && !query.isEmpty()) {
            return "select count(*) from phones as p join stocks as s on p.id = s.phoneId " +
                    "where s.stock > 0 and p.price is not null and (lower(p.brand) like :query " +
                    "or lower(p.model) like :query)";
        }

        return "select count(*) from phones as p join stocks as s on p.id = s.phoneId " +
                "where s.stock > 0 and p.price is not null";
    }
}
