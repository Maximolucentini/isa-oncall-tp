package ar.edu.um.isa.oncall.service.oncall;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.domain.ContactoDeProveedor;
import ar.edu.um.isa.oncall.domain.Proveedor;
import ar.edu.um.isa.oncall.domain.TurnoDeProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.CoberturaProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import ar.edu.um.isa.oncall.domain.enumeration.Severidad;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia.EstadoGuardia;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia.MotivoSinCobertura;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Criterios de aceptación de docs/especificaciones/003-guardia-proveedor.md (sección 5) y casos borde (sección 4).
 * Test unitario puro: sin Spring, sin base, con instantes fijos.
 */
class ResolutorDeGuardiaTest {

    private final ResolutorDeGuardia resolutor = new ResolutorDeGuardia();

    private Proveedor proveedor;
    private ContactoDeProveedor ana;
    private ContactoDeProveedor beto;
    private final List<TurnoDeProveedor> turnos = new ArrayList<>();
    private long proximoIdTurno = 1;

    @BeforeEach
    void escenario() {
        proveedor = new Proveedor()
            .id(1L)
            .nombre("P")
            .zonaHoraria("America/New_York")
            .cobertura(CoberturaProveedor.VEINTICUATRO_SIETE)
            .telefonoContacto("+1 555 0100")
            .emailContacto("soporte@p.example")
            .urlSoporte("https://p.example/soporte")
            .activo(true);
        ana = new ContactoDeProveedor().id(10L).nombre("Ana").email("ana@p.example").activo(true).proveedor(proveedor);
        beto = new ContactoDeProveedor().id(11L).nombre("Beto").email("beto@p.example").activo(true).proveedor(proveedor);
    }

    // ---------------------------------------------------------------- helpers

    private TurnoDeProveedor turno(ContactoDeProveedor c, NivelGuardia nivel, String desde, String hasta, boolean reemplazo) {
        TurnoDeProveedor t = new TurnoDeProveedor()
            .id(proximoIdTurno++)
            .contacto(c)
            .nivel(nivel)
            .desde(Instant.parse(desde))
            .hasta(Instant.parse(hasta))
            .esReemplazo(reemplazo);
        turnos.add(t);
        return t;
    }

    private TurnoDeProveedor regular(ContactoDeProveedor c, NivelGuardia nivel, String desde, String hasta) {
        return turno(c, nivel, desde, hasta, false);
    }

    private ResultadoGuardia en(String instante) {
        return en(instante, null, null);
    }

    private ResultadoGuardia en(String instante, NivelGuardia nivel, Severidad severidad) {
        return resolutor.resolver(proveedor, turnos, Instant.parse(instante), nivel, severidad);
    }

    // ---------------------------------------------------------------- criterios de aceptación

    @Test
    @DisplayName("CA1 · turno simple: el contacto de guardia es el responsable primario")
    void ca1_turnoSimple() {
        regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T12:00:00Z", "2026-11-05T20:00:00Z");

        ResultadoGuardia r = en("2026-11-05T14:00:00Z");

        assertThat(r.estado()).isEqualTo(EstadoGuardia.CUBIERTO);
        assertThat(r.responsable().nombre()).isEqualTo("Ana");
        assertThat(r.nivelResponsable()).isEqualTo(NivelGuardia.PRIMARIO);
        assertThat(r.motivo()).isNull();
    }

    @Test
    @DisplayName("CA2 · hueco: SIN_COBERTURA con el contacto general, no una excepción")
    void ca2_hueco() {
        regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T12:00:00Z", "2026-11-05T20:00:00Z");

        ResultadoGuardia r = en("2026-11-05T21:00:00Z");

        assertThat(r.estado()).isEqualTo(EstadoGuardia.SIN_COBERTURA);
        assertThat(r.motivo()).isEqualTo(MotivoSinCobertura.SIN_TURNOS);
        assertThat(r.responsable()).isNull();
        assertThat(r.contactoGeneral().telefono()).isEqualTo("+1 555 0100");
        assertThat(r.contactoGeneral().urlSoporte()).isEqualTo("https://p.example/soporte");
    }

    @Test
    @DisplayName("CA3 · superposición: gana el turno que empezó más tarde y se informa cuántos había")
    void ca3_superposicion() {
        regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T00:00:00Z", "2026-11-05T09:00:00Z");
        regular(beto, NivelGuardia.PRIMARIO, "2026-11-05T08:30:00Z", "2026-11-05T17:00:00Z");

        ResultadoGuardia r = en("2026-11-05T08:45:00Z");

        assertThat(r.responsable().nombre()).isEqualTo("Beto");
        assertThat(r.turnosVigentesPrimario()).isEqualTo(2);
    }

    @Test
    @DisplayName("CA4 · traspaso exacto: en t == hasta de uno y desde del otro, solo el entrante")
    void ca4_traspasoExacto() {
        regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T00:00:00Z", "2026-11-05T09:00:00Z");
        regular(beto, NivelGuardia.PRIMARIO, "2026-11-05T09:00:00Z", "2026-11-05T17:00:00Z");

        ResultadoGuardia r = en("2026-11-05T09:00:00Z");

        assertThat(r.responsable().nombre()).isEqualTo("Beto");
        assertThat(r.turnosVigentesPrimario()).isEqualTo(1);
    }

    @Test
    @DisplayName("CA5 · reemplazo: pisa al turno regular mientras dura, después vuelve el regular")
    void ca5_reemplazo() {
        regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T00:00:00Z", "2026-11-05T23:00:00Z");
        turno(beto, NivelGuardia.PRIMARIO, "2026-11-05T02:00:00Z", "2026-11-05T04:00:00Z", true);

        ResultadoGuardia durante = en("2026-11-05T03:00:00Z");
        ResultadoGuardia despues = en("2026-11-05T05:00:00Z");

        assertThat(durante.responsable().nombre()).isEqualTo("Beto");
        assertThat(durante.responsable().esReemplazo()).isTrue();
        assertThat(despues.responsable().nombre()).isEqualTo("Ana");
    }

    @Test
    @DisplayName("CA5b · el reemplazo gana aunque haya empezado antes que el regular")
    void ca5b_reemplazoQueEmpezoAntes() {
        turno(beto, NivelGuardia.PRIMARIO, "2026-11-05T01:00:00Z", "2026-11-05T06:00:00Z", true);
        regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T02:00:00Z", "2026-11-05T23:00:00Z");

        assertThat(en("2026-11-05T03:00:00Z").responsable().nombre()).isEqualTo("Beto");
    }

    @Test
    @DisplayName("CA6 · primario y secundario: responsable el primario, la respuesta trae los dos")
    void ca6_primarioYSecundario() {
        regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T00:00:00Z", "2026-11-06T00:00:00Z");
        regular(beto, NivelGuardia.SECUNDARIO, "2026-11-05T00:00:00Z", "2026-11-06T00:00:00Z");

        ResultadoGuardia sinNivel = en("2026-11-05T12:00:00Z");
        ResultadoGuardia soloSecundario = en("2026-11-05T12:00:00Z", NivelGuardia.SECUNDARIO, null);

        assertThat(sinNivel.responsable().nombre()).isEqualTo("Ana");
        assertThat(sinNivel.primario().nombre()).isEqualTo("Ana");
        assertThat(sinNivel.secundario().nombre()).isEqualTo("Beto");
        assertThat(soloSecundario.responsable().nombre()).isEqualTo("Beto");
        assertThat(soloSecundario.nivelResponsable()).isEqualTo(NivelGuardia.SECUNDARIO);
    }

    @Test
    @DisplayName("CA7 · solo secundario: cubre si no se pide nivel; si se pide PRIMARIO, no se cae al otro")
    void ca7_soloSecundario() {
        regular(beto, NivelGuardia.SECUNDARIO, "2026-11-05T00:00:00Z", "2026-11-06T00:00:00Z");

        ResultadoGuardia sinNivel = en("2026-11-05T12:00:00Z");
        ResultadoGuardia pidePrimario = en("2026-11-05T12:00:00Z", NivelGuardia.PRIMARIO, null);

        assertThat(sinNivel.estado()).isEqualTo(EstadoGuardia.CUBIERTO);
        assertThat(sinNivel.responsable().nombre()).isEqualTo("Beto");
        assertThat(sinNivel.nivelResponsable()).isEqualTo(NivelGuardia.SECUNDARIO);
        assertThat(pidePrimario.estado()).isEqualTo(EstadoGuardia.SIN_COBERTURA);
        assertThat(pidePrimario.secundario().nombre()).isEqualTo("Beto");
    }

    @Test
    @DisplayName("CA8 · horario de verano: la cobertura hábil se evalúa en la hora local real de la fecha")
    void ca8_horarioDeVerano() {
        proveedor.cobertura(CoberturaProveedor.HORARIO_HABIL);

        // EE. UU. pasa a horario de verano el domingo 8/3/2026.
        ResultadoGuardia despuesDelCambio = en("2026-03-09T13:30:00Z"); // lunes 09:30 EDT (UTC-4)
        ResultadoGuardia antesDelCambio = en("2026-03-02T13:30:00Z"); // lunes 08:30 EST (UTC-5)

        assertThat(despuesDelCambio.dentroDeCoberturaContractual()).isTrue();
        assertThat(despuesDelCambio.instanteLocal()).isEqualTo("2026-03-09T09:30-04:00");
        assertThat(antesDelCambio.dentroDeCoberturaContractual()).isFalse();
        assertThat(antesDelCambio.instanteLocal()).isEqualTo("2026-03-02T08:30-05:00");
    }

    @Test
    @DisplayName("CA8b · el horario de verano no altera qué turno está vigente (se compara en UTC)")
    void ca8b_turnoQueCruzaElCambioDeHorario() {
        // De sábado 7/3 a lunes 9/3 en UTC: cruza el cambio de horario de Nueva York.
        regular(ana, NivelGuardia.PRIMARIO, "2026-03-07T12:00:00Z", "2026-03-09T12:00:00Z");

        assertThat(en("2026-03-08T06:59:59Z").responsable().nombre()).isEqualTo("Ana"); // 01:59:59 EST
        assertThat(en("2026-03-08T07:00:00Z").responsable().nombre()).isEqualTo("Ana"); // 03:00:00 EDT
        assertThat(en("2026-03-09T12:00:00Z").estado()).isEqualTo(EstadoGuardia.SIN_COBERTURA);
    }

    @Test
    @DisplayName("CA9 · contacto inactivo: su turno se ignora")
    void ca9_contactoInactivo() {
        ana.activo(false);
        regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T00:00:00Z", "2026-11-06T00:00:00Z");

        ResultadoGuardia r = en("2026-11-05T12:00:00Z");

        assertThat(r.estado()).isEqualTo(EstadoGuardia.SIN_COBERTURA);
        assertThat(r.turnosVigentesPrimario()).isZero();
    }

    // ---------------------------------------------------------------- casos borde (sección 4)

    @Nested
    @DisplayName("Casos borde")
    class CasosBorde {

        @Test
        @DisplayName("#2 · empate en desde: gana el último cargado (id mayor)")
        void empateEnDesde() {
            regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T08:00:00Z", "2026-11-05T12:00:00Z");
            regular(beto, NivelGuardia.PRIMARIO, "2026-11-05T08:00:00Z", "2026-11-05T12:00:00Z");

            assertThat(en("2026-11-05T09:00:00Z").responsable().nombre()).isEqualTo("Beto");
        }

        @Test
        @DisplayName("#8 · proveedor inactivo: SIN_COBERTURA con motivo PROVEEDOR_INACTIVO")
        void proveedorInactivo() {
            proveedor.activo(false);
            regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T00:00:00Z", "2026-11-06T00:00:00Z");

            ResultadoGuardia r = en("2026-11-05T12:00:00Z");

            assertThat(r.estado()).isEqualTo(EstadoGuardia.SIN_COBERTURA);
            assertThat(r.motivo()).isEqualTo(MotivoSinCobertura.PROVEEDOR_INACTIVO);
            assertThat(r.contactoGeneral().email()).isEqualTo("soporte@p.example");
        }

        @Test
        @DisplayName("#9 · turno invertido (hasta <= desde): se ignora")
        void turnoInvertido() {
            regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T18:00:00Z", "2026-11-05T06:00:00Z");

            assertThat(en("2026-11-05T12:00:00Z").estado()).isEqualTo(EstadoGuardia.SIN_COBERTURA);
        }

        @Test
        @DisplayName("#10 · HORARIO_HABIL fuera de horario con turno cargado: manda el turno, se avisa con la bandera")
        void turnoFueraDeHorarioContractual() {
            proveedor.cobertura(CoberturaProveedor.HORARIO_HABIL);
            regular(ana, NivelGuardia.PRIMARIO, "2026-11-07T00:00:00Z", "2026-11-08T00:00:00Z"); // sábado

            ResultadoGuardia r = en("2026-11-07T15:00:00Z");

            assertThat(r.estado()).isEqualTo(EstadoGuardia.CUBIERTO);
            assertThat(r.responsable().nombre()).isEqualTo("Ana");
            assertThat(r.dentroDeCoberturaContractual()).isFalse();
        }

        @Test
        @DisplayName("#10b · horario hábil es [09:00, 18:00): las 18:00 en punto ya quedan afuera")
        void bordesDelHorarioHabil() {
            proveedor.cobertura(CoberturaProveedor.HORARIO_HABIL);

            assertThat(en("2026-11-05T14:00:00Z").dentroDeCoberturaContractual()).isTrue(); // jueves 09:00 EST
            assertThat(en("2026-11-05T22:59:59Z").dentroDeCoberturaContractual()).isTrue(); // 17:59:59 EST
            assertThat(en("2026-11-05T23:00:00Z").dentroDeCoberturaContractual()).isFalse(); // 18:00 EST
        }

        @Test
        @DisplayName("#11 · SOLO_CRITICO: dentro de contrato solo con severidad SEV1")
        void soloCritico() {
            proveedor.cobertura(CoberturaProveedor.SOLO_CRITICO);

            assertThat(en("2026-11-05T12:00:00Z", null, Severidad.SEV1).dentroDeCoberturaContractual()).isTrue();
            assertThat(en("2026-11-05T12:00:00Z", null, Severidad.SEV2).dentroDeCoberturaContractual()).isFalse();
            assertThat(en("2026-11-05T12:00:00Z", null, null).dentroDeCoberturaContractual()).isFalse();
        }

        @Test
        @DisplayName("#13 · zona horaria inválida: se usa UTC sin fallar")
        void zonaInvalida() {
            proveedor.zonaHoraria("Marte/Olympus_Mons");
            regular(ana, NivelGuardia.PRIMARIO, "2026-11-05T00:00:00Z", "2026-11-06T00:00:00Z");

            ResultadoGuardia r = en("2026-11-05T12:00:00Z");

            assertThat(r.zonaHorariaUsada()).isEqualTo("UTC");
            assertThat(r.responsable().nombre()).isEqualTo("Ana");
        }

        @Test
        @DisplayName("Un turno de otro proveedor nunca se cuenta")
        void turnoDeOtroProveedor() {
            Proveedor otro = new Proveedor().id(2L).activo(true);
            ContactoDeProveedor carla = new ContactoDeProveedor().id(20L).nombre("Carla").activo(true).proveedor(otro);
            regular(carla, NivelGuardia.PRIMARIO, "2026-11-05T00:00:00Z", "2026-11-06T00:00:00Z");

            assertThat(en("2026-11-05T12:00:00Z").estado()).isEqualTo(EstadoGuardia.SIN_COBERTURA);
        }
    }
}
