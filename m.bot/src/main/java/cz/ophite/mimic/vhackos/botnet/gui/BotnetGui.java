package cz.ophite.mimic.vhackos.botnet.gui;

import cz.ophite.mimic.vhackos.botnet.Application;
import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.config.ApplicationConfig;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandDispatcher;
import cz.ophite.mimic.vhackos.botnet.utils.appender.HackedAppender;
import cz.ophite.mimic.vhackos.botnet.utils.appender.IAppender;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Grafické rozhraní pro botnet.
 *
 * @author mimic
 */
public final class BotnetGui extends JFrame {

    private static final Logger LOG = LoggerFactory.getLogger(BotnetGui.class);

    private static final int AREA_BUFFER = 1024 * 512;

    private JTextPane area;
    private JPanel bottomPane;
    private JScrollPane scroll;
    private JFormattedTextField tfCommand;
    private Font font;

    public void open() {
        HackedAppender.getInstance().addListener(new AppenderLogic());

        font = getAreaFont();
        prepareComponents();
        setVisible(true);
    }

    public void close(int exitCode) {
        bottomPane.setVisible(false);

        if (exitCode != Application.ERROR_CODE_MISSING_LOGIN_CREDENTIAL) {
            dispose();
            System.exit(0);
        } else {
            LOG.info("Exit the application manually");
        }
    }

    public void postProcessing(ApplicationConfig config) {
        if (config.isFullScreenMode()) {
            dispose();

            var gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

            setSize(new Dimension(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight()));
            setUndecorated(true);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    private void prepareComponents() {
        var gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        var width = (int) (0.8 * gd.getDisplayMode().getWidth());
        var height = (int) (0.8 * gd.getDisplayMode().getHeight());

        setTitle(String.format("vHack OS - Botnet v%s | by mimic", IBotnet.VERSION));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(width, height));
        setFont(font);
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.BLACK);

        getContentPane().setBackground(getBackground());

        var sc = new StyleContext();
        var defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        var mainStyle = sc.addStyle("MainStyle", defaultStyle);

        StyleConstants.setFontFamily(mainStyle, getFont().getFamily());
        StyleConstants.setFontSize(mainStyle, 13);

        var doc = new DefaultStyledDocument(sc);
        area = new JTextPane(doc);
        area.setContentType("text/html");
        area.setBackground(getBackground());
        area.setForeground(Color.LIGHT_GRAY);
        area.setFont(font);
        area.setEditable(false);
        area.setBorder(BorderFactory.createEmptyBorder());
        area.setFocusable(false);

        var caret = (DefaultCaret) area.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scroll = new JScrollPane(new VerticalScrollPanel(area));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setViewportView(area);
        scroll.setBackground(getBackground());
        scroll.setBorder(BorderFactory.createEmptyBorder());

        bottomPane = new JPanel();
        bottomPane.setBackground(getBackground());
        bottomPane.setLayout(new BorderLayout(8, 0));

        var lbCommand = new JLabel("Command:");
        lbCommand.setBackground(getBackground());
        lbCommand.setForeground(Color.ORANGE);
        lbCommand.setFont(font.deriveFont(16f));
        bottomPane.add(lbCommand, BorderLayout.LINE_START);

        tfCommand = new JFormattedTextField();
        tfCommand.setBackground(getBackground());
        tfCommand.setForeground(Color.WHITE);
        tfCommand.setBorder(BorderFactory.createEmptyBorder());
        tfCommand.setFont(lbCommand.getFont());
        tfCommand.setCaretColor(Color.ORANGE);
        tfCommand.requestFocus();
        tfCommand.addActionListener(evt -> new Thread(() -> {
            tfCommand.setEnabled(false);
            CommandDispatcher.getInstance().call(tfCommand.getText());
            tfCommand.setText("");
            tfCommand.setEnabled(true);
            tfCommand.requestFocus();
        }).start());
        bottomPane.add(tfCommand, BorderLayout.CENTER);

        var tfCommandCaret = (DefaultCaret) tfCommand.getCaret();
        tfCommandCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        add(scroll, BorderLayout.CENTER);
        add(bottomPane, BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(null);
    }

    private static Font getAreaFont() {
        try {
            var is = Application.class.getResourceAsStream("/font.ttf");
            var font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(13f);
            LOG.debug("The custom font family '{}' has been loaded", font.getFamily());
            return font;

        } catch (Exception e) {
            LOG.error("An error occurred while loading inernal the font", e);
            return new Font(Font.MONOSPACED, Font.PLAIN, 13);
        }
    }

    private void appendToPane(String msg, Color c) {
        if (area == null) {
            return;
        }
        var keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, c);
        StyleConstants.setBackground(keyWord, getBackground());

        var doc = area.getStyledDocument();
        try {
            if (doc.getLength() > AREA_BUFFER) {
                doc.remove(0, doc.getLength() - AREA_BUFFER);
            }
            doc.insertString(doc.getLength(), msg, keyWord);
            area.setCaretPosition(doc.getLength());

        } catch (Exception e) {
            LOG.error("There was an error writing message '" + msg + "' to the GUI log", e);
        }
    }

    /**
     * Logika pro zápis z GUI appenderu do logu.
     */
    private final class AppenderLogic implements IAppender {

        @Override
        public void append(LogEvent event, String message) {
            var c = Color.WHITE;

            if (event.getLevel() == Level.INFO) {
                c = Color.WHITE;
            } else if (event.getLevel() == Level.WARN) {
                c = Color.YELLOW;
            } else if (event.getLevel() == Level.ERROR) {
                c = Color.RED;
            } else if (event.getLevel() == Level.TRACE) {
                c = Color.CYAN;
            } else if (event.getLevel() == Level.DEBUG) {
                c = Color.GRAY;
            }

            var lines = StringUtils.countMatches(event.getMessage().getFormattedMessage(), "\n");
            if (lines > 1) {
                appendToPane(event.getMessage().getFormattedMessage(), Color.ORANGE);
            } else {
                appendToPane(message, c);
            }
        }
    }
}
