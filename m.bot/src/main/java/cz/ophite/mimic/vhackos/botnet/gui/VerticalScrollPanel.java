package cz.ophite.mimic.vhackos.botnet.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pouze vertikální posuvník.
 *
 * @author mimic
 */
public final class VerticalScrollPanel extends JPanel implements Scrollable {

    private VerticalScrollPanel() {
        this(new GridLayout(0, 1));
    }

    private VerticalScrollPanel(LayoutManager lm) {
        super(lm);
    }

    VerticalScrollPanel(Component comp) {
        this();
        add(comp);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 100;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
