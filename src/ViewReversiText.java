import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
    if (evt.getPropertyName().equals("ScoreUpdate")) {
      ReversiModel o = (ReversiModel) evt.getSource();
      scoreBlack = o.getBlackScore();
      scoreWhite = o.getWhiteScore();
      turn = o.getTurnColor();
      repaint();
    }
  }
}
