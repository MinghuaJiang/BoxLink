package edu.virginia.cs.ui;

import edu.virginia.cs.ui.model.CloudLinkTableModelGenerator;
import edu.virginia.cs.util.ClipboardUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Administrator on 2016/9/15.
 */
public class CloudLinkTable extends JTable {
    private JPopupMenu menu;
    public CloudLinkTable(CloudLinkTableModelGenerator model) {
        super(model.generateTableModel());
        menu = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("Copy Link");
        copyItem.addActionListener((e) -> {
            String url = (String) CloudLinkTable.this.getValueAt(this.getSelectedRow(), 4);
            ClipboardUtil.copyStringtoClipBoard(url);
        });
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener((e) -> {
            int row = this.getSelectedRow();
            model.getService().grantConnectionIfNeccessary();
            model.getService().deleteFile((String) CloudLinkTable.this.getValueAt(row, 0));
            ((DefaultTableModel)CloudLinkTable.this.getModel()).removeRow(row);
        });
        menu.add(copyItem);
        menu.add(deleteItem);
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                int row = CloudLinkTable.this.getSelectedRow();
                if (row != -1) {
                    CloudLinkTable.this.showPopupMenu(event);
                }
            }

            public void mouseReleased(MouseEvent event) {
                int row = CloudLinkTable.this.getSelectedRow();
                if (row != -1) {
                    CloudLinkTable.this.showPopupMenu(event);
                }
            }

        });
    }

    public void showPopupMenu(MouseEvent event) {
        if (event.isPopupTrigger())
            menu.show(event.getComponent(),
                    event.getX(), event.getY());
    }
}
