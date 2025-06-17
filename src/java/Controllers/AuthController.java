/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Dao.PersonaDaoImpl;
import Dao.UsuarioDaoImpl;
import Interface.IPersona;
import Interface.IUsuario;
import Model.Persona;
import Model.Usuario;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author LAB_REDES
 */
@WebServlet(name = "AuthController", urlPatterns = {"/AuthController"})
public class AuthController extends HttpServlet {

    IUsuario uDao = new UsuarioDaoImpl();
    Usuario us = new Usuario();
    IPersona PDao = new PersonaDaoImpl();
    Persona p = new Persona();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AuthController</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AuthController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
       
        
        
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        System.out.println("action" + action);
        JsonObject jsonResponse = new JsonObject();

        try (PrintWriter out = response.getWriter()) {
            if ("validar".equals(action)) {
                String user = request.getParameter("usuario");
                String passw = request.getParameter("password");

                System.out.println("user." + user);
                System.out.println("pass." + passw);

                us = uDao.validate(user, passw);

                if (us != null && us.getUsuario() != null) {
                    // Crear o recuperar sesión
                    HttpSession session = request.getSession(true);
                    session.setAttribute("usuario", us);

                    System.out.println("usuario en sesion" + us.getPersona().getId_persona());
                    System.out.println("usuario en sesion" + us.getRol());

                    // Respuesta JSON en caso de éxito
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Inicio de sesión exitoso");
                    jsonResponse.add("userData", new Gson().toJsonTree(us));

                } else {
                    // Respuesta JSON en caso de credenciales incorrectas
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "Usuario o contraseña incorrectos");
                }

                out.print(jsonResponse.toString());

            } else if ("Salir".equals(action)) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Sesión cerrada correctamente");
                out.print(jsonResponse.toString());

            } else if ("register".equals(action)) {

                String nombre = request.getParameter("nombre");
                String email = request.getParameter("email");
                String direccion = request.getParameter("direccion");
                String telefono = request.getParameter("telefono");
                String password = request.getParameter("password");
                
                
                p = new Persona();

                p.setNombre(nombre);
                p.setEmail(email);
                p.setDireccion(direccion);
                p.setTelefono(telefono);
                us.setPassword(password);

                int resultado = 0; 

                try {
                    resultado = PDao.insert(p,us);
                    
                    if (resultado !=0) {
                       jsonResponse.addProperty("success", true);
                       jsonResponse.addProperty("message", "registro satisfactorio");
                    } else {
                       jsonResponse.addProperty("success", false);
                       jsonResponse.addProperty("message", "error de registro");
                    }
                } catch (Exception e) {
                    System.err.println("Error al insertar una persona: " + e.getMessage());
                     jsonResponse.addProperty("success", false);
                     jsonResponse.addProperty("message", "error de registro");
                }
                out.print(jsonResponse.toString());

            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Error en el servidor: " + e.getMessage());
            e.printStackTrace();
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse.toString());
            }
        }

        
        
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
