package org.cis1200.chess;

public class Pos {
    private int r;
    private int c;
    public Pos(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public int getR() {
        return r;
    }
    public int getC() {
        return c;
    }

    public boolean myEquals(Pos other) {
        if (other == null) {
            return false;
        }
        return this.c == other.getC() && this.r == other.getR();
    }

    @Override
    public Pos clone() {
        return new Pos(this.r, this.c);
    }

}
