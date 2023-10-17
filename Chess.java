/**
 * This class is a model for Chess
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games.
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * Run this file to see the main method play a game of Chess,
 * visualized with Strings printed to the console.
 */
public class Chess {

    private GamePiece[][] board;
    private int numTurns;
    private boolean player1;

    private boolean pieceClicked;
    private GamePiece currentPiece;

    private Pos currentPiecePos;


    /**
     * Constructor sets up game state.
     */
    public Chess() {
        reset();
    }

    /**
     * playTurn allows players to play a turn. If the turn is successful and
     * the game has not ended, the player is changed. If the turn is
     * unsuccessful or the game has ended, the player is not changed.
     *
     * @param c column to play in
     * @param r row to play in
     */
    public void playTurn(int c, int r) {
        GamePiece currentSquare = board[r][c];
        //valid square that is not one of the users own pieces
        if (currentSquare == null || currentSquare.isWhite() != player1) {
            //valid square to place selected piece
            if (pieceClicked &&
                    currentPiece.isSquareLegal(getBoard(), currentPiecePos, new Pos(r, c))) {
                board[r][c] = currentPiece;
                board[currentPiecePos.getR()][currentPiecePos.getC()] = null;
                pieceClicked = false;
                player1 = !player1;
                numTurns++;
                if (currentPiece.getPiece().equals("P")) {
                    specialPawnRules(r, c);
                } else {
                    PawnPiece.setEnPassantSquarePos(-1, -1);
                }
                if (currentPiece.getPiece().equals("K")) {
                    specialKingRules(r, c, !player1);
                } else if (currentPiece.getPiece().equals("R")) {
                    specialRookRules(currentPiecePos);
                }
                currentPiece = null;
            } else {
                //invalid square resets selected piece
                currentPiece = null;
                currentPiecePos = new Pos(-1, -1);
                pieceClicked = false;
            }
        } else { //clicks piece of current color, so the piece becomes selected
            currentPiece = currentSquare;
            currentPiecePos = new Pos(r, c);
            pieceClicked = true;
        }
    }

    public void specialPawnRules(int r, int c) {
        ((PawnPiece) currentPiece).revokeCanMoveTwice();
        if (currentPiece.isWhite()) {
            if (currentPiecePos.getR() == r + 2) { //move twice
                PawnPiece.setEnPassantSquarePos(r + 1, c);
            } else if (r == 0) { //promote to queen
                board[r][c] = new QueenPiece(true);
                PawnPiece.setEnPassantSquarePos(-1, -1);
            } else if (PawnPiece.getEnPassantSquarePos().myEquals(new Pos(r, c))) { //en passant
                board[r + 1][c] = null;
                PawnPiece.setEnPassantSquarePos(-1, -1);
            } else {
                PawnPiece.setEnPassantSquarePos(-1, -1);
            }
        } else {
            if (currentPiecePos.getR() == r - 2) { //move twice
                PawnPiece.setEnPassantSquarePos(r - 1, c);
            } else if (r == 0) { //promote to queen
                board[r][c] = new QueenPiece(false);
                PawnPiece.setEnPassantSquarePos(-1, -1);
            } else if (PawnPiece.getEnPassantSquarePos().myEquals(new Pos(r, c))) { //en passant
                board[r - 1][c] = null;
                PawnPiece.setEnPassantSquarePos(-1, -1);
            } else {
                PawnPiece.setEnPassantSquarePos(-1, -1);
            }
        }
    }

    public void specialKingRules(int r, int c, boolean isWhite) {
        Pos oldKingPos = KingPiece.getKingPos(isWhite);
        if (oldKingPos.getR() == r && oldKingPos.getC() + 2 == c) { //castled
            board[r][c - 1] = new RookPiece(isWhite);
            board[r][c + 1] = null;
        } else if (oldKingPos.getR() == r && oldKingPos.getC() - 2 == c) { //castled
            board[r][c + 1] = new RookPiece(isWhite);
            board[r][c - 2] = null;
        }
        KingPiece.setKingPos(r, c, isWhite);
        ((KingPiece) currentPiece).revokeCanCastleShort();
        ((KingPiece) currentPiece).revokeCanCastleLong();
    }

    public void specialRookRules(Pos oldRookPos) {
        if (oldRookPos.myEquals(new Pos(0, 0))) {
            Pos bKingPos = KingPiece.getKingPos(false);
            ((KingPiece) board[bKingPos.getR()][bKingPos.getC()]).revokeCanCastleLong();
        } else if (oldRookPos.myEquals(new Pos(0, 7))) {
            Pos bKingPos = KingPiece.getKingPos(false);
            ((KingPiece) board[bKingPos.getR()][bKingPos.getC()]).revokeCanCastleShort();
        } else if (oldRookPos.myEquals(new Pos(7, 0))) {
            Pos wKingPos = KingPiece.getKingPos(true);
            ((KingPiece) board[wKingPos.getR()][wKingPos.getC()]).revokeCanCastleLong();
        } else if (oldRookPos.myEquals(new Pos(7, 7))) {
            Pos wKingPos = KingPiece.getKingPos(true);
            ((KingPiece) board[wKingPos.getR()][wKingPos.getC()]).revokeCanCastleShort();
        }
    }

    /**
     * checkmated checks whether the game has reached a win condition.
     *
     * @return -1 if nobody has won yet, 0 if white has won, 1 if black
     *         has won, and 2 if the game hits stalemate
     */
    public int checkmated() {
        //checks if any opposing piece can move
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].isWhite() == player1) {
                    for (int k = 0; k < 8; k++) {
                        for (int l = 0; l < 8; l++) {
                            if (board[i][j].isSquareLegal(getBoard(),
                                    new Pos(i, j), new Pos(k, l))) {
                                return -1;
                            }
                        }
                    }
                }
            }
        }

        //checks stalemate
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].isWhite() != player1) {
                    if (board[i][j].isSquareLegal(getBoard(),
                            new Pos(i, j), KingPiece.getKingPos(player1))) {
                        if (!player1) {
                            return 0; //white wins
                        } else {
                            return 1; //black wins
                        }
                    }
                }
            }
        }

        //stalemate returns 2
        return 2;


    }

    public GamePiece getCurrentPiece() {
        return currentPiece;
    }


    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numTurns + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    System.out.print("0");
                } else {
                    System.out.print(board[i][j]);
                }

                if (j < 8) {
                    System.out.print(" | ");
                }
            }
            if (i < 8) {
                System.out.println("\n------------------------------");
            }
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new GamePiece[8][8];

        for (int i = 0; i < 8; i++) {
            board[1][i] = new PawnPiece(false);
            board[6][i] = new PawnPiece(true);
        }

        board[7][0] = new RookPiece(true);
        board[7][7] = new RookPiece(true);
        board[0][0] = new RookPiece(false);
        board[0][7] = new RookPiece(false);

        board[7][1] = new KnightPiece(true);
        board[7][6] = new KnightPiece(true);
        board[0][1] = new KnightPiece(false);
        board[0][6] = new KnightPiece(false);

        board[7][2] = new BishopPiece(true);
        board[7][5] = new BishopPiece(true);
        board[0][2] = new BishopPiece(false);
        board[0][5] = new BishopPiece(false);

        board[7][3] = new QueenPiece(true);
        board[0][3] = new QueenPiece(false);
        board[7][4] = new KingPiece(true);
        board[0][4] = new KingPiece(false);


        numTurns = 0;
        player1 = true;
        pieceClicked = false;
        currentPiece = null;
        currentPiecePos = new Pos(-1, -1);
        PawnPiece.setEnPassantSquarePos(-1, -1);
        KingPiece.setKingPos(7, 4, true);
        KingPiece.setKingPos(0, 4, false);
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     * 
     * @return true if it's Player 1 (White)'s turn,
     *         false if it's Player 2 (Black)'s turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }
    public boolean isPieceClicked() {
        return pieceClicked;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public GamePiece getCell(int c, int r) {
        return board[r][c];
    }

    public GamePiece[][] getBoard() {
        if (board == null) {
            return null;
        }

        GamePiece[][] copy = new GamePiece[board.length][];
        for (int i = 0; i < board.length; i++) {
            copy[i] = board[i].clone();
        }

        return copy;
    }

    public Pos getCurrentPiecePos() {
        return currentPiecePos;
    }

    /*
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the Chess game from start to finish
     * without ever creating a Java Swing object.
     */
    public static void main(String[] args) {
        Chess t = new Chess();

        //white pawn e4
        t.playTurn(4, 6);
        t.playTurn(4, 4);
        t.printGameState();

        //black pawn e5
        t.playTurn(4, 1);
        t.playTurn(4, 3);
        t.printGameState();

        //white bishop c4
        t.playTurn(5, 7);
        t.playTurn(2, 4);
        t.printGameState();

        //black bishop c5
        t.playTurn(5, 0);
        t.playTurn(2, 3);
        t.printGameState();

        //white queen h5
        t.playTurn(3, 7);
        t.playTurn(7, 3);
        t.printGameState();

        //black knight f6
        t.playTurn(6, 0);
        t.playTurn(5, 2);
        t.printGameState();

        //white queen f7, checkmate
        t.playTurn(7, 3);
        t.playTurn(5, 1);
        t.printGameState();

        System.out.println();
        int result = t.checkmated();
        if (result == 0) {
            System.out.println("Winner is: White");
        } else if (result == 1) {
            System.out.println("Winner is: Black");
        } else if (result == 2) {
            System.out.println("Stalemate");
        } else {
            System.out.println("Game not finished");
        }
    }
}
