import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MarketTable extends JPanel {
    private JTable table;
    private String[] columnNames = {"Market", "Bid Price", "Bid Volume", "Ask Price", "Ask Volume"};
    private Object[][] data = {
            {"N/A", "-", "-", "-", "-"},
            {"N/A", "-", "-", "-", "-"},
            {"N/A", "-", "-", "-", "-"}
    };

    public MarketTable(){
        table = new JTable(data, columnNames);

        this.setLayout(new BorderLayout());
        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(table, BorderLayout.CENTER);
    }

    public void setData(Object[][] data){
        table.setModel(new DefaultTableModel(data, columnNames));
    }
}
