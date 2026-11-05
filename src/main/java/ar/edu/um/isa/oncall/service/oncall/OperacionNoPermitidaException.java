package ar.edu.um.isa.oncall.service.oncall;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Pedido válido en forma pero que la regla no admite (por ejemplo, abrir un ticket a un proveedor inactivo). */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OperacionNoPermitidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OperacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
