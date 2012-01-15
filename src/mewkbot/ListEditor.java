package mewkbot;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Mewes
 */
public class ListEditor extends javax.swing.JPanel implements ListSelectionListener {

    private String entityName = "";

    public ListEditor() {
        this.initComponents();

        this.tableData.getSelectionModel().addListSelectionListener(this);
    }

    public void addColumns(String[] titles) {
        DefaultTableModel model = (DefaultTableModel) this.tableData.getModel();
        for (int i = 0; i < titles.length; i++) {
            TableColumn column = new TableColumn();
            column.setHeaderValue(titles[i]);
            model.addColumn(titles[i]);
        }
        this.tableData.doLayout();
    }

    public void addRow(String[] row) {
        DefaultTableModel model = (DefaultTableModel) this.tableData.getModel();
        model.addRow(row);
    }

    public void addRows(List<String[]> rows) {
        DefaultTableModel model = (DefaultTableModel) this.tableData.getModel();
        for (String[] row : rows) {
            model.addRow(row);
        }
    }

    /**
     * 
     * @param index
     * @return 
     */
    public String[] getRow(int index) {
        int columnCount = this.tableData.getColumnCount();
        String[] rowData = new String[columnCount];

        for (int i = 0; i < columnCount; i++) {
            rowData[i] = (String) this.tableData.getValueAt(index, i);
        }

        return rowData;
    }

    /**
     * 
     * @return 
     */
    public List<String[]> getRows() {
        int columnCount = this.tableData.getColumnCount();
        int rowCount = this.tableData.getRowCount();
        List<String[]> rowsData = new ArrayList<String[]>();

        for (int i = 0; i < rowCount; i++) {
            String[] rowData = new String[columnCount];

            for (int j = 0; j < columnCount; j++) {
                rowData[j] = (String) this.tableData.getValueAt(i, j);
            }

            rowsData.add(rowData);
        }

        return rowsData;
    }

    /**
     * 
     * @param index
     * @param data 
     */
    public void setRow(int index, String[] data) {
        int columnCount = this.tableData.getColumnCount();

        if (columnCount != data.length) {
            return;
        }

        for (int i = 0; i < columnCount; i++) {
            this.tableData.setValueAt(data[i], index, i);
        }
    }

    /**
     * 
     * @param index 
     */
    public void removeRow(int index) {
        DefaultTableModel model = (DefaultTableModel) this.tableData.getModel();
        model.removeRow(index);

        this.buttonEdit.setEnabled(false);
        this.buttonRemove.setEnabled(false);
    }

    /**
     * 
     * @return the entityName
     */
    public String getEntityName() {
        return this.entityName;
    }

    /**
     * 
     * @param entityName the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == this.tableData.getSelectionModel() && this.tableData.getRowSelectionAllowed() && e.getValueIsAdjusting()) {
            this.buttonEdit.setEnabled(true);
            this.buttonRemove.setEnabled(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonAdd = new javax.swing.JButton();
        buttonEdit = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        scrollPaneData = new javax.swing.JScrollPane();
        tableData = new javax.swing.JTable();

        buttonAdd.setText("Add");
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        buttonEdit.setText("Edit");
        buttonEdit.setEnabled(false);
        buttonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEditActionPerformed(evt);
            }
        });

        buttonRemove.setText("Remove");
        buttonRemove.setEnabled(false);
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });

        tableData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableData.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPaneData.setViewportView(tableData);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneData, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonRemove)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneData, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonRemove)
                        .addComponent(buttonEdit))
                    .addComponent(buttonAdd))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
    String[] data = TableInputOptionPane.showInputDialog("Add " + this.entityName, this, this.tableData);
    if (data != null) {
        this.addRow(data);
    }
}//GEN-LAST:event_buttonAddActionPerformed

private void buttonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEditActionPerformed
    int selectedRow = this.tableData.getSelectedRow();
    if (selectedRow >= 0) {
        String[] data = TableInputOptionPane.showInputDialog("Edit " + this.entityName, this, this.tableData, selectedRow);
        if (data != null) {
            this.setRow(selectedRow, data);
        }
    }
}//GEN-LAST:event_buttonEditActionPerformed

private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
    int selectedRow = this.tableData.getSelectedRow();
    if (selectedRow >= 0) {
        this.removeRow(selectedRow);
    }
}//GEN-LAST:event_buttonRemoveActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonEdit;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JScrollPane scrollPaneData;
    private javax.swing.JTable tableData;
    // End of variables declaration//GEN-END:variables

    private static class TableInputOptionPane extends JOptionPane {

        public static String[] showInputDialog(String title, ListEditor parent, JTable table) {
            return TableInputOptionPane.showInputDialog(title, parent, table, -1);
        }

        public static String[] showInputDialog(String title, ListEditor parent, JTable table, int rowIndex) {

            class GetData extends JDialog implements ActionListener {

                String[] data = null;
                JTextField[] textFields;
                JTable table;
                JButton buttonCancel = new JButton("Cancel");
                JButton buttonOk = new JButton("OK");

                /**
                 * 
                 * @param title 
                 * @param parent 
                 * @param table 
                 */
                public GetData(String title, ListEditor parent, JTable table, int rowIndex) {
                    int columnCount = table.getColumnCount();

                    this.textFields = new JTextField[columnCount];

                    this.table = table;
                    this.buttonCancel.addActionListener(this);
                    this.buttonOk.addActionListener(this);

                    this.setTitle(title);
                    this.setModal(true);
                    this.setResizable(false);
                    this.setLocationRelativeTo(parent);

                    JPanel panelGrid = new JPanel(new SpringLayout());

                    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this.getContentPane());
                    this.getContentPane().setLayout(layout);
                    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(buttonOk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(buttonCancel)).addComponent(panelGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)).addContainerGap()));
                    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(panelGrid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(buttonCancel).addComponent(buttonOk)).addContainerGap()));

                    String[] rowData = null;
                    if (rowIndex >= 0) {
                        rowData = parent.getRow(rowIndex);
                    }

                    for (int i = 0; i < columnCount; i++) {
                        String columnName = (String) table.getColumnModel().getColumn(i).getHeaderValue();

                        panelGrid.add(new JLabel(columnName));

                        this.textFields[i] = new JTextField();
                        if (rowIndex >= 0) {
                            this.textFields[i].setText(rowData[i]);
                        }
                        panelGrid.add(this.textFields[i]);
                    }

                    this.makeCompactGrid(panelGrid, columnCount, 2, 3, 3, 3, 3);

                    this.pack();
                    this.setVisible(true);
                }

                /**
                 * 
                 * @param ae 
                 */
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (ae.getSource() == this.buttonOk) {
                        int columnCount = table.getColumnCount();
                        this.data = new String[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            this.data[i] = this.textFields[i].getText();
                        }
                    } else {
                        this.data = null;
                    }
                    
                    this.dispose();
                }

                /**
                 * 
                 * @return data
                 */
                public String[] getData() {
                    return this.data;
                }

                /**
                 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
                 * 
                 * Aligns the first <code>rows</code> * <code>cols</code>
                 * components of <code>parent</code> in
                 * a grid. Each component in a column is as wide as the maximum
                 * preferred width of the components in that column;
                 * height is similarly determined for each row.
                 * The parent is made just big enough to fit them all.
                 *
                 * @param rows number of rows
                 * @param cols number of columns
                 * @param initialX x location to start the grid at
                 * @param initialY y location to start the grid at
                 * @param xPad x padding between cells
                 * @param yPad y padding between cells
                 */
                private void makeCompactGrid(Container parent,
                        int rows, int cols,
                        int initialX, int initialY,
                        int xPad, int yPad) {
                    SpringLayout layout;
                    try {
                        layout = (SpringLayout) parent.getLayout();
                    } catch (ClassCastException exc) {
                        System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
                        return;
                    }

                    //Align all cells in each column and make them the same width.
                    Spring x = Spring.constant(initialX);
                    for (int c = 0; c < cols; c++) {
                        Spring width = Spring.constant(0);
                        for (int r = 0; r < rows; r++) {
                            width = Spring.max(width,
                                    getConstraintsForCell(r, c, parent, cols).
                                    getWidth());
                        }
                        for (int r = 0; r < rows; r++) {
                            SpringLayout.Constraints constraints =
                                    getConstraintsForCell(r, c, parent, cols);
                            constraints.setX(x);
                            constraints.setWidth(width);
                        }
                        x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
                    }

                    //Align all cells in each row and make them the same height.
                    Spring y = Spring.constant(initialY);
                    for (int r = 0; r < rows; r++) {
                        Spring height = Spring.constant(0);
                        for (int c = 0; c < cols; c++) {
                            height = Spring.max(height,
                                    getConstraintsForCell(r, c, parent, cols).getHeight());
                        }
                        for (int c = 0; c < cols; c++) {
                            SpringLayout.Constraints constraints =
                                    getConstraintsForCell(r, c, parent, cols);
                            constraints.setY(y);
                            constraints.setHeight(height);
                        }
                        y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
                    }

                    //Set the parent's size.
                    SpringLayout.Constraints pCons = layout.getConstraints(parent);
                    pCons.setConstraint(SpringLayout.SOUTH, y);
                    pCons.setConstraint(SpringLayout.EAST, x);
                }

                /* 
                 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
                 * 
                 * Used by makeCompactGrid. 
                 */
                private SpringLayout.Constraints getConstraintsForCell(
                        int row, int col,
                        Container parent,
                        int cols) {
                    SpringLayout layout = (SpringLayout) parent.getLayout();
                    Component c = parent.getComponent(row * cols + col);
                    return layout.getConstraints(c);
                }
            }

            return new GetData(title, parent, table, rowIndex).getData();
        }
    }
}