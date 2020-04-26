/**
 * Created on: 22:08:18 25 thg 4, 2020
 * 
 * @author Dinh Van Dung YKNB
 */

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class HomeManagerController implements Initializable{
  @FXML
  private Button btnBanDatManager;
  @FXML
  private Button btnBanAnManager;
  @FXML
  private Button btnMonAnManager;

  @FXML
  private AnchorPane banDat;
  @FXML
  private AnchorPane banAn;
  @FXML
  private AnchorPane monAn;
  
  
  public void navigationBar(ActionEvent e) {
    if (e.getSource() == btnBanDatManager) {
      banDat.setVisible(true);
      banAn.setVisible(false);
      monAn.setVisible(false);
    } else if (e.getSource() == btnBanAnManager) {
      banDat.setVisible(false);
      banAn.setVisible(true);
      monAn.setVisible(false);
    } else if (e.getSource() == btnMonAnManager) {
      banDat.setVisible(false);
      banAn.setVisible(false);
      monAn.setVisible(true);
    }
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    banDat.setVisible(true);
    banAn.setVisible(false);
    monAn.setVisible(false);
  }
}
