package org.cis1200.chess;

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games.
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a Chess object to serve as the game's model.
 */
public class RunChess implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Chess");
        frame.setLocation(320, 20);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        final JTextArea instructions = new JTextArea();
        instructions.setText(
                "Hello! Welcome to my chess game! \n" +
                "My chess game includes all the rules of a basic chess game, \n " +
                "including castling, en Passant, check, and checkmate. \n" +
                "To play, this game uses clicks instead of mouse drags. \n" +
                "To select a piece, click on that piece using the mouse. \n" +
                "If that piece is a valid piece (i.e. a non-empty square with a piece \n" +
                "that is the same color of the current player) then that piece will be \n" +
                "selected, and all the valid squares it can be played on will be shown. \n" +
                "If you click again in a valid square, then the piece will \n" +
                "move to that square, and it will then switch to the next player's turn. \n" +
                "Once a player is in checkmate, the game will end. Have fun!");
        frame.add(instructions, BorderLayout.CENTER);

        // Game board
        final org.cis1200.chess.GameBoard board = new org.cis1200.chess.GameBoard(status);


        // Reset button
        final JPanel control_panel = new JPanel();

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        //control_panel.add(instButton);
        frame.add(control_panel, BorderLayout.NORTH);
        frame.add(board, BorderLayout.CENTER);

        //frame.add(instructions, BorderLayout.CENTER);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Start the game
        board.reset();

        //instructions button
        final JFrame instFrame = new JFrame("Chess");
        instFrame.setLocation(500, 200);
        final JPanel control_panel2 = new JPanel();
        final JButton instButton = new JButton("Start Game");
        control_panel2.add(instButton);
        instButton.addActionListener(e -> instFrame.setVisible(false));
        instButton.addActionListener(e -> frame.setVisible(true));
        instFrame.add(control_panel2, BorderLayout.SOUTH);
        instFrame.add(instructions, BorderLayout.CENTER);

        instFrame.pack();
        instFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instFrame.setVisible(true);

    }
}