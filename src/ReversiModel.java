import java.awt.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A somewhat defective implementation of the game Reversi. The purpose
 * of this class is to illustrate shortcomings in the game framework.
 *
 * @author evensen
 */
public class ReversiModel implements GameModel {

    // Helper for handling property changes
    private PropertyChangeSupport observers;

    public enum Direction {
        EAST(1, 0),
        SOUTHEAST(1, 1),
        SOUTH(0, 1),
        SOUTHWEST(-1, 1),
        WEST(-1, 0),
        NORTHWEST(-1, -1),
        NORTH(0, -1),
        NORTHEAST(1, -1),
        NONE(0, 0);

        private final int xDelta;

        private final int yDelta;

        Direction(final int xDelta, final int yDelta) {
            this.xDelta = xDelta;
            this.yDelta = yDelta;
        }

        public int getXDelta() {
            return this.xDelta;
        }

        public int getYDelta() {
            return this.yDelta;
        }

    }

    public enum Turn {
        BLACK,
        WHITE;

        public static Turn nextTurn(final Turn t) {
            return t == BLACK ? WHITE : BLACK;
        }

    }

    public enum PieceColor {
        BLACK,
        WHITE,
        EMPTY;

        public static PieceColor opposite(final PieceColor t) {
            return t == BLACK ? WHITE : BLACK;
        }

    }

    /**
     * The size of the state matrix.
     */
    private final Dimension gameboardSize = Constants.getGameSize();

    /**
     * Graphical representation of a coin.
     */
    private static final GameTile blackTile = new RoundTile(Color.BLACK,
            Color.BLACK, 1.0, 0.8);


    private static final GameTile whiteTile = new RoundTile(Color.BLACK,
            Color.WHITE, 1.0, 0.8);
    private static final GameTile blankTile = new SquareTile(Color.BLACK,
            new Color(0, 200, 0), 2.0);
    private static final GameTile cursorRedTile = new CrossTile(Color.RED, 2.0);
    private Turn turn;

    private Position cursorPos;
    private final PieceColor[][] board;
    private int whiteScore;
    private int blackScore;
    private final int width;
    private final int height;
    private boolean gameOver;

    public ReversiModel() {
        this.observers = new PropertyChangeSupport(this);
        this.width = Constants.getGameSize().width;
        this.height = Constants.getGameSize().height;
        this.board = new PieceColor[this.width][this.height];

        // Blank out the whole gameboard...
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.board[i][j] = PieceColor.EMPTY;
            }
        }

        this.turn = Turn.BLACK;

        // Insert the four starting bricks.
        int midX = this.width / 2 - 1;
        int midY = this.height / 2 - 1;
        this.board[midX][midY] = PieceColor.WHITE;
        this.board[midX + 1][midY + 1] = PieceColor.WHITE;
        this.board[midX + 1][midY] = PieceColor.BLACK;
        this.board[midX][midY + 1] = PieceColor.BLACK;

        // Set the initial score.
        this.whiteScore = 2;
        this.blackScore = 2;

        this.gameOver = false;

        // Insert the collector in the middle of the gameboard.
        this.cursorPos = new Position(midX, midY);
    }

    /**
     * Return whether the specified position is empty. If it only consists
     * of a blank tile, it is considered empty.
     *
     * @param pos The position to test.
     * @return true if position is empty, false otherwise.
     */
    private boolean isPositionEmpty(final Position pos) {
        return this.board[pos.getX()][pos.getY()] == PieceColor.EMPTY;
    }

    /**
     * Update the direction of the collector
     * according to the users keypress.
     *
     * @throws GameOverException
     */
    private Direction updateDirection(final int key) throws GameOverException {
        switch (key) {
            case KeyEvent.VK_LEFT:
                return Direction.WEST;
            case KeyEvent.VK_UP:
                return Direction.NORTH;
            case KeyEvent.VK_RIGHT:
                return Direction.EAST;
            case KeyEvent.VK_DOWN:
                return Direction.SOUTH;
            case KeyEvent.VK_SPACE:
                tryPlay();
                return Direction.NONE;
            default:
                // Do nothing if another key is pressed
                return Direction.NONE;
        }
    }

    /**
     * Attempts to play at the current cursor position, and flips tiles if possible
     */
    private void tryPlay() {

        if (isPositionEmpty(this.cursorPos)) {
            if (canTurn(this.turn, this.cursorPos)) {
                turnOver(this.turn, this.cursorPos);
                this.board[this.cursorPos.getX()][this.cursorPos.getY()] =
                        (this.turn == Turn.BLACK
                                ? PieceColor.BLACK
                                : PieceColor.WHITE);
                this.turn = Turn.nextTurn(this.turn);
            }
            if (!canTurn(this.turn)) {
                if (!canTurn(Turn.nextTurn(this.turn))) {
                    this.gameOver = true;
                    return;
                }

                this.turn = Turn.nextTurn(this.turn);
            }
        }
    }

    /**
     * Flips all the tiles based on the input move
     *
     * @param turn      the current player
     * @param cursorPos the position to flip from
     */
    private void turnOver(final Turn turn, final Position cursorPos) {
        if (isPositionEmpty(cursorPos)) {
            PieceColor myColor =
                    (turn == Turn.BLACK ? PieceColor.BLACK : PieceColor.WHITE);
            PieceColor opponentColor = PieceColor.opposite(myColor);
            int blackResult = (turn == Turn.BLACK) ? 1 : -1;
            int whiteResult = -blackResult;

            this.blackScore += Math.max(0, blackResult);
            this.whiteScore += Math.max(0, whiteResult);

            for (int i = 0; i < 8; i++) {
                Direction d = Direction.values()[i];
                int xDelta = d.getXDelta();
                int yDelta = d.getYDelta();
                int x = cursorPos.getX() + xDelta;
                int y = cursorPos.getY() + yDelta;
                boolean canTurn = false;
                while (x >= 0 && x < this.width && y >= 0 && y < this.height) {
                    if (this.board[x][y] == opponentColor) {
                        canTurn = true;
                    } else if (this.board[x][y] == myColor && canTurn) {
                        // Move backwards to the cursor, flipping bricks
                        // as we go.
                        x -= xDelta;
                        y -= yDelta;
                        while (!(x == cursorPos.getX() && y == cursorPos.getY())) {
                            this.board[x][y] = myColor;
                            x -= xDelta;
                            y -= yDelta;
                            this.blackScore += blackResult;
                            this.whiteScore += whiteResult;
                        }
                        break;
                    } else {
                        break;
                    }
                    x += xDelta;
                    y += yDelta;
                }
            }
        }
    }

    /**
     * Checks if the player has any eligible moves
     *
     * @param turn the player to check
     * @return true if player can make a move, false if not
     */
    private boolean canTurn(final Turn turn) {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (canTurn(turn, new Position(x, y))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the player can make a move at the given position
     *
     * @param turn      the player to check
     * @param cursorPos the position to check
     * @return true if possible move, false if not
     */
    private boolean canTurn(final Turn turn, final Position cursorPos) {
        if (isPositionEmpty(cursorPos)) {
            PieceColor myColor =
                    (turn == Turn.BLACK ? PieceColor.BLACK : PieceColor.WHITE);
            PieceColor opponentColor = PieceColor.opposite(myColor);
            for (int i = 0; i < 8; i++) {
                Direction d = Direction.values()[i];
                int xDelta = d.getXDelta();
                int yDelta = d.getYDelta();
                int x = cursorPos.getX() + xDelta;
                int y = cursorPos.getY() + yDelta;
                boolean canTurn = false;
                while (x >= 0 && x < this.width && y >= 0 && y < this.height) {
                    if (this.board[x][y] == opponentColor) {
                        canTurn = true;
                    } else if (this.board[x][y] == myColor && canTurn) {
                        return true;
                    } else {
                        break;
                    }
                    x += xDelta;
                    y += yDelta;
                }
            }
        }
        return false;
    }

    /**
     * Get the current player's color
     */
    public Turn getTurnColor() {
        return this.turn;
    }

    /**
     * Accessor to black's current score.
     *
     * @return black's score
     */
    public int getBlackScore() {
        return this.blackScore;
    }

    /**
     * Accessor to white's current score.
     *
     * @return white's score
     */
    public int getWhiteScore() {
        return this.whiteScore;
    }

    /**
     * Get next position of the collector.
     */
    private Position getNextCursorPos(final Direction dir) {
//    System.out.println(cursorPos.getX() + dir.getXDelta() + " / " + (cursorPos.getY() + dir.getYDelta()));

        return new Position(this.cursorPos.getX()
                + dir.getXDelta(),
                this.cursorPos.getY() + dir.getYDelta());
    }

    /**
     * Gets the reference to the GameTile at the given position
     *
     * @param pos the position to read
     * @return a array of GameTiles at the given position
     * @throws IndexOutOfBoundsException if the position is out of bounds
     */
    @Override
    public GameTile[] getGameboardState(Position pos) throws IndexOutOfBoundsException {
        return getGameboardState(pos.getX(), pos.getY());
    }

    /**
     * Gets the reference to the GameTile at the given position
     *
     * @param x the x-coordinate to read
     * @param y the y-coordinate to read
     * @return a array of GameTiles at the given position
     * @throws IndexOutOfBoundsException if the position is out of bounds
     */
    public GameTile[] getGameboardState(int x, int y) throws IndexOutOfBoundsException {
        // Uses 3 "layers"; a bottom (always a blank tile), the color of piece; if any, and the cursor; if any.

        // 2nd-layer
        GameTile piece = null;
        if (board[x][y] == PieceColor.BLACK) piece = blackTile;
        if (board[x][y] == PieceColor.WHITE) piece = whiteTile;

        // 3rd-layer
        Position pos = new Position(x, y);
        GameTile top = null;
        if (cursorPos.equals(pos)) {
            // TODO change bottom if placeable
            if (canTurn(turn, pos)) {   // if the current player can turn the tile, and the cursor is there, mark it
                piece = (turn == Turn.BLACK ?
                        blackTile :
                        whiteTile);
            }
            top = cursorRedTile;
        }
        return new GameTile[]{blankTile, piece, top};
    }

    /**
     * Returns the size of the game board
     *
     * @return the size of the game board
     */
    @Override
    public Dimension getGameboardSize() {
        return gameboardSize;
    }

    /**
     * Returns an integer value of milliseconds
     *
     * @return integer milliseconds T betweene each update
     */
    @Override
    public int getGameUpdateSpeed() {
        return -1;
    }

    @Override
    public ViewUI createUI() {
        return new ViewReversiText(this);
    }

    @Override
    public void removeObserver(PropertyChangeListener observer) {
        observers.removePropertyChangeListener(observer);
    }

    @Override
    public void addObserver(PropertyChangeListener observer) {
        observers.addPropertyChangeListener(observer);
    }

    /**
     * This method is called repeatedly so that the
     * game can update its state.
     *
     * @param lastKey The most recent keystroke.
     */
    @Override
    public void gameUpdate(final int lastKey) throws GameOverException {
        if (!this.gameOver) {
            Position nextCursorPos = getNextCursorPos(updateDirection(lastKey));
            Dimension boardSize = getGameboardSize();
            int nextX =
                    Math.max(0,
                            Math.min(nextCursorPos.getX(), boardSize.width - 1));
            int nextY =
                    Math.max(
                            0,
                            Math.min(nextCursorPos.getY(), boardSize.height - 1));
            nextCursorPos = new Position(nextX, nextY);
            this.cursorPos = nextCursorPos;
            observers.firePropertyChange(new PropertyChangeEvent(this, null, null, null));
        } else {
            throw new GameOverException(this.blackScore - this.whiteScore);
        }
    }

}
