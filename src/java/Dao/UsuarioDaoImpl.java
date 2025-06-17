/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Interface.IUsuario;
import Model.Persona;
import Model.Rol;
import Model.Usuario;
import Util.ConexionSingleton;
import java.sql.*;

/**
 *
 * @author LAB_REDES
 */
public class UsuarioDaoImpl implements IUsuario{
    private Connection cn;

    @Override
    public Usuario validate(String user, String passw) {
        Usuario u = null;
        Persona p = null;
        PreparedStatement st;
        ResultSet rs;
        String query = null;

        try {
            u = new Usuario();
            p= new Persona();
            String hashedPassword = u.HashPassword(passw);
            query = "SELECT u.id_usuario,u.usuario,u.rol,p.id_persona, p.nombre FROM usuarios u, persona p WHERE p.id_persona = u.id_persona AND u.usuario = ? and u.password = ?; ";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setString(1, user);
            st.setString(2, hashedPassword);
            rs = st.executeQuery();
            while (rs.next()) {
                u = new Usuario();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setUsuario(rs.getString("usuario"));
                u.setRol(Rol.valueOf(rs.getString("rol").toUpperCase()));
                p.setId_persona(rs.getInt("id_persona"));
                p.setNombre(rs.getString("nombre"));
                u.setPersona(p);
                
            }

        } catch (Exception e) {
            System.out.println("Error al validar el usuario: " + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            System.out.println("Erro. No se pudo validar el usuario");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }
            }
        }
        return u;

    }
        
    }
    

