/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


$(document).ready(function () {
    //traer una lista
    cargarProductos();

    $(document).on("click", ".btn-addCar", function () {
        let id_producto = $(this).data("id_producto");
      //  console.log("id_producto",id_producto);
        AddCarrito(id_producto);
    });
});

function cargarProductos() {
    let contenedor = $("#productosContainer");

    $.ajax({
        url: config.baseUrl + "/ProductoController",
        type: "GET",
        dataType: "json",
        success: function (productos) {
            contenedor.empty();

            if (productos.length === 0) {
                contenedor.append('<p class="text-center">No hay productos disponibles.</p>');
                return;
            }

            productos.forEach((producto) => {
                let card = `
                    <div class="col-sm-4 mb-4">
                        <div class="card">
                            <img src="${config.baseUrl}/ImagenController?id=${producto.id_producto}" class="card-img-top" alt="${producto.nombre}" >
                            <div class="card-body">
                                <h5 class="card-title text-center">${producto.nombre}</h5>
                                <p class="card-text">${producto.descripcion}</p>
                                <p class="fw-bold text-center">$${producto.precio}</p>
                            </div>
                            <div class="card-footer d-flex justify-content-between align-items-center">
                               <button class="btn btn-success  btn-addCar" data-id_producto="${producto.id_producto}"><i class="fa-solid fa-cart-shopping"></i> Add Car</button>
                            </div>
                        </div>
                    </div>
                `;
                contenedor.append(card);
            });
        },
        error: function (xhr, status, error) {
            console.error("Error al obtener productos:", error);
            contenedor.append('<p class="text-center text-danger">Error al cargar productos.</p>');
        }
    });
}

function AddCarrito(id_producto) {
 //   console.log("id_producto",id_producto);
    $.ajax({
        url: config.baseUrl + "/AppController?action=AddCarrito&id="+id_producto,
        type: "GET",
        dataType: "json",
         success: function (response) {
                Swal.fire({
                    title: "Success",
                    text: "Producto añadido exitosamente.",
                    icon: "success",
                    timer: 2000, // Muestra el mensaje por 2 segundos
                    showConfirmButton: false
                }).then(() => {
                    window.location.href = "index.html";
                });
            },
        error: function (xhr, status, error) {
            console.error("Error al obtener el producto:", error);
            Swal.fire({
                title: "¡Error!",
                text: "Error al obtener el producto.",
                icon: "error"
            });
        }
    });
}