/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Classes.Estoque;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author curso
 */
public class EstoqueDAO {
    
    private Connection conexao = null;
    private final PreparedStatement sqlCriaEstoque;
    private final PreparedStatement sqlBuscaByFilial;
    private final PreparedStatement sqlBuscaByProduto;
    private final PreparedStatement sqlBuscaByProdutoByFilial;
    private final PreparedStatement sqlBuscaTodos;
    private final PreparedStatement sqlBuscaTodasFiliais;
    private final PreparedStatement sqlExcluiByProdutoByFilial;
    private final PreparedStatement sqlAtualizaByProdutoByFilial;

    public EstoqueDAO() throws Exception {
        try {
            conexao = ConexaoDB.getConexao();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EstoqueDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Impossível carregar o driver do banco de dados!");
        } catch (SQLException ex) {
            Logger.getLogger(EstoqueDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Impossível conectar com o banco de dados!");
        }
        String sql = null;
        try {
            sql = "INSERT INTO estoque(filial, produto, quantidade) VALUES (?,?,?)"; 
            sqlCriaEstoque = conexao.prepareStatement(sql);

            sql = "SELECT * FROM estoque WHERE filial=?";
            sqlBuscaByFilial = conexao.prepareStatement(sql);

            sql = "SELECT * FROM estoque WHERE produto=?";
            sqlBuscaByProduto = conexao.prepareStatement(sql);

            sql = "SELECT * FROM estoque WHERE filial=? AND produto=?";
            sqlBuscaByProdutoByFilial = conexao.prepareStatement(sql);

            sql = "SELECT * FROM estoque ORDER BY filial";
            sqlBuscaTodos = conexao.prepareStatement(sql);

            sql = "SELECT distinct(filial) FROM estoque";
            sqlBuscaTodasFiliais = conexao.prepareStatement(sql);

            sql = "DELETE FROM estoque WHERE filial=? AND produto=?";
            sqlExcluiByProdutoByFilial = conexao.prepareStatement(sql);

            sql = "UPDATE estoque SET filial = ?, produto = ?, quantidade = ? "
                    + "WHERE filial = ? AND produto=?";
            sqlAtualizaByProdutoByFilial = conexao.prepareStatement(sql);

        } catch (SQLException ex) {
            Logger.getLogger(EstoqueDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Impossível preparar a SQL: " + sql);
        }

    }

    public void criaEstoque(Estoque estoque) throws Exception {
        sqlCriaEstoque.clearParameters();
        sqlCriaEstoque.setString(1, estoque.getFilial());  
        sqlCriaEstoque.setString(2, estoque.getProduto());
        sqlCriaEstoque.setInt(3, estoque.getQuantidade());
        sqlCriaEstoque.executeUpdate();
    }

    public List<Estoque> buscaByFilial(String filial) throws Exception {
        List<Estoque> estoques = new ArrayList<Estoque>();
        sqlBuscaByFilial.clearParameters();
        sqlBuscaByFilial.setString(1, filial);
        ResultSet resultado = sqlBuscaByFilial.executeQuery();
        while (resultado.next()) {
            Estoque estoque = new Estoque();
            estoque.setFilial(resultado.getString("filial"));
            estoque.setProduto(resultado.getString("produto"));
            estoque.setQuantidade(resultado.getInt("quantidade"));
            estoques.add(estoque);
        }
        return estoques;
    }

    public Estoque buscaByProduto(String produto) throws Exception {
        Estoque estoque = null;
        sqlBuscaByProduto.clearParameters();
        sqlBuscaByProduto.setString(1, produto);
        ResultSet resultado = sqlBuscaByProduto.executeQuery();
        if (resultado.next()) {
            estoque = new Estoque();
            estoque.setFilial(resultado.getString("filial"));
            estoque.setProduto(resultado.getString("produto"));
            estoque.setQuantidade(resultado.getInt("quantidade"));
        } else {
            throw new Exception("Estoque não encontrado nos registros!");
        }
        return estoque;
    }

    public Estoque buscaByProdutoByFilial(String produto, String filial) throws Exception {
        Estoque estoque = null;
        sqlBuscaByProdutoByFilial.clearParameters();
        sqlBuscaByProdutoByFilial.setString(1, filial);
        sqlBuscaByProdutoByFilial.setString(2, produto);
        ResultSet resultado = sqlBuscaByProdutoByFilial.executeQuery();
        if (resultado.next()) {
            estoque = new Estoque();
            estoque.setFilial(resultado.getString("filial"));
            estoque.setProduto(resultado.getString("produto"));
            estoque.setQuantidade(resultado.getInt("quantidade"));
        }
        return estoque;
    }

    public List<Estoque> buscaTodos() throws Exception {
        List<Estoque> estoques = new ArrayList<Estoque>();
        ResultSet resultado = sqlBuscaTodos.executeQuery();
        while (resultado.next()) {
            Estoque estoque = new Estoque();
            estoque.setFilial(resultado.getString("filial"));
            estoque.setProduto(resultado.getString("produto"));
            estoque.setQuantidade(resultado.getInt("quantidade"));
            estoques.add(estoque);
        }
        return estoques;
    }

    public List<Estoque> buscaTodasFiliais() throws Exception {
        List<Estoque> estoques = new ArrayList<Estoque>();
        ResultSet resultado = sqlBuscaTodasFiliais.executeQuery();
        while (resultado.next()) {
            Estoque estoque = new Estoque();
            estoque.setFilial(resultado.getString("filial"));
            estoques.add(estoque);
        }
        return estoques;
    }

    public void excluiByProdutoByFilial(String produto, String filial) throws SQLException {
        sqlExcluiByProdutoByFilial.clearParameters();
        sqlExcluiByProdutoByFilial.setString(1, filial);
        sqlExcluiByProdutoByFilial.setString(2, produto);
        sqlExcluiByProdutoByFilial.executeUpdate();
    }

    public void atualizaEstoque(Estoque estoque, String filial, String produto) throws Exception {
        sqlAtualizaByProdutoByFilial.clearParameters();
        sqlAtualizaByProdutoByFilial.setString(1, estoque.getFilial());
        sqlAtualizaByProdutoByFilial.setString(2, estoque.getProduto());
        sqlAtualizaByProdutoByFilial.setInt(3, estoque.getQuantidade());
        sqlAtualizaByProdutoByFilial.setString(4, filial);
        sqlAtualizaByProdutoByFilial.setString(5, produto);
        sqlAtualizaByProdutoByFilial.executeUpdate();
    }
}
