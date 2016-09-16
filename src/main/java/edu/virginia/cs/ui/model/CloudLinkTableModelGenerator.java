package edu.virginia.cs.ui.model;

import edu.virginia.cs.service.CloudFileService;
import edu.virginia.cs.service.CloudFileRecord;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CloudLinkTableModelGenerator {
    private Object[] columnNames = {"ID", "File Name", "Uploaded By", "Uploaded Time", "Share Link"};
    private List<CloudFileRecord> info;
    private Object[][] values;
    private CloudFileService service;
    public CloudLinkTableModelGenerator(CloudFileService service) {
        this.service = service;
        info = service.getUploadedItems();
        values = new Object[getRowCount()][getColumnCount()];
        for(int i = 0 ;i < values.length;i++){
            for(int j = 0; j < getColumnCount();j++){
                values[i][j] = this.getValueAt(i,j);
            }
        }
    }

    public DefaultTableModel generateTableModel(){
        return new DefaultTableModel(values,columnNames);
    }

    public CloudFileService getService() {
        return service;
    }

    public int getRowCount() {
        return info.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return info.get(rowIndex).getId();
        } else if (columnIndex == 1) {
            return info.get(rowIndex).getFileName();
        } else if (columnIndex == 2) {
            return info.get(rowIndex).getCreatedBy();
        } else if (columnIndex == 3) {
            return info.get(rowIndex).getCreatedTime();
        } else {
            return info.get(rowIndex).getUrl();
        }
    }
}
