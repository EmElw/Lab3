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

    void setGameboardState(Position pos, GameTile tile, GameTile[][] matrix) throws IndexOutOfBoundsException;

    void setGameboardState(final int x, final int y,
                           final GameTile tile, GameTile[][] matrix) throws IndexOutOfBoundsException;

    GameTile getGameboardState(final Position pos, GameTile[][] matrix) throws IndexOutOfBoundsException;

    GameTile getGameboardState(final int x, final int y, GameTile[][] matrix) throws IndexOutOfBoundsException;

    /**
     * Returns the size of the gameboard.
     */
    Dimension getGameboardSize(GameTile[][] matrix);

    /**
     * This method is called repeatedly so that the game can update it's state.
     *
     * @param lastKey The most recent keystroke.
     */
    void gameUpdate(int lastKey) throws GameOverException;
}
