/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Model.Producto;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author LAB_REDES
 */
public interface IProducto {

    public List<Producto> lista();

    public boolean insert(Producto pro);

    public boolean update(Producto pro);

    public Producto SearchById(int id);

    public boolean delete(int id);

    public boolean updateStock(int id, int stock);

    public void listImg(int id, HttpServletResponse response);

}
