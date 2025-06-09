/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Interface.IProducto;
import Model.Producto;
import Util.ConexionSingleton;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author LAB_REDES
 */
public class ProductoDaoImpl implements IProducto {

    private Connection cn;

    @Override
    public List<Producto> lista() {
        List<Producto> lista = null;
        Producto pr;
        PreparedStatement st;
        ResultSet rs;
        String query = null;
        try {
            query = "SELECT id_producto,nombre,descripcion,precio,stock FROM productos;";
            lista = new ArrayList<>();
             if (cn == null || cn.isClosed()) {
                System.out.println("La conexión es nula o está cerrada");
            }
            cn = ConexionSingleton.getConnection();
           
            st = cn.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                pr = new Producto();
                pr.setId_producto(rs.getInt("id_producto"));
                pr.setNombre(rs.getString("nombre"));
                pr.setDescripcion(rs.getString("descripcion"));
                pr.setPrecio(rs.getDouble("precio"));
                pr.setStock(rs.getInt("stock"));
                lista.add(pr);
            }

        } catch (Exception e) {
            System.out.println("Error al Listar Productos: " + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {

            } finally {
                if (cn != null) {
                    try {
                    } catch (Exception ex) {
                    }
                }

            }
        }
        return lista;
    }

    @Override
    public boolean insert(Producto pro) {
        boolean flag = false;
        PreparedStatement st;
        String query = null;

        try {
            query = "INSERT INTO productos (nombre, descripcion, precio, stock, imagen) VALUES (?, ?, ?, ?, ?)";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setString(1, pro.getNombre());
            st.setString(2, pro.getDescripcion());
            st.setDouble(3, pro.getPrecio());
            st.setInt(4, pro.getStock());
            st.setBlob(5, pro.getImagen());
            st.executeUpdate();
            //cn.commit();
            flag = true;
        } catch (Exception e) {
            System.out.println("Error Agregar  Productos: " + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            flag = false;
            System.out.println("Erro. No se pudo Agregar");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }
            }
        }
        return flag;
    }

    @Override
    public boolean update(Producto pro) {
        boolean flag = false;
        PreparedStatement st;
        String query = null;

        try {
            query = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?,stock = ?, imagen = ? WHERE id_producto = ?";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setString(1, pro.getNombre());
            st.setString(2, pro.getDescripcion());
            st.setDouble(3, pro.getPrecio());
            st.setInt(4, pro.getStock());
            st.setBlob(5, pro.getImagen());
            st.setInt(6, pro.getId_producto());
            st.executeUpdate();
            flag = true;
        } catch (Exception e) {
            System.out.println("Error Actualizar el Product: " + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            flag = false;
            System.out.println("Erro. No se pudo Actualizar");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }
            }
        }
        return flag;
    }

    @Override
    public Producto SearchById(int id) {
        Producto pr = null;
        PreparedStatement st;
        ResultSet rs;
        String query = null;
        try {
            query = "SELECT * FROM productos WHERE id_producto = ?";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                pr = new Producto();
                pr.setId_producto(rs.getInt("id_producto"));
                pr.setNombre(rs.getString("nombre"));
                pr.setDescripcion(rs.getString("descripcion"));
                pr.setPrecio(rs.getDouble("precio"));
                pr.setStock(rs.getInt("stock"));
            }
        } catch (Exception e) {
            System.out.println("Error buscar al producto ID: " + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            System.out.println("Erro. No se pudo buscar produto por ID");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }
            }
        }
        return pr;
    }

    @Override
    public boolean delete(int id) {
        boolean flag = false;
        PreparedStatement st;
        String query = null;

        try {
            query = "DELETE FROM productos WHERE id_producto = ?";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setInt(1, id);
            st.executeUpdate();
            flag = true;
        } catch (Exception e) {
            System.out.println("Error al eliminar el Product: " + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            flag = false;
            System.out.println("Erro. No se pudo Actualizar");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }
            }
        }
        return flag;
    }

    @Override
    public boolean updateStock(int id, int stock) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void listImg(int id, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
