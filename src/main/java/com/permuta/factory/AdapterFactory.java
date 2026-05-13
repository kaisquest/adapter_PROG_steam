package com.permuta.factory;

import com.permuta.adapter.Alex.BibliotecaAdapter;
import com.permuta.adapter.Alex.CompraAdapter;
import com.permuta.adapter.Alex.JuegoAdapter;
import com.permuta.adapter.Alex.ReseniaAdapter;
import com.permuta.adapter.Alex.UsuarioAdapter;
import com.permuta.adapter.teacherCode.excepciones.ValidationException;

import org.alexyivan.controlador.BibliotecaControlador;
import org.alexyivan.controlador.CompraControlador;
import org.alexyivan.controlador.JuegoControlador;
import org.alexyivan.controlador.ResenhaControlador;
import org.alexyivan.controlador.UsuarioControlador;
import org.alexyivan.exception.ValidacionException;
import org.alexyivan.modelo.entidad.BibliotecaEntidad;
import org.alexyivan.modelo.entidad.CompraEntidad;
import org.alexyivan.modelo.entidad.JuegoEntidad;
import org.alexyivan.modelo.entidad.UsuarioEntidad;
import org.alexyivan.repositorio.hibernate.BibliotecaRepoHibernate;
import org.alexyivan.repositorio.hibernate.CompraRepoHibernate;
import org.alexyivan.repositorio.hibernate.JuegoRepoHibernate;
import org.alexyivan.repositorio.hibernate.ResenhaRepoHibernate;
import org.alexyivan.repositorio.hibernate.UsuarioRepoHibernate;
import org.alexyivan.repositorio.inmemory.BibliotecaRepoInMemory;
import org.alexyivan.repositorio.inmemory.CompraRepoInMemory;
import org.alexyivan.repositorio.inmemory.JuegoRepoInMemory;
import org.alexyivan.repositorio.inmemory.ResenhaRepoInMemory;
import org.alexyivan.repositorio.inmemory.UsuarioRepoInMemory;
import org.alexyivan.transaction.HibernateTransactionManager;
import org.alexyivan.transaction.ISesionManager;
import org.alexyivan.transaction.ITransactionManager;
import org.alexyivan.transaction.NoOpTransactionManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

public final class AdapterFactory {

    static UsuarioRepoInMemory usuarioRepo = new UsuarioRepoInMemory();
    static CompraRepoInMemory compraRepo = new CompraRepoInMemory();
    static BibliotecaRepoInMemory bibliotecaRepo = new BibliotecaRepoInMemory();
    static JuegoRepoInMemory juegoRepo = new JuegoRepoInMemory();
    static ResenhaRepoInMemory resenhaRepo = new ResenhaRepoInMemory();

    public static final boolean USE_IN_MEMORY = true;

    public static AdapterBundle getAdapterBundle(String groupID)
            throws NoSuchFieldException, IllegalAccessException {
        if (USE_IN_MEMORY) {
            return getInMemoryBundle();
        } else {
            return getHibernateBundle();
        }
    }

    private static AdapterBundle getHibernateBundle() {
        ITransactionManager tm = new HibernateTransactionManager();

        var userRepo = new UsuarioRepoHibernate((ISesionManager) tm);
        var gameRepo = new JuegoRepoHibernate((ISesionManager) tm);
        var purchaseRepo = new CompraRepoHibernate((ISesionManager) tm);
        var libraryRepo = new BibliotecaRepoHibernate((ISesionManager) tm);
        var reviewRepo = new ResenhaRepoHibernate((ISesionManager) tm);

        try {
            tm.inTransaction(() -> {

                var session = ((ISesionManager) tm).getSession();

                var cb = session.getCriteriaBuilder();

                var us = cb.createCriteriaDelete(UsuarioEntidad.class);
                session.createMutationQuery(us).executeUpdate();

                var ju = cb.createCriteriaDelete(JuegoEntidad.class);
                session.createMutationQuery(ju).executeUpdate();

                var co = cb.createCriteriaDelete(CompraEntidad.class);
                session.createMutationQuery(co).executeUpdate();

                var li = cb.createCriteriaDelete(BibliotecaEntidad.class);
                session.createMutationQuery(li).executeUpdate();

                var re = cb.createCriteriaDelete(ResenhaRepoHibernate.class);
                session.createMutationQuery(re).executeUpdate();

                return null;
            });
        } catch (ValidacionException e) {
            throw new RuntimeException("No se han podido borrar los datos de la base de datos", e);
        }

        var userController = new UsuarioControlador(userRepo, tm);
        var gameController = new JuegoControlador(gameRepo, tm);
        var purchaseController = new CompraControlador(purchaseRepo, userRepo, libraryRepo, gameRepo, tm);
        var libraryController = new BibliotecaControlador(libraryRepo, userRepo, gameRepo, purchaseRepo, tm);
        var reviewController = new ResenhaControlador(tm, libraryRepo, userRepo, gameRepo, reviewRepo);

        return new AdapterBundle(
                new UsuarioAdapter(userController),
                new CompraAdapter(purchaseController),
                new JuegoAdapter(gameController),
                new ReseniaAdapter(reviewController),
                new BibliotecaAdapter(libraryController));

    }

    private static AdapterBundle getInMemoryBundle() throws NoSuchFieldException, IllegalAccessException {
        ITransactionManager tm = new NoOpTransactionManager();

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

        var bibliotecaControlador = new BibliotecaControlador(bibliotecaRepo, usuarioRepo, juegoRepo, compraRepo, tm);

        return new AdapterBundle(
                new UsuarioAdapter(new UsuarioControlador(usuarioRepo, tm)),
                new CompraAdapter(new CompraControlador(compraRepo, usuarioRepo, bibliotecaRepo, juegoRepo, tm)),
                new JuegoAdapter(new JuegoControlador(juegoRepo, tm)),
                // ITransactionManager tm, IBibliotecaRepo bibliotecaRepo, IUsuarioRepo
                // usuarioRepo, IJuegoRepo juegoRepo, IResenhaRepo resenhaRepo
                new ReseniaAdapter(new ResenhaControlador(tm, bibliotecaRepo, usuarioRepo, juegoRepo, resenhaRepo)),
                new BibliotecaAdapter(bibliotecaControlador));
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
