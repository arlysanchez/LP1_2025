/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

$(document).ready(function () {
    cargarCarrito();
});

function cargarCarrito() {
    $.ajax({
        url: "AppController?action=carrito",
        type: "GET",
        dataType: "json",
        success: function (response) {
            if (response.status === "success") {
                let carrito = response.listCarrito;
                let totalPagar = response.totalPagar;
                let tbody = $("#carrito-body");
                tbody.empty();

                if (carrito.length === 0) {
                    tbody.append(`<tr><td colspan="7" class="text-center">No hay productos en el carrito</td></tr>`);
                    $("#btnGenerarCompra").addClass("disabled");
                } else {
                    carrito.forEach((producto, index) => {
                        tbody.append(`
                            <tr>
                                <td>${index + 1}</td>
                                <td>${producto.nombre}</td>
                                <td>${producto.descripcion}</td>
                                <td>S/.${producto.precioCompra.toFixed(2)}</td>
                                <td>
                                    <input type="number" class="form-control text-center actualizar-cantidad" 
                                           data-id="${producto.idProducto}" value="${producto.cantidad}" min="1" style="width: 80px;">
                                </td>
                                <td>S/.${producto.subTotal.toFixed(2)}</td>
                                <td>
                                    <button class="btn btn-danger btn-sm eliminar-producto" data-id="${producto.idProducto}">
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        `);
                    });

                    $("#btnGenerarCompra").removeClass("disabled");
                }

                $("#totalPagar").val(`S/.${totalPagar.toFixed(2)}`);
                $("#totalPagarFinal").val(`S/.${totalPagar.toFixed(2)}`);
            }
        },
        error: function (xhr, status, error) {
            console.error("Error al cargar el carrito:", error);
            Swal.fire("Error", "No se pudo cargar el carrito", "error");
        }
    });
    
}

$(document).on("change", ".actualizar-cantidad", function () {
    let idProducto = $(this).data("id");
    let nuevaCantidad = $(this).val();

    if (nuevaCantidad < 1) {
        Swal.fire("Error", "La cantidad debe ser al menos 1", "warning");
        $(this).val(1);
        return;
    }

    $.ajax({
        url: `AppController?action=ActualizarCantidad&idpro=${idProducto}&cantidad=${nuevaCantidad}`,
        type: "GET",
        dataType: "json",
        success: function (response) {
            console.log("response", response);
            if (response.status === "success") {
                cargarCarrito();
            }
        },
        error: function () {
            Swal.fire("Error", "No se pudo actualizar la cantidad", "error");
        }
    });
});
$(document).on("click", ".eliminar-producto", function () {
    let idProducto = $(this).data("id");

    Swal.fire({
        title: "¿Estás seguro?",
        text: "Este producto se eliminará del carrito",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `AppController?action=Delete&id=${idProducto}`,
                type: "GET",
                dataType: "json",
                success: function (response) {
                    if (response.status === "success") {
                        Swal.fire("Eliminado", "Producto eliminado del carrito", "success");
                        cargarCarrito();
                    }
                },
                error: function () {
                    Swal.fire("Error", "No se pudo eliminar el producto", "error");
                }
            });
        }
    });
});

$(document).on("click", "#btnGenerarCompra", function () {
    console.log("llegas");
    $.ajax({
        url: `AppController?action=GenerarCompra`,
        type: "GET",
        dataType: "json",
        success: function (response) {
            console.log("response", response);
            if (response.status === "success") {
                Swal.fire({
                    title: "Success",
                    text: "Venta procesada...!!!",
                    icon: "success",
                    timer: 2000, // Muestra el mensaje por 2 segundos
                    showConfirmButton: false
                }).then(() => {
                    window.location.href = "index.html";
                });

            }else{
                Swal.fire({
                        title: "¡Error!",
                        text: "Iniciar Sesion, para generar su Pedido",
                        icon: "error"
                    });
            }
        },
        error: function () {
            Swal.fire("Error", "No se pudo actualizar la cantidad", "error");
        }
    });
});