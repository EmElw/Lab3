import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * Created by Magnus on 2016-12-15.
 */
public class ViewReversiText extends JComponent implements ViewUI {

    private int scoreBlack;
    private int scoreWhite;
    ReversiModel.Turn turn;

    private Dimension d;

    public ViewReversiText(ReversiModel o) {
        this.d = o.getGameboardSize();
    }

    @Override
    public void paint(Graphics g) {
        // Draw turn
        g.drawRect(0, 0, d.width, d.height);
        g.drawString((turn == ReversiModel.Turn.BLACK ? "Black" : "White"),
                20, 25);

        // Draw score
        g.drawString("B: " + scoreBlack,
                d.height - 10, 20);

        g.drawString("W: " + scoreWhite,
                d.height - 10, d.width - 50);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ReversiModel o = (ReversiModel) evt.getSource();
        scoreBlack = o.getBlackScore();
        scoreWhite = o.getWhiteScore();
        turn = o.getTurnColor();
        System.out.printf("Bong! Black: %d \t White: %d\n", scoreBlack, scoreWhite);
        // PLZ WORK
        repaint();
    }
}
