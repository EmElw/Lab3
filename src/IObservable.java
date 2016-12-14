import java.beans.PropertyChangeListener;

/**
 * Created by Magnus on 2016-12-09.
 */
public interface IObservable {

    void addObserver(PropertyChangeListener observer);

    void removeObserver(PropertyChangeListener observer);
}
