package domino;

public class Ficha {
    private int ladoA;
    private int ladoB;

    public Ficha(int ladoA, int ladoB) {
        this.ladoA = ladoA;
        this.ladoB = ladoB;
    }

    public int getLadoA() { return ladoA; }
    public int getLadoB() { return ladoB; }

    public void voltear() {
        int temp = ladoA;
        ladoA = ladoB;
        ladoB = temp;
    }

    public boolean encajaCon(int numero) {
        return ladoA == numero || ladoB == numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ficha ficha = (Ficha) o;
        return (ladoA == ficha.ladoA && ladoB == ficha.ladoB) || (ladoA == ficha.ladoB && ladoB == ficha.ladoA);
    }

    @Override
    public int hashCode() {
        return ladoA + ladoB;
    }
}
