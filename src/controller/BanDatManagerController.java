/**
 * Created on: 14:58:41 26 thg 4, 2020
 * @author Dinh Van Dung YKNB
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class BanDatManagerController implements Initializable{

  @FXML
  private VBox danhSachBanDat;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    refreshNodes();
  }
  private void refreshNodes()
  {
      danhSachBanDat.getChildren().clear();
      
      Node[] nodes = new  Node[15];
      
      for(int i = 0; i<10; i++)
      {
          try {
              nodes[i] = (Node)FXMLLoader.load(getClass().getResource("/view/ItemBanDat.fxml"));
              danhSachBanDat.getChildren().add(nodes[i]);
              
          } catch (IOException ex) {
              ex.printStackTrace();
          }
         
      }  
  }
  
}
