package com.example.vulnapp.repo;

import com.example.vulnapp.model.Account;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepository {

    private final DataSource dataSource;

    public AccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * VULNERABLE: SQL Injection (CWE-89)
     * - Using Statement
     * - Concatenating user input directly into SQL
     */
    public List<Account> findByOwnerUnsafe(String owner) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);

        // 🔥 VULNERABLE QUERY (do not do this in real apps)
        String sql = "SELECT id, owner_name, balance FROM accounts WHERE owner_name = '" + owner + "'";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Account> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new Account(
                        rs.getLong("id"),
                        rs.getString("owner_name"),
                        rs.getDouble("balance")
                ));
            }
            return results;
        }
    }
}
