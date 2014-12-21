package vista.ayuda;

public class Contenido {
    private String id;
    private String texto;
    public Contenido(String id, String texto) {
        super();
        this.id=id;
        this.texto=texto;
    }

    

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
