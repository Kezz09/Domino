package domino;

public class Main {
    public static void main(String[] args) {
        Juego juego = new Juego();
        Grafico gui = new Grafico(juego);
        gui.setVisible(true);
    }
}
