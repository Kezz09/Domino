package domino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Juego {
    private List<Ficha> fichasJugador;
    private List<Ficha> fichasComputadora;
    private List<Ficha> mesa;
    private List<Ficha> pozo;
    private boolean turnoJugador;

    public Juego() {
        List<Ficha> todas = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                todas.add(new Ficha(i, j));
            }
        }
        Collections.shuffle(todas);

        fichasJugador = new ArrayList<>(todas.subList(0, 7));
        fichasComputadora = new ArrayList<>(todas.subList(7, 14));
        mesa = new ArrayList<>();
        pozo = new ArrayList<>(todas.subList(14, todas.size()));
        turnoJugador = true;

        Ficha ficha66 = null;
        for (Ficha f : fichasJugador) {
            if (f.getLadoA() == 6 && f.getLadoB() == 6) {
                ficha66 = f;
                fichasJugador.remove(f);
                break;
            }
        }
        if (ficha66 == null) {
            for (Ficha f : fichasComputadora) {
                if (f.getLadoA() == 6 && f.getLadoB() == 6) {
                    ficha66 = f;
                    fichasComputadora.remove(f);
                    break;
                }
            }
        }
        if (ficha66 == null) {
            for (Ficha f : pozo) {
                if (f.getLadoA() == 6 && f.getLadoB() == 6) {
                    ficha66 = f;
                    pozo.remove(f);
                    break;
                }
            }
        }
        if (ficha66 != null) {
            mesa.add(ficha66);
        }
    }

    public List<Ficha> getFichasJugador() { return fichasJugador; }
    public List<Ficha> getFichasComputadora() { return fichasComputadora; }
    public List<Ficha> getMesa() { return mesa; }
    public List<Ficha> getPozo() { return pozo; }

    public boolean jugarFichaJugador(Ficha ficha) {
        return jugarFichaJugador(ficha, false);
    }

    public boolean jugarFichaJugador(Ficha ficha, boolean alInicio) {
        if (mesa.isEmpty()) {
            mesa.add(ficha);
            fichasJugador.remove(ficha);
            turnoJugador = false;
            return true;
        }
        int izquierda = mesa.get(0).getLadoA();
        int derecha = mesa.get(mesa.size() - 1).getLadoB();

        if (ficha.getLadoB() == izquierda) {
            mesa.add(0, ficha);
            fichasJugador.remove(ficha);
            turnoJugador = false;
            return true;
        } else if (ficha.getLadoA() == izquierda) {
            ficha.voltear();
            mesa.add(0, ficha);
            fichasJugador.remove(ficha);
            turnoJugador = false;
            return true;
        } else if (ficha.getLadoA() == derecha) {
            mesa.add(ficha);
            fichasJugador.remove(ficha);
            turnoJugador = false;
            return true;
        } else if (ficha.getLadoB() == derecha) {
            ficha.voltear();
            mesa.add(ficha);
            fichasJugador.remove(ficha);
            turnoJugador = false;
            return true;
        }
        return false;
    }


    public boolean jugarTurnoComputadora() {
        boolean jugo = false;
        while (true) {
            if (mesa.isEmpty()) {
                if (!fichasComputadora.isEmpty()) {
                    Ficha ficha = fichasComputadora.get(0);
                    mesa.add(ficha);
                    fichasComputadora.remove(0);
                    jugo = true;
                }
                break;
            } else {
                int izquierda = mesa.get(0).getLadoA();
                int derecha = mesa.get(mesa.size() - 1).getLadoB();
                boolean pudo = false;
                for (int i = 0; i < fichasComputadora.size(); i++) {
                    Ficha ficha = fichasComputadora.get(i);
                    if (ficha.getLadoB() == izquierda) {
                        mesa.add(0, ficha);
                        fichasComputadora.remove(i);
                        jugo = true;
                        pudo = true;
                        break;
                    } else if (ficha.getLadoA() == izquierda) {
                        ficha.voltear();
                        mesa.add(0, ficha);
                        fichasComputadora.remove(i);
                        jugo = true;
                        pudo = true;
                        break;
                    } else if (ficha.getLadoA() == derecha) {
                        mesa.add(ficha);
                        fichasComputadora.remove(i);
                        jugo = true;
                        pudo = true;
                        break;
                    } else if (ficha.getLadoB() == derecha) {
                        ficha.voltear();
                        mesa.add(ficha);
                        fichasComputadora.remove(i);
                        jugo = true;
                        pudo = true;
                        break;
                    }
                }
                if (!pudo) {
                    if (robarFicha(fichasComputadora)) {
                        continue;
                    }
                }
                break;
            }
        }
        turnoJugador = true;
        return jugo;
    }

    // NUEVO: encuentra la ficha que la computadora jugará (para animarla)
    public Ficha buscarFichaParaComputadora() {
        if (mesa.isEmpty()) return fichasComputadora.isEmpty() ? null : fichasComputadora.get(0);
        int izquierda = mesa.get(0).getLadoA();
        int derecha = mesa.get(mesa.size() - 1).getLadoB();
        for (Ficha ficha : fichasComputadora) {
            if (ficha.getLadoB() == izquierda || ficha.getLadoA() == izquierda
                    || ficha.getLadoA() == derecha || ficha.getLadoB() == derecha) {
                return ficha;
            }
        }
        return null;
    }

    public boolean puedeJugar(List<Ficha> fichas) {
        if (mesa.isEmpty()) return true;
        int izquierda = mesa.get(0).getLadoA();
        int derecha = mesa.get(mesa.size() - 1).getLadoB();

        for (Ficha ficha : fichas) {
            int l1 = ficha.getLadoA();
            int l2 = ficha.getLadoB();
            if (l1 == izquierda || l2 == izquierda || l1 == derecha || l2 == derecha) {
                return true;
            }
        }
        return false;
    }

    public boolean robarDelPozo() { return robarFicha(fichasJugador); }
    public boolean robarFicha(List<Ficha> destino) {
        if (!pozo.isEmpty()) {
            destino.add(pozo.remove(0));
            return true;
        }
        return false;
    }
    public boolean esTurnoJugador() { return turnoJugador; }

    public String chequearGanador() {
        if (fichasJugador.isEmpty()) {
            return "¡Felicidades! Has ganado, te quedaste sin fichas.:D";
        }
        if (fichasComputadora.isEmpty()) {
            return "¡Has sido derrotado! La computadora se quedó sin fichas.";
        }
        return null;
    }
}
