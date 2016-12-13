import java.awt.*;

/**
 * Created by Magnus on 2016-12-08.
 */
class GameUtils {

    /**
     * Sets the reference of the given position in the given matrix to the given GameTile
     * @param pos the position in the matrix to set
     * @param tile the tile to set to
     * @param matrix the matrix to set in
     * @throws IndexOutOfBoundsException if position is out of bounds
     */
    static public void setGameboardState(Position pos, GameTile tile, GameTile[][] matrix) throws IndexOutOfBoundsException {
        matrix[pos.getX()][pos.getY()] = tile;
    }

    /**
     * Sets the reference of the given position in the given matrix to the given GameTile
     * @param x the x-coordinate in the matrix to set
     * @param y the y-coordinate in the matrix to set
     * @param tile the tile to set to
     * @param matrix the matrix to set in
     * @throws IndexOutOfBoundsException if position is out of bounds
     */
    static public void setGameboardState(int x, int y, GameTile tile, GameTile[][] matrix) throws IndexOutOfBoundsException {
        setGameboardState(new Position(x, y), tile, matrix);
    }

    /**
     * Gets the reference to the GameTile at the given position
     * @param pos the position to read
     * @param matrix the matrix to read from
     * @return a GameTile at the given position/matrix
     * @throws IndexOutOfBoundsException if the position is out of bounds
     */
    static public GameTile getGameboardState(Position pos, GameTile[][] matrix) {
        return matrix[pos.getX()][pos.getY()];
    }

    /**
     * Gets the reference to the GameTile at the given position
     * @param x the x-coordinate to read
     * @param y the y-coordinate to read
     * @param matrix the matrix to read from
     * @return a GameTile at the given position/matrix
     * @throws IndexOutOfBoundsException if the position is out of bounds
     */
    static public GameTile getGameboardState(int x, int y, GameTile[][] matrix) {
        return getGameboardState(new Position(x, y), matrix);
    }

    /**
     * Returns the size of the game board
     * @param matrix the game board
     * @return the size of the game board
     */
    static public Dimension getGameboardSize(GameTile[][] matrix) {
        return new Dimension(matrix.length, matrix[0].length);
    }
}
