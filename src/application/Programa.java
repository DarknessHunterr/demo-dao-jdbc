package application;

import model.dao.DaoFactory;
import model.dao.VendedorDao;
import model.entities.Departamento;
import model.entities.Vendedor;

import java.util.Date;
import java.util.List;

public class Programa {
    public static void main(String[] args) {

        VendedorDao vendedorDao = DaoFactory.createVendedorDao();

        System.out.println("=== TESTE 1: VENDEDOR findById===");
        Vendedor vendedor = vendedorDao.findById(3);
        System.out.println(vendedor);

        System.out.println("\n=== TESTE 1: VENDEDOR findByDepartment===");
        Departamento departamento = new Departamento(2,null);
        List<Vendedor> vendedores = vendedorDao.findByDepartment(departamento);
        for (Vendedor v : vendedores){
            System.out.println(v);
        }
    }
}
