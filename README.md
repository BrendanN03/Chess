# Chess
## Overview
This document provides instructions and information about the Chess Game developed using Java Swing. The game allows you to play chess against another human player on the same computer.

## Game Rules
This Chess Game follows the standard rules of chess. You can find comprehensive information about the rules of chess at [FIDE - The International Chess Federation](https://www.fide.com/FIDE/handbook/LawsOfChess.pdf).

## User Interface
The user interface of the Chess Game is designed using Java Swing, providing an interactive and user-friendly experience. Here are some key features of the interface:
- A chessboard with 64 squares.
- Player names and a message area to display game status.
- Pieces captured by each player.
- Option to restart the game.
- Highlighted legal moves for the selected piece.
  
## Features
- Two-player local gameplay: Play against a friend on the same computer.
- Legal move validation: The game ensures that the rules of chess are followed.
- Check and checkmate detection: The game identifies when a player's king is in check and when a game ends in checkmate.
- Piece promotion: When a pawn reaches the opponent's back rank, it can be promoted to a higher-ranking piece (queen, rook, bishop, or knight).
- Castling and en passant: The game supports these special chess moves.
- Stalemate and draw detection.
  
## Known Issues
- No cases for stalemate by insufficient material or stalemate by 40 moves.
- No support for saving or loading game states.
- Lack of AI opponent for single-player mode.
