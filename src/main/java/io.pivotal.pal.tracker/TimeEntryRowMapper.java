package io.pivotal.pal.tracker;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeEntryRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        TimeEntry mapObject = new TimeEntry();
        mapObject.setId(rs.getLong("id"));
        mapObject.setProjectId(rs.getLong("projectId"));
        mapObject.setUserId(rs.getLong("userId"));
        mapObject.setDate(LocalDate.parse(rs.getDate("date").toString(),DateTimeFormatter.BASIC_ISO_DATE));
        mapObject.setHours(rs.getInt("hours"));
        return mapObject;
    }
}
