package com.es.core.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

@Repository
public abstract class AbstractJdbcDao<T> {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public <E> Optional<T> get(String sqlQuery, BeanPropertyRowMapper<T> rowMapper, E id) {
        try {
            T item = jdbcTemplate.queryForObject(sqlQuery, rowMapper, id);
            return Optional.of(item);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(String namedParameterSqlQuery, SqlParameterSource namedParams) {
        namedParameterJdbcTemplate.update(namedParameterSqlQuery, namedParams);
    }

    public Number save(String namedParameterSqlQuery, SqlParameterSource namedParams, KeyHolder keyHolder) {
        namedParameterJdbcTemplate.update(namedParameterSqlQuery, namedParams, keyHolder);

        return keyHolder.getKey();
    }

    public <E> void delete(String sqlQuery, E id) {
        jdbcTemplate.update(sqlQuery, id);
    }
}
