package com.appviewx.repos;

import com.appviewx.model.primarydb.FileLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Repository
public class FileLineJDBCRepository implements JdbcRepository<FileLine> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int[] batchInsert(List<FileLine> itemsList) {

        return jdbcTemplate.batchUpdate("insert into filelines (column1, column2, column3) values(?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, itemsList.get(i).getColumn1());
                ps.setString(2, itemsList.get(i).getColumn2());
                ps.setString(3, itemsList.get(i).getColumn3());
            }

            @Override
            public int getBatchSize() {
                return itemsList.size();
            }
        });
    }

    @Override
    public int[][] batchInsert(List<FileLine> itemsList, int batchSize) {
        return jdbcTemplate.batchUpdate("insert into filelines (column1, column2, column3) values(?,?)", itemsList, batchSize, new ParameterizedPreparedStatementSetter<FileLine>() {
            @Override
            public void setValues(PreparedStatement ps, FileLine fileLine) throws SQLException {
                ps.setString(1, fileLine.getColumn1());
                ps.setString(2, fileLine.getColumn2());
                ps.setString(3, fileLine.getColumn3());
            }
        });
    }

    @Override
    public int[] batchUpdate(List<FileLine> itemsList) {
        return new int[0];
    }

    @Override
    public int[][] batchUpdate(List<FileLine> itemsList, int batchSize) {
        return new int[0][];
    }
}
