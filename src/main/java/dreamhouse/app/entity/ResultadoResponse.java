package dreamhouse.app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoResponse {
    private String titulo;
    private String respuesta;
    private String mensaje;

    private String icon;

    public ResultadoResponse(String respuesta, String mensaje) {
        this.respuesta = respuesta;
        this.mensaje = mensaje;
    }
}
