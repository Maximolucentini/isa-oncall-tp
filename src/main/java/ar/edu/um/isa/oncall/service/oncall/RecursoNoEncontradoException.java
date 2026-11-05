package ar.edu.um.isa.oncall.service.oncall;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** El ExceptionTranslator de JHipster respeta @ResponseStatus y la traduce a un 404. */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNoEncontradoException(String entidad, Long id) {
        super(entidad + " " + id + " no existe");
    }
}
