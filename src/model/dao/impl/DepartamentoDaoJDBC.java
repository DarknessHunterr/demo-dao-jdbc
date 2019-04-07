package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartamentoDao;
import model.entities.Departamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDaoJDBC implements DepartamentoDao {

    private Connection conn;

    public DepartamentoDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Departamento obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("INSERT INTO department " +
                    "(Name) " +
                    "VALUES" +
                    "(?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1,obj.getNome());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Falha ao inserir! Nenhuma linha afetada!");
            }
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Departamento obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("UPDATE department " +
                            "SET Name = ? " +
                            "WHERE Id = ?",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1,obj.getNome());
            st.setInt(2,obj.getId());

            st.executeUpdate();
        } catch (SQLException e){
            throw  new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

            st.setInt(1,id);

            st.executeUpdate();
        } catch (SQLException e){
            throw  new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Departamento findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT dep.* AS DepName" +
                    "FROM department dep " +
                    "WHERE dep.Id = ?");

            st.setInt(1,id);
            rs = st.executeQuery();
            if (rs.next()){
                return instantiateDepartment(rs);
            }
            return null;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    private Departamento instantiateDepartment(ResultSet rs) throws SQLException {
        Departamento departamento = new Departamento();
        departamento.setId(rs.getInt("DepartmentId"));
        departamento.setNome(rs.getString("DepName"));
        return departamento;
    }

    @Override
    public List<Departamento> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT dep.* " +
                    "FROM department dep " +
                    "ORDER BY Name");

            rs = st.executeQuery();

            List<Departamento> departamentos = new ArrayList<>();

            while (rs.next()){
                Departamento departamento = instantiateDepartment(rs);
                departamentos.add(departamento);
            }
            return departamentos;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
