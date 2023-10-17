import java.awt.*;

public class KingPiece implements GamePiece {
    private boolean isWhite;

    private boolean canCastleShort;
    private boolean canCastleLong;
    private static Pos whiteKingPos = new Pos(7, 4);
    private static Pos blackKingPos = new Pos(0, 4);
    public KingPiece(boolean isWhite) {
        this.isWhite = isWhite;
        canCastleShort = true;
        canCastleLong = true;
    }
    public String getPiece() {
        return "K";
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

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] != null && board[i][j].isWhite() != isWhite) {
                        if (board[i][j].isSquareLegal(board, new Pos(i, j),
                                new Pos(newR, newC), true)) {
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

        if (oldC == newC && oldR == newR) {
            return false;
        }

        if ((oldC + 1 == newC || oldC == newC || oldC - 1 == newC)
                && (oldR + 1 == newR || oldR == newR || oldR - 1 == newR)) {
            return true;
        }

        if (canCastleShort) {
            //need open spaces, and no check in those spaces
            if (isWhite && newR == 7 && newC == 6) {
                if (board[7][5] == null && board[7][6] == null) {
                    if (this.isSquareLegal(board, whiteKingPos, new Pos(7, 5), false)) {
                        //check if in check currently
                        board[7][5] = board[7][4];
                        board[7][4] = null;
                        if (this.isSquareLegal(board, new Pos(7, 5), new Pos(7, 4))) {
                            board[7][4] = board[7][5];
                            board[7][5] = null;
                            return true;
                        }
                        board[7][4] = board[7][5];
                        board[7][5] = null;
                    }
                }
            } else if (!isWhite && newR == 0 && newC == 6) {
                if (board[0][5] == null && board[0][6] == null) {
                    if (this.isSquareLegal(board, blackKingPos, new Pos(0, 5), false)) {
                        //check if in check currently
                        board[0][5] = board[0][4];
                        board[0][4] = null;
                        if (this.isSquareLegal(board, new Pos(0, 5), new Pos(0, 4))) {
                            board[0][4] = new KingPiece(false);
                            board[0][5] = null;
                            return true;
                        }
                        board[0][4] = board[0][5];
                        board[0][5] = null;
                    }
                }
            }
        }

        if (canCastleLong) {
            if (isWhite && newR == 7 && newC == 2) {
                if (board[7][1] == null && board[7][2] == null && board[7][3] == null) {
                    if (this.isSquareLegal(board, whiteKingPos, new Pos(7, 3), false)) {
                        //check if in check currently
                        board[7][3] = board[7][4];
                        board[7][4] = null;
                        if (this.isSquareLegal(board, new Pos(7, 3), new Pos(7, 4))) {
                            board[7][4] = board[7][3];
                            board[7][3] = null;
                            return true;
                        }
                        board[7][4] = board[7][3];
                        board[7][3] = null;
                    }
                }
            } else if (!isWhite && newR == 0 && newC == 2) {
                if (board[0][1] == null && board[0][2] == null && board[0][3] == null) {
                    if (this.isSquareLegal(board, blackKingPos, new Pos(0, 3), false)) {
                        //check if in check currently
                        board[0][3] = board[0][4];
                        board[0][4] = null;
                        if (this.isSquareLegal(board, new Pos(0, 3), new Pos(0, 4))) {
                            board[0][4] = board[0][3];
                            board[0][3] = null;
                            return true;
                        }
                        board[0][4] = board[0][3];
                        board[0][3] = null;
                    }
                }
            }
        }

        return false;
    }

    public static void setKingPos(int r, int c, boolean isWhite) {
        if (isWhite) {
            whiteKingPos = new Pos(r, c);
        } else {
            blackKingPos = new Pos(r, c);
        }
    }

    public static Pos getKingPos(boolean isWhite) {
        if (isWhite) {
            return whiteKingPos;
        } else {
            return blackKingPos;
        }
    }

    public void revokeCanCastleShort() {
        canCastleShort = false;
    }

    public void revokeCanCastleLong() {
        canCastleLong = false;
    }

    public void draw(Graphics g, int x, int y) {
        if (isWhite) {
            g.drawRect(x + 10, y - 15, 20, 5);
            g.drawRect(x + 17, y - 20, 5, 20);

            g.drawRect(x + 5, y, 30, 20);
            g.drawRect(x, y + 20, 40, 40);
        } else {
            g.fillRect(x + 10, y - 15, 20, 5);
            g.fillRect(x + 17, y - 20, 5, 20);

            g.fillRect(x + 5, y, 30, 20);
            g.fillRect(x, y + 20, 40, 40);
        }
    }

    @Override
    public String toString() {
        return "K";
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
        KingPiece other = (KingPiece) obj;
        return isWhite == other.isWhite() && other.toString().equals("K");
    }
}
