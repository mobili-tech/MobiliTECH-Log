package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String jdbcUrl = System.getenv("DB_HOST");
    private static final String usuario = System.getenv("DB_USER");
    private static final String senha = System.getenv("DB_PSWD");

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(jdbcUrl, usuario, senha);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado: " + e.getMessage());
        }
    }
}

