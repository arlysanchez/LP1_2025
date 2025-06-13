/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Dao.ProductoDaoImpl;
import Interface.IProducto;
import Model.Producto;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author LAB_REDES
 */
@MultipartConfig
@WebServlet(name = "ProductoController", urlPatterns = {"/ProductoController"})
public class ProductoController extends HttpServlet {

    private final IProducto productoDao = new ProductoDaoImpl();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            // Handle the case where action is null. Default to listing products.
            listarProductos(request, response);
            return;
        }

        switch (action) {
            case "guardar":
                guardarProducto(request, response);
                break;
            case "editar":
             editarProducto(request, response);
                break;
            case "eliminar":
                eliminarProducto(request, response);

                break;
            case "buscar":
              BuscarProducto(request,response);
                break;
            default:
                listarProductos(request, response);
                break;
        }

    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Producto> productos = productoDao.lista();
        String json = new Gson().toJson(productos);
        System.out.println("json" + json);
        try (PrintWriter out = response.getWriter()) {
            out.print(json);
        }

    }

    private void guardarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String precio = request.getParameter("precio");
        double precioDouble = Double.parseDouble(precio);
        int stock = Integer.parseInt(request.getParameter("stock"));
        Part part = request.getPart("imagen");
        InputStream inputStream = part.getInputStream();

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precioDouble);
        producto.setStock(stock);
        producto.setImagen(inputStream);

        boolean resultado = false; // Inicializar con un valor por defecto

        try {
            resultado = productoDao.insert(producto);
            if (resultado) {
                System.out.println("Done");
            } else {
                System.out.println("Failed");
            }
        } catch (Exception e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
            e.printStackTrace();
            resultado = false; // Asegurar que resultado sea false en caso de excepción
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(new Gson().toJson(resultado));
        }
    }
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String id_producto = request.getParameter("id"); // Change this line
             System.out.println("id_prod_serve: " + id_producto);
        if (id_producto != null && !id_producto.isEmpty()) {
            try {
                int id = Integer.parseInt(id_producto);
                boolean resultado = productoDao.delete(id); // Asume que tienes un método delete en tu DAO
                if (resultado) {
                    System.out.println("Producto eliminado con ID: " + id);
                } else {
                    System.out.println("Error al eliminar el producto con ID: " + id);
                }
                try (PrintWriter out = response.getWriter()) {
                    out.print(new Gson().toJson(resultado));
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                 try (PrintWriter out = response.getWriter()) {
                    out.print("ID de producto inválido");
                }
                System.err.println("Error al convertir el ID del producto: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = response.getWriter()) {
                    out.print("Falta el ID del producto");
                }
            System.out.println("Falta el parámetro ID para eliminar el producto");
        }
    }
     private void BuscarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id_producto = request.getParameter("id");
        if (id_producto != null && !id_producto.isEmpty()) {
            try {
                int id = Integer.parseInt(id_producto);
                Producto producto = productoDao.SearchById(id); // Asume que tienes un metodo get
                if (producto != null) {
                    try (PrintWriter out = response.getWriter()) {
                        out.print(new Gson().toJson(producto));
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    try (PrintWriter out = response.getWriter()) {
                        out.print("Producto no encontrado");
                    }
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                try (PrintWriter out = response.getWriter()) {
                    out.print("ID de producto inválido");
                }
                System.err.println("Error al convertir el ID del producto: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = response.getWriter()) {
                out.print("Falta el ID del producto");
            }
            System.out.println("Falta el parámetro ID para obtener el producto");
        }
    }
     
      private void editarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id_producto = request.getParameter("id_producto");  
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String precio = request.getParameter("precio");
        double precioDouble = Double.parseDouble(precio);
        int stock = Integer.parseInt(request.getParameter("stock"));
        Part part = request.getPart("imagen");
        InputStream inputStream = part.getInputStream();


            if (id_producto != null && !id_producto.isEmpty()) {
            try {
                int id = Integer.parseInt(id_producto);

                Producto producto = new Producto();
                producto.setId_producto(id); 
                producto.setNombre(nombre);
                producto.setDescripcion(descripcion);
                producto.setPrecio(precioDouble);
                producto.setStock(stock);
                producto.setImagen(inputStream);
                boolean resultado = productoDao.update(producto);
                if (resultado) {
                    System.out.println("Producto actualizado con éxito");
                } else {
                    System.out.println("Error al actualizar el producto");
                }

                try (PrintWriter out = response.getWriter()) {
                    out.print(new Gson().toJson(resultado));
                }

            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                try (PrintWriter out = response.getWriter()) {
                    out.print("ID de producto inválido");
                }
                System.err.println("Error al convertir el ID del producto: " + e.getMessage());
                e.printStackTrace();
            }

          } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = response.getWriter()) {
                out.print("Falta el ID del producto");
            }
            System.out.println("Falta el parámetro ID para actualizar el producto");
        }
    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
