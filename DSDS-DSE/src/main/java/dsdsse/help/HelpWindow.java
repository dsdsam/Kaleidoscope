package dsdsse.help;

import dsdsse.app.DsdsseEnvironment;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Admin on 7/1/2016.
 */
public class HelpWindow extends JDialog implements HyperlinkListener {
    JFrame parentFrame;
    JPanel helpPanel;
    JPanel bottomPanel;
    JPanel quickHelpPanel;
    JEditorPane htmlHelpPanel = null;
    JEditorPane htmlQuickHelpPanel = null;
    JScrollPane scroller;
    JViewport vp;

    public HelpWindow(JFrame parentFrame, String title) {
        super(parentFrame, title);

        this.parentFrame = parentFrame;
        setModal(true);
        getContentPane().setLayout(new java.awt.BorderLayout());

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                setVisible(false);
                //System.out.println("  Window Closing");
            }
        });

        helpPanel = initHelpPanel();
        bottomPanel = createBottomPanel();
// System.out.println("HelpWindow created.");
    }

    // -------------------------------------------------------------
    private JPanel initHelpPanel() {
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new java.awt.BorderLayout());
        helpPanel.setPreferredSize(new java.awt.Dimension(350, 495));

        try {
            URL helpURL = this.getClass().getResource("/Resources/htmls/what-is-dsdsse.html");

            htmlHelpPanel = new JEditorPane(helpURL);
            DsdsseEnvironment.setCompFontSize(htmlHelpPanel, java.awt.Font.PLAIN, 11);
            htmlHelpPanel.setEditable(false);
            htmlHelpPanel.setAutoscrolls(false);
            htmlHelpPanel.addHyperlinkListener(this);
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }

        scroller = new JScrollPane();
        vp = scroller.getViewport();
        return helpPanel;
    }

    // -------------------------------------------------------------
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 5, 5, 5));
// bottomPanel.setBackground( Color.red );
        bottomPanel.setPreferredSize(new java.awt.Dimension(330, 50));

        JButton closeButton = new JButton("Close");
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new java.awt.Dimension(80, 25));

        closeButton.addActionListener(new ActionListener() {
                                          public void actionPerformed(ActionEvent ae) {
                                              setVisible(false);
                                          }
                                      }
        );

        bottomPanel.add(closeButton);
        return bottomPanel;
    }

    // -------------------------------------------------------------
    public void setTitle(String title) {
        super.setTitle(title);
    }

    // -------------------------------------------------------------
    public void initHelpContent() {
        setTitle("Help");
        setResizable(true);
        vp.removeAll();
        helpPanel.removeAll();
        getContentPane().removeAll();
        pack();
        vp.add(htmlHelpPanel);
        helpPanel.add(scroller, java.awt.BorderLayout.CENTER);
        helpPanel.add(bottomPanel, java.awt.BorderLayout.SOUTH);
        getContentPane().add(helpPanel, BorderLayout.CENTER);
        pack();
        setResizable(false);

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        //    System.out.println("MCLNMedia.showQuickHelp: size++ "+
        //  size.height+"   "+size.width);
        setLocation(0, 0);
//  setLocation((scrSize.width - dlgSize.width) / 2,
        //            (scrSize.height - dlgSize.height) / 2);
        setVisible(true);
    }

    // -------------------------------------------------------------
    public void initQuickHelpContent(String helpType,
                                     JEditorPane htmlHelpPanel,
                                     JPanel bottomPanel) {
        setTitle("Quick Help");
        setResizable(true);
        vp.removeAll();
        helpPanel.removeAll();
        getContentPane().removeAll();
        pack();
        vp.add(htmlHelpPanel);
        helpPanel.add(scroller, java.awt.BorderLayout.CENTER);
        helpPanel.add(bottomPanel, java.awt.BorderLayout.SOUTH);
        getContentPane().add(helpPanel, BorderLayout.CENTER);
        pack();
        setResizable(false);

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        setLocation((scrSize.width - dlgSize.width) / 2,
                (scrSize.height - dlgSize.height) / 2);
        show();
    }
// -------------------------------------------------------------
//public void initQuickHelpContent();
// -------------------------------------------------------------
/*
    File file = new File("what-is-dsdsse.html");
    String relPath = file.getPath();
    System.out.println("path: " + relPath);
    String absPath = file.getAbsolutePath();
    System.out.println("path: " + absPath);
    String conPath = file.getCanonicalPath();
    System.out.println("path: " + conPath);
    URL helpURL = file.toURL();
    String strURL = helpURL.toExternalForm();
    System.out.println("URL: " + strURL);

    String uf = helpURL.getFile();
    System.out.println("URL: fa " + uf);

//    String up = helpURL.getPath();
//    System.out.println("URL: pa " + up);

    String upr = helpURL.getProtocol();
    System.out.println("URL: pr " + upr);

    int upo = helpURL.getPort();
    System.out.println("URL: po " + upo);

    String uho = helpURL.getHost();
    System.out.println("URL: ho" + uho);

//    String uqu = helpURL.getQuery();
//    System.out.println("URL: pr" + uqu);

    URL url = new URL("file:templ.html");
    html = new JEditorPane(helpURL);
ssssssssssssssssssssssssssssssssssssssssss
 http://WWW.DMEM.STRATH.AC.UK/~pball/
http://www.strath.ac.uk/Departments/DMEM/MSRG/simulate.html
*/

    //
// Notification of a change relative to a hyperlink.
//
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            linkActivated(e.getURL());
        }
    }

    //
// Follows the reference in an
// link.  The given url is the requested reference.
// By default this calls <a href="#setPage">setPage</a>,
// and if an exception is thrown the original previous
// document is restored and a beep sounded.  If an
// attempt was made to follow a link, but it represented
// a malformed url, this method will be called with a
// null argument.
//
// @param u the URL to follow
//
    protected void linkActivated(URL u) {
        Cursor c = htmlHelpPanel.getCursor();
        Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        htmlHelpPanel.setCursor(waitCursor);
        SwingUtilities.invokeLater(new PageLoader(u, c));
    }

    //
// temporary class that loads synchronously (although
// later than the request so that a cursor change
// can be done).
//
    class PageLoader implements Runnable {
        PageLoader(URL u, Cursor c) {
            url = u;
            cursor = c;
        }

        // --------------------------------
        public void run() {
            if (url == null) {
                // restore the original cursor
                htmlHelpPanel.setCursor(cursor);

                // PENDING(prinz) remove this hack when
                // automatic validation is activated.
                Container parent = htmlHelpPanel.getParent();
                parent.repaint();
            } else {
                Document doc = htmlHelpPanel.getDocument();
                try {
                    htmlHelpPanel.setPage(url);
                } catch (IOException ioe) {
                    htmlHelpPanel.setDocument(doc);
                    getToolkit().beep();
                } finally {
                    // schedule the cursor to revert after
                    // the paint has happended.
                    url = null;
                    SwingUtilities.invokeLater(this);
                }
            }
        }

        URL url;
        Cursor cursor;
    }

}
