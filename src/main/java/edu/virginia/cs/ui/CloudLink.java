package edu.virginia.cs.ui;

import edu.virginia.cs.ui.listener.FileDragListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CloudLink extends JWindow {
    private CloudLinkPanel panel;

    public CloudLink() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        setSize(200, 120);
        setLocationRelativeTo(null);
        panel = new CloudLinkPanel();
        add(panel);
        this.setShape(panel.getShape());
        new DropTarget(panel, new FileDragListener(panel));
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                panel.showPopupMenu(event);
            }

            public void mouseReleased(MouseEvent event) {
                panel.showPopupMenu(event);
            }

            public void mouseClicked(MouseEvent event) {
                panel.handleDoubleClick(event);
            }
        });
        this.addMouseMotionListener(
                new MouseMotionListener() {
                    private int mx, my;

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        mx = e.getXOnScreen();
                        my = e.getYOnScreen();
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        Point p = CloudLink.this.getLocation();
                        p.x += e.getXOnScreen() - mx;
                        p.y += e.getYOnScreen() - my;
                        mx = e.getXOnScreen();
                        my = e.getYOnScreen();
                        CloudLink.this.setLocation(p);
                    }
                }
        );
        setVisible(true);
    }
    public static void main(String[] args) {
        new CloudLink();
    }
}
