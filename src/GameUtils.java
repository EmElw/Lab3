import java.awt.*;

/**
 * Created by Magnus on 2016-12-08.
 */
class GameUtils implements GameModel {

    @Override
    public void setGameboardState(Position pos, GameTile tile, GameTile[][] matrix) throws IndexOutOfBoundsException {
        matrix[pos.getX()][pos.getY()] = tile;
    }

    @Override
    public void setGameboardState(int x, int y, GameTile tile, GameTile[][] set) throws IndexOutOfBoundsException {
        setGameboardState(new Position(x, y), tile, set);
    }

    @Override
    public GameTile getGameboardState(Position pos, GameTile[][] matrix) {
        return matrix[pos.getX()][pos.getY()];
    }

    @Override
    public GameTile getGameboardState(int x, int y, GameTile[][] matrix) {
        return getGameboardState(new Position(x, y), matrix);
    }

    @Override
    public Dimension getGameboardSize(GameTile[][] matrix) {
        return new Dimension(matrix.length, matrix[0].length);
    }

    @Override
    public void gameUpdate(int lastKey) throws GameOverException {
    }
}
