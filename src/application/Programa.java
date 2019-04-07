package application;

import model.dao.DaoFactory;
import model.dao.VendedorDao;
import model.entities.Departamento;
import model.entities.Vendedor;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Programa {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        VendedorDao vendedorDao = DaoFactory.createVendedorDao();

        System.out.println("=== TESTE 1: VENDEDOR findById===");
        Vendedor vendedor = vendedorDao.findById(3);
        System.out.println(vendedor);

        System.out.println("\n=== TESTE 2: VENDEDOR findByDepartment===");
        Departamento departamento = new Departamento(2,null);
        List<Vendedor> vendedores = vendedorDao.findByDepartment(departamento);
        for (Vendedor v : vendedores){
            System.out.println(v);
        }

        System.out.println("\n=== TESTE 3: VENDEDOR findAll===");
        vendedores = vendedorDao.findAll();
        for (Vendedor v : vendedores){
            System.out.println(v);
        }

        System.out.println("\n=== TESTE 4: VENDEDOR INSERT===");
        Vendedor vendedor1 = new Vendedor(null,"Greg","greg@gmail.com",new Date(), 4000.0, departamento);
        vendedorDao.insert(vendedor1);
        System.out.println("Inserido com sucesso! Novo Id = " + vendedor1.getId());


        System.out.println("\n=== TESTE 5: VENDEDOR UPDATE===");
        vendedor = vendedorDao.findById(1);
        vendedor.setNome("Martha Waine");
        vendedorDao.update(vendedor);
        System.out.println("Atualização feita!");

        System.out.println("\n=== TESTE 6: VENDEDOR DELETE===");
        System.out.println("DIGITE UM ID PARA O TESTE DE DELEÇÃO: ");
        int id = sc.nextInt();
        vendedorDao.deleteById(id);
        System.out.println("DELEÇÃO FEITA!");

        sc.close();

    }
}
