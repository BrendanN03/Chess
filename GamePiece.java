import java.awt.*;

public interface GamePiece {
    public String getPiece();

    public boolean isWhite();
    public boolean isSquareLegal(GamePiece[][] board, Pos oldPos, Pos newPos);

    public boolean isSquareLegal(GamePiece[][] board, Pos oldPos, Pos newPos, boolean isGhostCall);
    public void draw(Graphics g, int x, int y);
}
