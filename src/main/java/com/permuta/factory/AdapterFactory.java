package com.permuta.factory;

import com.permuta.adapter.Alex.BibliotecaAdapter;
import com.permuta.adapter.Alex.CompraAdapter;
import com.permuta.adapter.Alex.JuegoAdapter;
import com.permuta.adapter.Alex.ReseniaAdapter;
import com.permuta.adapter.Alex.UsuarioAdapter;
import org.alexyivan.controlador.BibliotecaControlador;
import org.alexyivan.controlador.CompraControlador;
import org.alexyivan.controlador.JuegoControlador;
import org.alexyivan.controlador.ResenhaControlador;
import org.alexyivan.controlador.UsuarioControlador;
import org.alexyivan.repositorio.inmemory.BibliotecaRepoInMemory;
import org.alexyivan.repositorio.inmemory.CompraRepoInMemory;
import org.alexyivan.repositorio.inmemory.JuegoRepoInMemory;
import org.alexyivan.repositorio.inmemory.ResenhaRepoInMemory;
import org.alexyivan.repositorio.inmemory.UsuarioRepoInMemory;

import java.lang.reflect.Field;
import java.util.ArrayList;

public final class AdapterFactory {

    static UsuarioRepoInMemory usuarioRepo = new UsuarioRepoInMemory();
    static CompraRepoInMemory compraRepo = new CompraRepoInMemory();
    static BibliotecaRepoInMemory bibliotecaRepo = new BibliotecaRepoInMemory();
    static JuegoRepoInMemory juegoRepo = new JuegoRepoInMemory();
    static ResenhaRepoInMemory resenhaRepo = new ResenhaRepoInMemory();

    public static AdapterBundle getAdapterBundle(String groupID)
            throws NoSuchFieldException, IllegalAccessException {

        Class<?> userClazz = UsuarioRepoInMemory.class;
        Class<?> compraClazz = CompraRepoInMemory.class;
        Class<?> bibliotecaClazz = BibliotecaRepoInMemory.class;
        Class<?> juegoClazz = JuegoRepoInMemory.class;
        Class<?> resenhaClazz = ResenhaRepoInMemory.class;

        resetList(userClazz, "usuarios");
        resetList(compraClazz, "compras");
        resetList(bibliotecaClazz, "bibliotecas");
        resetList(juegoClazz, "juegos");
        resetList(resenhaClazz, "resenhas");

        resetLong(userClazz, "idCounter");
        resetLong(compraClazz, "idCounter");
        resetLong(bibliotecaClazz, "idCounter");
        resetLong(juegoClazz, "idCounter");
        resetLong(resenhaClazz, "idCounter");

        var bibliotecaControlador = new BibliotecaControlador(bibliotecaRepo, usuarioRepo, juegoRepo, compraRepo);

        return new AdapterBundle(
                new UsuarioAdapter(new UsuarioControlador(usuarioRepo)),
                new CompraAdapter(new CompraControlador(compraRepo, usuarioRepo, bibliotecaRepo, juegoRepo)),
                new JuegoAdapter(new JuegoControlador(juegoRepo)),
                new ReseniaAdapter(new ResenhaControlador(resenhaRepo, juegoRepo, usuarioRepo, bibliotecaRepo)),
                new BibliotecaAdapter(bibliotecaControlador)
        );
    }

    private static void resetList(Class<?> clazz, String fieldName) throws IllegalAccessException {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            ((ArrayList<?>) field.get(null)).clear();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static void resetLong(Class<?> clazz, String fieldName) throws IllegalAccessException {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setLong(null, 1L);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

