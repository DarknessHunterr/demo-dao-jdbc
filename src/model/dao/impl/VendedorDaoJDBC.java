package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.VendedorDao;
import model.entities.Departamento;
import model.entities.Vendedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendedorDaoJDBC implements VendedorDao {

    private Connection conn;

    public VendedorDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Vendedor obj) {

    }

    @Override
    public void update(Vendedor obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Vendedor findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT s.*, dep.Name AS DepName " +
                    "FROM seller s INNER JOIN department dep " +
                    "ON s.DepartmentId = dep.Id " +
                    "WHERE s.Id = ?");

            st.setInt(1,id);
            rs = st.executeQuery();
            if (rs.next()){
                Departamento departamento = instantiateDepartment(rs);
                return instantiateVendedor(rs,departamento);
            }
            return null;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    private Vendedor instantiateVendedor(ResultSet rs, Departamento departamento) throws SQLException {
        Vendedor vendedor = new Vendedor();
        vendedor.setId(rs.getInt("Id"));
        vendedor.setNome(rs.getString("Name"));
        vendedor.setEmail(rs.getString("Email"));
        vendedor.setSalarioBase(rs.getDouble("BaseSalary"));
        vendedor.setDataDeAniversario(rs.getDate("BirthDate"));
        vendedor.setDepartamento(departamento);
        return vendedor;
    }

    private Departamento instantiateDepartment(ResultSet rs) throws SQLException {
        Departamento departamento = new Departamento();
        departamento.setId(rs.getInt("DepartmentId"));
        departamento.setNome(rs.getString("DepName"));
        return departamento;
    }

    @Override
    public List<Vendedor> findAll() {
        return null;
    }

    @Override
    public List<Vendedor> findByDepartment(Departamento departamento) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT s.*, dep.Name AS DepName " +
                    "FROM seller s INNER JOIN department dep " +
                    "ON s.DepartmentId = dep.Id " +
                    "WHERE s.DepartmentId = ? " +
                    "ORDER BY Name");

            st.setInt(1,departamento.getId());
            rs = st.executeQuery();

            List<Vendedor> vendedores = new ArrayList<>();
            Map<Integer, Departamento> map = new HashMap<>();

            while (rs.next()){

                Departamento dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"),dep);
                }

                Vendedor vendedor = instantiateVendedor(rs,dep);
                vendedores.add(vendedor);
            }
            return vendedores;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
