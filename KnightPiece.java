import java.awt.*;
public class KnightPiece implements GamePiece {
    private boolean isWhite;

    public KnightPiece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public String getPiece() {
        return "N";
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isSquareLegal(GamePiece[][] board, Pos oldPos, Pos newPos) {
        return isSquareLegal(board, oldPos, newPos, false);
    }

    public boolean isSquareLegal(GamePiece[][] board, Pos oldPos, Pos newPos, boolean isGhostCall) {
        if (board == null || oldPos == null || newPos == null) {
            return false;
        }

        int oldC = oldPos.getC();
        int oldR = oldPos.getR();
        int newC = newPos.getC();
        int newR = newPos.getR();

        if (board[newR][newC] != null && board[newR][newC].isWhite() == isWhite) {
            return false;
        }

        //cases for if in check or moving puts in check
        if (!isGhostCall) {
            GamePiece temp = board[newR][newC];
            board[newR][newC] = this;
            board[oldR][oldC] = null;
            Pos thisKingPos = KingPiece.getKingPos(isWhite);

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] != null && board[i][j].isWhite() != isWhite) {
                        if (board[i][j].isSquareLegal(board, new Pos(i, j), thisKingPos, true)) {
                            board[oldR][oldC] = this;
                            board[newR][newC] = temp;
                            return false;
                        }
                    }
                }
            }
            board[oldR][oldC] = this;
            board[newR][newC] = temp;
        }

        return (oldC + 2 == newC && (oldR + 1 == newR || oldR - 1 == newR)) ||
                (oldC - 2 == newC && (oldR + 1 == newR || oldR - 1 == newR)) ||
                (oldR + 2 == newR && (oldC - 1 == newC || oldC + 1 == newC)) ||
                (oldR - 2 == newR && (oldC - 1 == newC || oldC + 1 == newC));

    }

    public void draw(Graphics g, int x, int y) {
        if (isWhite) {
            g.drawOval(x + 20, y - 20, 20, 20);
            g.drawRect(x + 20, y - 20, 10, 10);
            g.drawRect(x - 10, y - 10, 40, 30);
            g.drawRect(x, y + 20, 40, 40);
        } else {
            g.fillOval(x + 20, y - 20, 20, 20);
            g.fillRect(x + 20, y - 20, 10, 10);
            g.fillRect(x - 10, y - 10, 40, 30);
            g.fillRect(x, y + 20, 40, 40);
        }
    }

    @Override
    public String toString() {
        return "N";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        KnightPiece other = (KnightPiece) obj;
        return isWhite == other.isWhite() && other.toString().equals("N");
    }
}
