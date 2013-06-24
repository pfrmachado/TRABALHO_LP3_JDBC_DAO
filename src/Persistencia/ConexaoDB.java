/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author curso
 */
public class ConexaoDB {

    static Connection conexao = null;

    public static Connection getConexao() throws ClassNotFoundException, SQLException {
        if (conexao == null) {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conexao = DriverManager.getConnection("jdbc:derby://localhost:1527/ESTOQUE", "usuario", "senha");
        }

        return conexao;
    }

    public static void fechaConexao() throws SQLException {
        if (conexao != null) {
            conexao.close();
        }
    }
}
