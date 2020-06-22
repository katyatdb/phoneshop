package com.es.core.dao.color;

import com.es.core.model.color.Color;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class JdbcColorDao implements ColorDao {
    private static final String FIND_ALL_COLOR_IDS = "select id from colors";
    private static final String INSERT_COLOR = "insert into colors (id, code) values (?, ?)";

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insertColors(Set<Color> colors) {
        List<Long> colorIds = jdbcTemplate.queryForList(FIND_ALL_COLOR_IDS, Long.class);

        List<Object[]> batchColors = colors.stream()
                .filter(color -> !colorIds.contains(color.getId()))
                .map(color -> new Object[]{color.getId(), color.getCode()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(INSERT_COLOR, batchColors);
    }
}
