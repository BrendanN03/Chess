import java.awt.*;

public class QueenPiece implements GamePiece {
    private boolean isWhite;

    public QueenPiece(boolean isWhite) {
        this.isWhite = isWhite;
    }
    public String getPiece() {
        return "Q";
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
            return true;
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
            return true;
        }

        //bishop rules
        boolean possiblyValid = false;
        int distance = -1;
        String direction = "";
        for (int i = 1; i < 8; i++) {
            if (oldC + i == newC && oldR + i == newR) {
                possiblyValid = true;
                distance = i;
                direction = "SE";
                break;
            } else if (oldC - i == newC && oldR - i == newR) {
                possiblyValid = true;
                distance = i;
                direction = "NW";
                break;
            } else if (oldC + i == newC && oldR - i == newR) {
                possiblyValid = true;
                distance = i;
                direction = "NE";
                break;
            } else if (oldC - i == newC && oldR + i == newR) {
                possiblyValid = true;
                distance = i;
                direction = "SW";
                break;
            }
        }

        if (!possiblyValid) {
            return false;
        }

        //bishop blocking rules
        if (direction.equals("SE")) {
            for (int i = 1; i < distance; i++) {
                if (board[oldR + i][oldC + i] != null) {
                    return false;
                }
            }
        } else if (direction.equals("NW")) {
            for (int i = 1; i < distance; i++) {
                if (board[oldR - i][oldC - i] != null) {
                    return false;
                }
            }
        } else if (direction.equals("NE")) {
            for (int i = 1; i < distance; i++) {
                if (board[oldR - i][oldC + i] != null) {
                    return false;
                }
            }
        } else if (direction.equals("SW")) {
            for (int i = 1; i < distance; i++) {
                if (board[oldR + i][oldC - i] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void draw(Graphics g, int x, int y) {
        if (isWhite) {
            g.drawRect(x, y - 20, 40, 5);
            g.drawRect(x + 10, y - 15, 20, 10);
            g.drawRect(x, y - 5, 40, 5);

            g.drawRect(x + 5, y, 30, 20);
            g.drawRect(x, y + 20, 40, 40);
        } else {
            g.fillRect(x, y - 20, 40, 5);
            g.fillRect(x + 10, y - 15, 20, 10);
            g.fillRect(x, y - 5, 40, 5);

            g.fillRect(x + 5, y, 30, 20);
            g.fillRect(x, y + 20, 40, 40);
        }
    }

    @Override
    public String toString() {
        return "Q";
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
        QueenPiece other = (QueenPiece) obj;
        return isWhite == other.isWhite() && other.toString().equals("Q");
    }
}
