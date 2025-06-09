/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

$(document).ready(function () {
    cargarProductos();
});
function cargarProductos() {
    let tabla = $("productosTabla");
    let loading = $("#loadingSpinner");

    loading.show();
    tabla.empty();

    $.ajax({
        url: config.baseUrl + "/ProductoController",
        type: "GET",
        dataType: "json",
        success: function (productos) {
            console.log("Datos recibidos:", productos);
            tabla.empty();

            if (productos.length === 0) {
                tabla.append(`<tr><td colspan="7" class="text-center">No hay productos disponibles.</td></tr>`);
                loading.hide();
                return;
            }

            productos.forEach((producto, index) => {
                tabla.append(`
                    <tr>
                        <td>${index + 1}</td>
                        <td>${producto.nombre}</td>
                        <td>${producto.descripcion}</td>
                        <td>${producto.precio}</td>
                        <td>${producto.stock}</td>
                        <td>
                            <button class="btn btn-danger btn-small btn-eliminar" data-id_producto="${producto.id_producto}"><i class="fa-solid fa-trash"></i></button>
                            <button class="btn btn-warning btn-small btn-editar" data-id_producto="${producto.id_producto}"><i class="fa-solid fa-pencil"></i></button>
                        </td>
                    </tr>`);
            });

            loading.hide();
        },
        error: function (xhr, status, error) {
            console.error("Error al obtener productos:", error);
            tabla.append(`<tr><td colspan="7" class="text-center text-danger">Error al cargar productos.</td></tr>`);
            loading.hide();
        }
    });
}
