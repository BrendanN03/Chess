package org.cis1200.chess;

import java.awt.*;

public class RookPiece implements GamePiece {
    private boolean isWhite;

    public RookPiece(boolean isWhite) {
        this.isWhite = isWhite;
    }
    public String getPiece() {
        return "R";
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

        if (oldC != newC && oldR != newR) {
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

        //rook rules
        if (oldC == newC) {
            if (oldR > newR) {
                for (int i = 1; i < oldR - newR; i++) {
                    if (board[oldR - i][oldC] != null) {
                        return false;
                    }
                }
            } else if (oldR < newR) {
                for (int i = 1; i < newR - oldR; i++) {
                    if (board[oldR + i][oldC] != null) {
                        return false;
                    }
                }
            }
        } else if (oldR == newR) {
            if (oldC > newC) {
                for (int i = 1; i < oldC - newC; i++) {
                    if (board[oldR][oldC - i] != null) {
                        return false;
                    }
                }
            } else if (oldC < newC) {
                for (int i = 1; i < newC - oldC; i++) {
                    if (board[oldR][oldC + i] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void draw(Graphics g, int x, int y) {
        if (isWhite) {
            g.drawRect(x - 10, y - 20, 10, 10);
            g.drawRect(x + 15, y - 20, 10, 10);
            g.drawRect(x + 40, y - 20, 10, 10);
            g.drawRect(x - 10, y - 10, 60, 30);
            g.drawRect(x, y, 40, 60);
        } else {
            g.fillRect(x - 10, y - 20, 10, 10);
            g.fillRect(x + 15, y - 20, 10, 10);
            g.fillRect(x + 40, y - 20, 10, 10);
            g.fillRect(x - 10, y - 10, 60, 30);
            g.fillRect(x, y, 40, 60);
        }
    }

    @Override
    public String toString() {
        return "R";
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
        RookPiece other = (RookPiece) obj;
        return isWhite == other.isWhite() && other.toString().equals("R");
    }
}
