package com.newlocal.web.rest;

import com.newlocal.security.SecurityUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long getPersonIdByCurrentLogin() {

        if(SecurityUtils.getCurrentUserLogin().isPresent()) {

            String query = "select person.id from person join jhi_user on (person.user_id = jhi_user.id) " +
                "where login = '" + SecurityUtils.getCurrentUserLogin().get() + "'";

            return this.jdbcTemplate.queryForObject(query, long.class);
        }else{
            return 0;
        }
    }
}
