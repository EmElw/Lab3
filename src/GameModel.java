import java.awt.Dimension;

/**
 * Common superclass for all game model classes.
 * <p>
 * Constructors of subclasses should initiate matrix elements and additional,
 * game-dependent fields.
 */
public interface GameModel {

//	/** A Matrix containing the state of the gameboard. */
//	private final GameTile[][] gameboardState;
//
//	/** The size of the state matrix. */
//	private final Dimension gameboardSize = Constants.getGameSize();
//
//	/**
//	 * Create a new game model. As GameModel is an abstract class, this is only
//	 * intended for subclasses.
//	 */
//	protected GameModel() {
//		this.gameboardState =
//				new GameTile[this.gameboardSize.width][this.gameboardSize.height];
//	}

//    /**
//     * Sets the reference of the given position in the given matrix to the given GameTile
//     * @param pos the position to set
//     * @param tile the tile to set to
//     * @throws IndexOutOfBoundsException if position is out of bounds
//     */
//    void setGameboardState(final Position pos,
//                           final GameTile tile) throws IndexOutOfBoundsException;
//
//    /**
//     * Sets the reference of the given position in the given matrix to the given GameTile
//     * @param x the x-coordinate in the matrix to set
//     * @param y the y-coordinate in the matrix to set
//     * @param tile the tile to set to
//     * @throws IndexOutOfBoundsException if position is out of bounds
//     */
//    void setGameboardState(int x, int y, GameTile tile) throws IndexOutOfBoundsException;

    /**
     * Gets the reference to the GameTile at the given position
     * @param pos the position to read
     * @return a GameTile at the given position/matrix
     * @throws IndexOutOfBoundsException if the position is out of bounds
     */
    GameTile[] getGameboardState(final Position pos) throws IndexOutOfBoundsException;

    /**
     * Gets the reference to the GameTile at the given position
     * @param x the x-coordinate to read
     * @param y the y-coordinate to read
     * @return a GameTile at the given position/matrix
     * @throws IndexOutOfBoundsException if the position is out of bounds
     */
    GameTile[] getGameboardState(int x, int y);

    /**
     * Returns the size of the game board
     * @return the size of the game board
     */
    Dimension getGameboardSize();

    /**
     * This method is called repeatedly so that the game can update it's state.
     *
     * @param lastKey The most recent keystroke.
     */
    void gameUpdate(int lastKey) throws GameOverException;
}
