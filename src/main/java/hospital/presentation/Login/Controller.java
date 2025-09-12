package hospital.presentation.Login;

import hospital.logic.*;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void login(String id, String clave) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("Id requerido");
        }
        if (clave == null || clave.trim().isEmpty()) {
            throw new Exception("Clave requerida");
        }

        Usuario usuario = Service.instance().authenticate(id, clave);
        Sesion.setUsuario(usuario);
        model.setCurrent(usuario);
    }

    public void logout() {
        Sesion.logout();
        model.setCurrent(null);
    }

    public void cambiarClave(String id, String claveActual, String claveNueva) throws Exception {
        Service.instance().cambiarClave(id, claveActual, claveNueva);

        if (Sesion.getUsuario() != null && Sesion.getUsuario().getId().equals(id)) {
            Usuario usuarioActualizado = Service.instance().authenticate(id, claveNueva);
            Sesion.setUsuario(usuarioActualizado);
            model.setCurrent(usuarioActualizado);
        }
    }
}