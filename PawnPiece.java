import java.awt.*;
public class PawnPiece implements GamePiece {
    private boolean isWhite;
    private boolean canMoveTwice;

    private static Pos enPassantSquarePos = new Pos(-1, -1);

    public PawnPiece(boolean isWhite) {
        this.isWhite = isWhite;
        canMoveTwice = true;
    }
    public String getPiece() {
        return "P";
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

        if (board[newR][newC] == null) {
            //en passant
            if (enPassantSquarePos.getC() == newC && enPassantSquarePos.getR() == newR) {
                if (isWhite) {
                    return (oldC == newC + 1 || oldC == newC - 1) && oldR == newR + 1;
                } else {
                    return (oldC == newC + 1 || oldC == newC - 1) && oldR == newR - 1;
                }
                //empty open space for pawn with no blocks
            } else if ((oldC == newC) && (Math.abs(oldR - newR) == 1)) {
                if (isWhite) {
                    return oldR == newR + 1;
                } else {
                    return oldR == newR - 1;
                }
                //empty open space for two moves ahead
            } else if (canMoveTwice && (oldC == newC)) {
                if (isWhite) {
                    return (oldR == newR + 2) && (board[newR + 1][newC] == null);
                } else {
                    return (oldR == newR - 2) && (board[newR - 1][newC] == null);
                }
            }
        } else { //new square is not null
            if ((oldC == newC + 1 || oldC == newC - 1)) {
                if (isWhite) {
                    return oldR == newR + 1;
                } else {
                    return oldR == newR - 1;
                }
            }
        }
        return false;
    }
    public void revokeCanMoveTwice() {
        canMoveTwice = false;
    }

    public static void setEnPassantSquarePos(int r, int c) {
        enPassantSquarePos = new Pos(r, c);
    }

    public static Pos getEnPassantSquarePos() {
        return enPassantSquarePos;
    }

    public boolean canMoveTwice() {
        return canMoveTwice;
    }

    public void draw(Graphics g, int x, int y) {
        if (isWhite) {
            g.drawOval(x + 10, y - 10, 20, 20);
            g.drawRect(x + 10, y + 10, 20, 40);
        } else {
            g.fillOval(x + 10, y - 10, 20, 20);
            g.fillRect(x + 10, y + 10, 20, 40);
        }
    }

    @Override
    public String toString() {
        return "P";
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
        PawnPiece other = (PawnPiece) obj;
        return isWhite == other.isWhite() && other.toString().equals("P");
    }
}
