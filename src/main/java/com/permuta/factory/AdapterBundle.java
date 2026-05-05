package com.permuta.factory;

import com.permuta.adapter.teacherCode.controller.IBibliotecaController;
import com.permuta.adapter.teacherCode.controller.ICompraController;
import com.permuta.adapter.teacherCode.controller.IJuegoController;
import com.permuta.adapter.teacherCode.controller.IReseniaController;
import com.permuta.adapter.teacherCode.controller.IUsuarioController;

public record AdapterBundle(
    IUsuarioController usuarioController,
    ICompraController compraController,
    IJuegoController juegoController,
    IReseniaController reseniaController,
    IBibliotecaController bibliotecaController
) {

}
