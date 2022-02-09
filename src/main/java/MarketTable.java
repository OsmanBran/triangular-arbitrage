import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MarketTable extends JPanel {
    private final JTable table;
    private final String[] columnNames = {"Market", "Bid Price", "Bid Volume", "Ask Price", "Ask Volume"};
    private final Object[][] data = {
            {"N/A", "-", "-", "-", "-"},
            {"N/A", "-", "-", "-", "-"},
            {"N/A", "-", "-", "-", "-"}
    };

    public MarketTable() {
        table = new JTable(data, columnNames);
        table.setRowHeight(30);

        this.setLayout(new BorderLayout());
        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(table, BorderLayout.CENTER);
    }

    public void setData(Object[][] data) {
        table.setModel(new DefaultTableModel(data, columnNames));
    }
}
