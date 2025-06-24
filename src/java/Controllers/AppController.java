/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Dao.PedidoDaoImpl;
import Dao.ProductoDaoImpl;
import Interface.IPedido;
import Interface.IProducto;
import Model.Carrito;
import Model.EstadoPedido;
import Model.Pedido;
import Model.Producto;
import Model.Usuario;
import Util.Fecha;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author LAB_REDES
 */
@WebServlet(name = "AppController", urlPatterns = {"/AppController"})
public class AppController extends HttpServlet {

    Producto p = new Producto();
    IProducto pDao = new ProductoDaoImpl();
    List<Producto> prod = new ArrayList<>();
    List<Carrito> listCarrito = new ArrayList<>();

    int item;
    double totalPagar = 0.0;
    int cantidad = 1;
    Carrito car;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        System.out.println("action" + action);
        Map<String, Object> jsonResponse = new HashMap<>();
        try {
            switch (action) {
                case "AddCarrito":
                    int pos = 0;
                    cantidad = 1;
                    int id = Integer.parseInt(request.getParameter("id"));
                    p = pDao.SearchById(id);

                    if (p == null) {
                        jsonResponse.put("status", "error");
                        jsonResponse.put("message", "Producto no encontrado.");
                        out.write(new Gson().toJson(jsonResponse));
                        return;
                    }

                    if (listCarrito.size() > 0) {
                        for (int i = 0; i < listCarrito.size(); i++) {
                            if (id == listCarrito.get(i).getIdProducto()) {
                                pos = i;
                            }
                        }
                        if (id == listCarrito.get(pos).getIdProducto()) {
                            cantidad = listCarrito.get(pos).getCantidad() + cantidad;
                            double subtotal = listCarrito.get(pos).getPrecioCompra() * cantidad;
                            listCarrito.get(pos).setCantidad(cantidad);
                            listCarrito.get(pos).setSubTotal(subtotal);
                        } else {
                            item = item + 1;
                            car = new Carrito();
                            car.setItem(item);
                            car.setIdProducto(p.getId_producto());
                            car.setNombre(p.getNombre());
                            car.setDescripcion(p.getDescripcion());
                            car.setPrecioCompra(p.getPrecio());
                            car.setCantidad(cantidad);
                            car.setSubTotal(cantidad * p.getPrecio());
                            listCarrito.add(car);
                        }

                    } else {
                        item = item + 1;
                        car = new Carrito();
                        car.setItem(item);
                        car.setIdProducto(p.getId_producto());
                        car.setNombre(p.getNombre());
                        car.setDescripcion(p.getDescripcion());
                        car.setPrecioCompra(p.getPrecio());
                        car.setCantidad(cantidad);
                        car.setSubTotal(cantidad * p.getPrecio());
                        listCarrito.add(car);

                    }

                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Producto agregado al carrito.");
                    jsonResponse.put("cartCount", listCarrito.size());
                    out.write(new Gson().toJson(jsonResponse));
                    break;

                case "carrito":
                    totalPagar = listCarrito.stream().mapToDouble(Carrito::getSubTotal).sum();
                    jsonResponse.put("status", "success");
                    jsonResponse.put("listCarrito", listCarrito);
                    jsonResponse.put("totalPagar", totalPagar);
                    out.write(new Gson().toJson(jsonResponse));
                    break;

                case "ActualizarCantidad":
                    int idpro = Integer.parseInt(request.getParameter("idpro"));
                    int cant = Integer.parseInt(request.getParameter("cantidad"));
                    System.out.println("idpro" + idpro);
                    System.out.println("idpro" + cant);

                    boolean actualizado = false;
                    for (int i = 0; i < listCarrito.size(); i++) {
                        if (listCarrito.get(i).getIdProducto() == idpro) {
                            listCarrito.get(i).setCantidad(cant);
                            double sutTot = listCarrito.get(i).getPrecioCompra() * cant;
                            listCarrito.get(i).setSubTotal(sutTot);
                            actualizado = true;
                            break;
                        }
                    }

                    totalPagar = listCarrito.stream().mapToDouble(Carrito::getSubTotal).sum();
                    jsonResponse.put("status", actualizado ? "success" : "error");
                    jsonResponse.put("message", actualizado ? "Cantidad actualizada correctamente" : "Producto no encontrado");
                    jsonResponse.put("totalPagar", totalPagar);

                    out.write(new Gson().toJson(jsonResponse));
                    break;

                case "Delete":
                    int idproducto = Integer.parseInt(request.getParameter("id"));
                    listCarrito.removeIf(c -> c.getIdProducto() == idproducto);
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Producto eliminado del carrito.");
                    jsonResponse.put("cartCount", listCarrito.size());
                    out.write(new Gson().toJson(jsonResponse));
                    break;

                case "GenerarCompra":
                    HttpSession sesion = request.getSession();
                    Usuario user = (Usuario) sesion.getAttribute("usuario");
                    System.out.println("user sesion"+user.getId_usuario());
                    if (user == null) {
                        System.out.println("Usuario no está en sesión");
                        jsonResponse.put("status", "error");
                        jsonResponse.put("message", "Debe iniciar sesión para realizar la compra.");
                        out.write(new Gson().toJson(jsonResponse));
                        return;
                    }
                    System.out.println("Usuario en sesión: " + user.getPersona().getId_persona());

                    IPedido pedidoDao = new PedidoDaoImpl();
                    IProducto pDaoStock = new ProductoDaoImpl();
                    boolean stockDisponible = true;

                    for (Carrito c : listCarrito) {
                        Producto prod = pDaoStock.SearchById(c.getIdProducto());
                        if (prod.getStock() < c.getCantidad()) {
                            stockDisponible = false;
                            break;
                        }
                    }

                    if (!stockDisponible) {
                        jsonResponse.put("status", "error");
                        jsonResponse.put("message", "Stock insuficiente para uno o más productos.");
                        out.write(new Gson().toJson(jsonResponse));
                        return;
                    }

                    if (listCarrito.isEmpty()) {
                        jsonResponse.put("status", "error");
                        jsonResponse.put("message", "El carrito está vacío.");
                        out.write(new Gson().toJson(jsonResponse));
                        return;
                    }

                    Pedido pedido = new Pedido(0, user.getPersona(), totalPagar, EstadoPedido.PROCESADO, Fecha.FechaBD2(), listCarrito);
                    int result = pedidoDao.generarPedido(pedido);

                    if (result > 0) {
                        for (Carrito c : listCarrito) {
                            Producto prod = pDaoStock.SearchById(c.getIdProducto());
                            int nuevoStock = prod.getStock() - c.getCantidad();
                            pDaoStock.updateStock(c.getIdProducto(), nuevoStock);
                        }
                        listCarrito.clear();

                        jsonResponse.put("status", "success");
                        jsonResponse.put("message", "Compra realizada con éxito.");
                        out.write(new Gson().toJson(jsonResponse));
                    } else {
                        jsonResponse.put("status", "error");
                        jsonResponse.put("message", "Error al procesar la compra.");
                        out.write(new Gson().toJson(jsonResponse));
                    }
                    break;

                default:
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Acción no válida.");
                    out.write(new Gson().toJson(jsonResponse));
            }
        } catch (Exception e) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Ocurrió un error: " + e.getMessage());
            out.write(new Gson().toJson(jsonResponse));
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
