/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Test;

import Dao.ProductoDaoImpl;
import Interface.IProducto;
import Model.Producto;
import Util.ConexionSingleton;

/**
 *
 * @author LAB_REDES
 */
import java.sql.*;
import java.util.List;

public class Test {
    
    public static void main(String[] args) {
        
        Test t = new Test();
         t.testConexion();
      // t.listProductos();
     // t.insert();
     //t.SearchById();
      // t.update();
    //  t.delete();
    }
    
    public void testConexion() {
        ConexionSingleton c = new ConexionSingleton();
        try {
            Connection connection = c.getConnection();
            if (connection != null && !connection.isClosed()) {
                System.out.println("Conexion satisfatoria");
            } else {
                System.out.println("No se puede establecer conexion");
            }
        } catch (Exception e) {
            System.out.println("Error de conexion" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void listProductos() {
        IProducto pro = new ProductoDaoImpl();
        
        List<Producto> listaProducto = pro.lista();
        if (listaProducto != null && !listaProducto.isEmpty()) {
            for (Producto producto : listaProducto) {
                System.out.println("idPro:" + producto.getId_producto());
                System.out.println("Nombre:" + producto.getNombre());
                System.out.println("Descripcion:" + producto.getDescripcion());
                System.out.println("Precio:" + producto.getPrecio());
                System.out.println("Stock:" + producto.getStock());
                
            }
        }
        
    }
    
    public void insert() {
        
        Producto p = new Producto();
        p.setNombre("Yuca");
        p.setDescripcion("de la selva");
        p.setPrecio(10);
        p.setStock(20);
        
        IProducto productoDao = new ProductoDaoImpl();
        boolean result = productoDao.insert(p);
        
        if (result) {
            System.out.println("success!!!");
        } else {
            System.out.println("error");
        }
        
    }
     public void delete() {
       Producto p = new Producto();

       IProducto productoDao = new ProductoDaoImpl();
       boolean result = productoDao.delete(26);

        if (result) {
            System.out.println("success delete!!!");
        } else {
            System.out.println("error");
        }

    }

    public void SearchById() {
        IProducto productoDao = new ProductoDaoImpl();

         Producto  producto = productoDao.SearchById(25);
        if (producto != null) {
             System.out.println("idPro:" + producto.getId_producto());
                System.out.println("Nombre:" + producto.getNombre());
                System.out.println("Descripcion:" + producto.getDescripcion());
                System.out.println("Precio:" + producto.getPrecio());
                System.out.println("Stock:" + producto.getStock());
        } else {
            System.out.println("no existe ese Id");
        }
    }

    public void update() {
        Producto p = new Producto();
        p.setNombre("Papa");
        p.setDescripcion("de la selva");
        p.setPrecio(10);
        p.setStock(20);
        p.setId_producto(25);

         IProducto productoDao = new ProductoDaoImpl();
        boolean result = productoDao.update(p);

        if (result) {
            System.out.println("success update!!!");
        } else {
            System.out.println("error");
        }

    }
    
}
