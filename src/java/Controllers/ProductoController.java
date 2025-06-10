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

                break;
            case "eliminar":

                break;
            case "buscar":

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
