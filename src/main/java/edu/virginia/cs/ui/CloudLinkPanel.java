package edu.virginia.cs.ui;

import edu.virginia.cs.service.CloudFileService;
import edu.virginia.cs.service.impl.BoxFileService;
import edu.virginia.cs.ui.model.CloudLinkTableModelGenerator;
import edu.virginia.cs.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CloudLinkPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    private JPopupMenu popupMenu;
    private CloudFileService service;
    private JProgressBar pbar;

    public CloudLinkPanel() {
        service = new BoxFileService();
        try {
            this.image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("bg.png"));
            popupMenu = new JPopupMenu();
            pbar = new JProgressBar();
            pbar.setMinimum(0);
            pbar.setMaximum(100);
            this.setLayout(null);
            pbar.setLocation(26, 80);
            pbar.setSize(150, 12);
            pbar.setVisible(false);
            add(pbar);
            JMenuItem uploadItem = new JMenuItem("Upload");
            uploadItem.addActionListener((e) -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDragEnabled(true);
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int flag = chooser.showOpenDialog(null);
                if (flag == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    doUploadFile(file);
                }
            });
            JMenuItem closeItem = new JMenuItem("Close");
            closeItem.addActionListener((e) -> {
                service.saveRecords();
                System.exit(0);
            });

            popupMenu.add(uploadItem);
            popupMenu.add(closeItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() % 2 == 0) {
            final JTable table = new CloudLinkTable(new CloudLinkTableModelGenerator(service));
            table.setPreferredScrollableViewportSize(new Dimension(1000, 200));
            table.setRowSelectionAllowed(true);
            fitTableColumns(table);
            //Create the scroll pane and add the table to it.
            JScrollPane scrollPane = new JScrollPane(table);
            JOptionPane.showMessageDialog(CloudLinkPanel.this, scrollPane, "File Sharing History", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void fitTableColumns(JTable myTable) {
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();
        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(myTable, column.getIdentifier()
                            , false, false, -1, col).getPreferredSize().getWidth();
            for (int row = 0; rowCount > row; row++) {
                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
                        myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // 此行很重要
            column.setWidth(width + myTable.getIntercellSpacing().width);
        }
    }

    public void showPopupMenu(MouseEvent event) {
        if (event.isPopupTrigger())
            popupMenu.show(event.getComponent(),
                    event.getX(), event.getY());
    }

    public void doUploadFile(File file) {
        service.grantConnectionIfNeccessary();
        pbar.setVisible(true);
        new Thread() {
            public void run() {
                service.uploadFile(file.getAbsolutePath(), (numBytes, totalBytes) -> {
                            double percentComplete = (double) numBytes / totalBytes;
                            pbar.setValue((int) (percentComplete * pbar.getMaximum()));
                        }
                );
                pbar.setVisible(false);
                pbar.setValue(0);
            }
        }.start();
    }

    public Shape getShape() {
        return ImageUtil.getImageShape(image);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }
}
