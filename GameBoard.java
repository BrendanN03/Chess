import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Chess chessModel; // model for the game
    private JLabel status; // current status text



    // Game constants
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        chessModel = new Chess(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                chessModel.playTurn(p.x / 100, p.y / 100);

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        chessModel.reset();
        status.setText("White's Turn");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void showInstructions() {

    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        String stringStatus;
        if (chessModel.getCurrentPlayer()) {
            stringStatus = "White's Turn";
        } else {
            stringStatus = "Black's Turn";
        }
        if (chessModel.isPieceClicked()) {
            stringStatus += ". Awaiting square to place piece.";
        }
        status.setText(stringStatus);

        int winner = chessModel.checkmated();
        if (winner == 0) {
            status.setText("White wins!!!");
        } else if (winner == 1) {
            status.setText("Black wins!!!");
        } else if (winner == 2) {
            status.setText("Stalemate.");
        }
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        for (int i = 100; i <= 700; i += 100) {
            g.drawLine(i, 0, i, 800);
            g.drawLine(0, i, 800, i);
        }

        for (int i = 100; i <= 800; i += 100) {
            g.drawString("A" + (i / 100), 5, 900 - i);
        }
        for (int i = 100; i <= 800; i += 100) {
            g.drawString("B" + (i / 100), 105, 900 - i);
        }
        for (int i = 100; i <= 800; i += 100) {
            g.drawString("C" + (i / 100), 200, 900 - i);
        }
        for (int i = 100; i <= 800; i += 100) {
            g.drawString("D" + (i / 100), 300, 900 - i);
        }
        for (int i = 100; i <= 800; i += 100) {
            g.drawString("E" + (i / 100), 400, 900 - i);
        }
        for (int i = 100; i <= 800; i += 100) {
            g.drawString("F" + (i / 100), 500, 900 - i);
        }
        for (int i = 100; i <= 800; i += 100) {
            g.drawString("G" + (i / 100), 600, 900 - i);
        }
        for (int i = 100; i <= 800; i += 100) {
            g.drawString("H" + (i / 100), 700, 900 - i);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                GamePiece state = chessModel.getCell(j, i);
                if (state != null) {
                    state.draw(g, 30 + 100 * j, 30 + 100 * i);
                }
            }
        }

        if (chessModel.isPieceClicked()) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessModel.getCurrentPiece().isSquareLegal(chessModel.getBoard(),
                            chessModel.getCurrentPiecePos(), new Pos(i, j))) {
                        g.drawOval(30 + 100 * j, 30 + 100 * i, 40, 40);
                    }
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
