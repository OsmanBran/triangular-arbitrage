import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StrategyTable extends JPanel {
    private JTable table;
    private String[] columnNames = {"Description", "Value"};
    private Object[][] data = {
            {"N/A", "-"},
            {"N/A", "-"},
            {"N/A", "-"},
            {"N/A", "-"},
            {"N/A", "-"},
    };

    public StrategyTable(){
        table = new JTable(data, columnNames);
        table.setRowHeight(30);

        this.setLayout(new BorderLayout());
        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(table, BorderLayout.CENTER);
    }

    public void setData(Strategy strategyData){
        Object[] labels = strategyData.getLabels();
        Object[] values = strategyData.getValues();
        Object[][] tableData = new Object[5][2];
        for (int row = 0; row < 5; row++){
            tableData[row][0] = labels[row];
            tableData[row][1] = values[row];
        }

        table.setModel(new DefaultTableModel(tableData, columnNames));
    }
}
