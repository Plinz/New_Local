package com.newlocal.web.rest;

import com.newlocal.security.SecurityUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long getUserIdByCurrentLogin() {

        if(SecurityUtils.getCurrentUserLogin().isPresent()) {
            String query = "select id from jhi_user where login = '" + SecurityUtils.getCurrentUserLogin().get() + "'";
            return this.jdbcTemplate.queryForObject(query, long.class);
        }else{
            return 0;
        }
    }
}
