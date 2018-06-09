package cz.ophite.mimic.vhackos.botnet.gui;

import cz.ophite.mimic.vhackos.botnet.Application;
import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.config.ApplicationConfig;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandDispatcher;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SentryGuard;
import cz.ophite.mimic.vhackos.botnet.utils.ResourceHelper;
import cz.ophite.mimic.vhackos.botnet.utils.appender.HackedAppender;
import cz.ophite.mimic.vhackos.botnet.utils.appender.IAppender;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Grafické rozhraní pro botnet.
 *
 * @author mimic
 */
public final class BotnetGui extends JFrame {

    private static final Logger LOG = LoggerFactory.getLogger(BotnetGui.class);

    private JTextPane area;
    private JPanel bottomPane;
    private JScrollPane scroll;
    private JFormattedTextField tfCommand;
    private Font font;

    private int bufferSize = 256 * 1024; // výchozí

    public void open() {
        HackedAppender.getInstance().addListener(new AppenderLogic());

        font = getMainFont();
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
        bufferSize = config.getGuiAreaBufferSize();

        if (config.isFullScreenMode()) {
            dispose();

            var gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

            setSize(new Dimension(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight()));
            setUndecorated(true);
            setLocationRelativeTo(null);
            setVisible(true);
        } else {
            if (config.hasValidCredentials()) {
                setTitle(getTitle() + " | Account: " + config.getUserName());

                var proxy = config.getProxyData();
                if (proxy != null) {
                    setTitle(getTitle() + " | Proxy: " + proxy);
                }
            }
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
        area.setEditorKit(new HTMLEditorKit());
        area.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                area.setFocusable(true);
                area.requestFocus();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                area.setFocusable(false);
                tfCommand.requestFocus();
            }
        });

        var caret = (DefaultCaret) area.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

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

    private static Font getMainFont() {
        var font = ResourceHelper.loadFont();

        if (font != null) {
            font = font.deriveFont(13f);
            LOG.debug("The custom font family '{}' has been loaded", font.getFamily());
        } else {
            font = new Font(Font.MONOSPACED, Font.PLAIN, 13);
        }
        return font;
    }

    private void appendToPane(String msg, Color c) {
        if (area == null) {
            return;
        }
        var doc = (HTMLDocument) area.getStyledDocument();
        // definice klíčových slov
        var keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, c);
        StyleConstants.setBackground(keyWord, getBackground());
        StyleConstants.setFontFamily(keyWord, font.getFamily());
        keyWord.addAttribute(StyleConstants.NameAttribute, HTML.Tag.FONT);

        try {
            if (doc.getLength() > bufferSize) {
                doc.remove(0, doc.getLength() - bufferSize);
            }
            smartAppend(doc, msg, keyWord, c);

        } catch (Exception e) {
            SentryGuard.logError("Error writing GUI record", new Object[]{ msg, e.toString() });
            LOG.error("There was an error writing message '" + msg + "' to the GUI log", e);
        }
    }

    private void smartAppend(HTMLDocument doc, String msg, SimpleAttributeSet keyWord, Color color)
            throws BadLocationException {

        synchronized (LOG) {
            var services = Service.getServiceClassNames();
            var serviceLine = false;

            for (var s : services) {
                var i = msg.indexOf(String.format("[%s]", s));

                if (i >= 0) {
                    var p1 = msg.substring(0, ++i);
                    var p2 = msg.substring(i + s.length(), msg.length());

                    doc.insertString(doc.getLength(), p1, keyWord);
                    StyleConstants.setForeground(keyWord, Color.GREEN);
                    doc.insertString(doc.getLength(), s, keyWord);
                    StyleConstants.setForeground(keyWord, color);
                    doc.insertString(doc.getLength(), p2, keyWord);

                    serviceLine = true;
                    break;
                }
            }
            if (!serviceLine) {
                doc.insertString(doc.getLength(), msg, keyWord);
            }
            // automaticky bude scrollovat v případě, že je scroll úplně dole
            var vScroll = scroll.getVerticalScrollBar();
            var autoScroll = (vScroll.getMaximum() - vScroll.getValue() - vScroll.getVisibleAmount()) < 32;
            if (autoScroll) {
                area.setCaretPosition(doc.getLength());
            }
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
