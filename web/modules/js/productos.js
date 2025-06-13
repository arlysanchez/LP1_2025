$(document).ready(function () {
    //traer una lista
    cargarProductos();
    $("#productoForm").submit(function (event) {
        event.preventDefault();
        guardarProducto();
    });
    $("#productosTabla").on("click", ".btn-eliminar", function () {
        const id_producto = $(this).data("id_producto");
       // console.log("id_producto", id_producto);
        eliminarProducto(id_producto);
    });
     $("#productosTabla").on("click", ".btn-editar", function () {
        const id_producto = $(this).data("id_producto");
       // console.log("id_producto_para_editar", id_producto);
        cargarProductoParaEditar(id_producto);
    });

});

function cargarProductos() {
    let tabla = $("#productosTabla");
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
                        <td><img src="${config.baseUrl}/ImagenController?id=${producto.id_producto}"  style="width: 50px; height: auto;"></td>
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

function guardarProducto() {
    let formData = new FormData($("#productoForm")[0]);

    // Determina si es una inserción o actualización basándose en el id_producto
    const idProducto = $("#id_producto").val();
    const action = idProducto ? "editar" : "guardar";  //EDITAR
    formData.append("action", action);
    formData.forEach((value, key) => {
        console.log(key, value);
    });

    $.ajax({
        url: config.baseUrl + "/ProductoController",
        type: "POST",
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
            console.log("Producto guardado:", response);
            Swal.fire({
                title: "Success",
                text: "Producto guardado exitosamente.",
                icon: "success"
            });

            $("#productoForm")[0].reset();
            cargarProductos();
        },
        error: function (xhr, status, error) {
            console.error("Error al guardar el producto:", xhr.responseText);
            Swal.fire({
                title: "¡Error!",
                text: "Error al guardar el producto.",
                icon: "error"
            });
        }
    });
}
function eliminarProducto(id_producto) {
    console.log("idpr", id_producto);
    Swal.fire({
        title: "¿Estás seguro?",
        text: "Eliminar el producto",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Sí, eliminarlo!"
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: config.baseUrl + "/ProductoController?action=eliminar&id=" + id_producto,
                type: "GET", // Usar GET para la eliminación
                dataType: "json",
                success: function (response) {
                    if (response) { // Verificar la respuesta del servidor (true si se eliminó)
                        Swal.fire({
                            title: "¡Eliminado!",
                            text: "El producto ha sido eliminado.",
                            icon: "success"
                        });
                        cargarProductos(); // Recargar la lista de productos
                    } else {
                        Swal.fire({
                            title: "¡Error!",
                            text: "No se pudo eliminar el producto.",
                            icon: "error"
                        });
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Error al eliminar el producto:", error);
                    Swal.fire({
                        title: "¡Error!",
                        text: "Error al eliminar el producto.",
                        icon: "error"
                    });
                }
            });
        }
    });
}

function cargarProductoParaEditar(id_producto) {
    $.ajax({
        url: config.baseUrl + "/ProductoController?action=buscar&id="+id_producto,
        type: "GET",
        dataType: "json",
        success: function (producto) {
            if (producto) {
                // Llena el formulario con los datos del producto
                $("#id_producto").val(producto.id_producto);
                $("#nombre").val(producto.nombre);
                $("#descripcion").val(producto.descripcion);
                $("#precio").val(producto.precio);
                $("#stock").val(producto.stock);

            } else {
                Swal.fire({
                    title: "¡Error!",
                    text: "No se encontró el producto.",
                    icon: "error"
                });
            }
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



















    