package tableviewfxmlsample1;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author ericjbruno
 */
public class SampleController implements Initializable {
    public TableViewFXMLSample1 tableViewFXMLSample1;

    public SampleController() {
        tableViewFXMLSample1 = new TableViewFXMLSample1();
    }
    
    public class Item {
        public SimpleLongProperty id = new SimpleLongProperty();
        public SimpleStringProperty name = new SimpleStringProperty();
        public SimpleIntegerProperty qty = new SimpleIntegerProperty();
        public SimpleStringProperty price = new SimpleStringProperty();
        
        public Long getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public String getPrice() {
            return price.get();
        }

        public Integer getQty() {
            return qty.get();
        }
    }

    // The table and columns
    @FXML
    TableView<Item> itemTbl;

    @FXML
    TableColumn itemIdCol;
    @FXML
    TableColumn itemNameCol;
    @FXML
    TableColumn itemQtyCol;
    @FXML
    TableColumn itemPriceCol;

    // The table's data
    ObservableList<Item> data;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up the table data
        itemIdCol.setCellValueFactory(
            new PropertyValueFactory<Item,Long>("id")
        );
        itemNameCol.setCellValueFactory(
            new PropertyValueFactory<Item,String>("name")
        );
        itemQtyCol.setCellValueFactory(
            new PropertyValueFactory<Item,Integer>("qty")
        );
        itemPriceCol.setCellValueFactory(
            new PropertyValueFactory<Item,String>("price")
        );

        data = FXCollections.observableArrayList();
        itemTbl.setItems(data);
        
        Connection c = tableViewFXMLSample1.connectToDatabaseOrDie();
        populateListOfTopics(c);
    }    
    private void populateListOfTopics(Connection conn)
  {
    try 
    {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT id, subject FROM blogs ORDER BY id");
            while ( rs.next() )
            {
                amendTable(rs);
            }
            rs.close();
        }
    }
    catch (SQLException se) {
      System.err.println("Threw a SQLException creating the list of blogs.");
      System.err.println(se.getMessage());
    }
  }

    static long nextId = 1;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        amendTable(null);
    }
    private void amendTable(ResultSet rs){
        Item item = new Item();
        if(rs == null){
        item.id.setValue(nextId++);
        item.name.setValue("Item Number " + item.id.getValue());
        item.qty.setValue(10);
        Float price = new Float(5.00 + (float)item.id.getValue());
        item.price.setValue( price.toString());
        }else{
            try {
                item.id.setValue(rs.getInt("id"));
                item.name.setValue("Item Number " + rs.getString("subject"));
                item.qty.setValue(10);
                Float price = new Float(5.00 + (float)item.id.getValue());
                item.price.setValue( price.toString());
            } catch (SQLException ex) {
                Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        data.add(item);
    }
}
