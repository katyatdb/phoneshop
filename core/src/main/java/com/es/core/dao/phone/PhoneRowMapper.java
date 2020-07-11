package com.es.core.dao.phone;

import com.es.core.model.color.Color;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PhoneRowMapper extends BeanPropertyRowMapper<Phone> {
    private static final String FIND_PHONE_COLORS = "select * from colors " +
            "join phone2color on colors.id = phone2color.colorId " +
            "where phone2color.phoneId = :phoneId";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PhoneRowMapper(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(Phone.class);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Phone mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Phone phone = super.mapRow(rs, rowNumber);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("phoneId", phone.getId());
        Set<Color> colors = new HashSet<>(namedParameterJdbcTemplate.query(FIND_PHONE_COLORS,
                sqlParameterSource, new BeanPropertyRowMapper<>(Color.class)));
        phone.setColors(colors);

        return phone;
    }
}
