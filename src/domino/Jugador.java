// Jugador.java
package domino;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private List<Ficha> fichas;

    public Jugador() {
        fichas = new ArrayList<>();
    }

    public void agregarFicha(Ficha ficha) {
        fichas.add(ficha);
    }

    public void removerFicha(Ficha ficha) {
        fichas.remove(ficha);
    }

    public List<Ficha> getFichas() {
        return fichas;
    }

    public boolean puedeJugar(int extremoIzquierdo, int extremoDerecho) {
        for (Ficha ficha : fichas) {
            if (ficha.encajaCon(extremoIzquierdo) || ficha.encajaCon(extremoDerecho)) {
                return true;
            }
        }
        return false;
    }
}
