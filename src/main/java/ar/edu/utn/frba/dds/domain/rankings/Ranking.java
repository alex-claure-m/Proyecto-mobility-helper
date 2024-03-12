package ar.edu.utn.frba.dds.domain.rankings;

import java.util.List;

// Ranking: modela un ranking genérico, y establece los métodos comunes a todos
public interface Ranking {
    public abstract String tituloDelRanking();
    public abstract List<Registro<String, String>> datosComoString();
}