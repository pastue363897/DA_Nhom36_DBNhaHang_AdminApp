/**
 * Created on: 14:58:41 26 thg 4, 2020
 * @author Dinh Van Dung YKNB
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import database.BanAnDAO;
import database.HoaDonBanDatDAO;
import entites.BanAn;
import entites.HoaDonBanDat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;

public class BanDatManagerController implements Initializable{

  @FXML
  private TilePane dsBanDat;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    refreshNodes();
  }
  private void refreshNodes()
  {
      loadAllBanDat();
  }
  public void loadAllBanDat() {
    List<HoaDonBanDat> list = new HoaDonBanDatDAO().getDSTTBanDat();
    loadBanDat(list);
  }
  public void loadBanDat(List<HoaDonBanDat> list) {
    dsBanDat.getChildren().clear();
    Node node;
    FXMLLoader fx;
    for (HoaDonBanDat b : list) {
      try {
        fx = new FXMLLoader(getClass().getResource("/view/ItemBanDat.fxml"));
        node = fx.load();
        node.applyCss();
        ItemTTBanDatController ict = fx.getController();
        ict.loadData(b);
        ict.setBanDatMGCT(this);
        dsBanDat.getChildren().add(node);
        
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
  
}
