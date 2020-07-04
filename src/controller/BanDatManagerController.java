/**
 * Created on: 14:58:41 26 thg 4, 2020
 * @author Dinh Van Dung YKNB
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import database.HoaDonBanDatDAO;
import entites.HoaDonBanDat;
import enums.ETinhTrangHoaDon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.util.StringConverter;

public class BanDatManagerController implements Initializable{

  @FXML
  private TilePane dsBanDat;
  
  @FXML
  private TextField txtTenKH;

  @FXML
  private Button btnTimBanDat;

  @FXML
  private DatePicker dpNgayDat;

  @FXML
  private TextField txtCMND;

  @FXML
  private TextField txtSoDT;
  
  @FXML
  private ComboBox<ETinhTrangHoaDon> cbTrangThaiBanDat;

  @FXML
  private Button btnShowAll;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    cbTrangThaiBanDat.setItems(FXCollections.observableArrayList(ETinhTrangHoaDon.DaThanhToan, ETinhTrangHoaDon.ChuaThanhToan, ETinhTrangHoaDon.DaHuy));
    cbTrangThaiBanDat.setConverter(new StringConverter<ETinhTrangHoaDon>() {
      
      @Override
      public String toString(ETinhTrangHoaDon object) {
        String lable = "Tất cả";
        switch (object) {
          case DaThanhToan:
            lable = "Đã thanh toán";
            break;
          case ChuaThanhToan:
            lable = "Chưa thanh toán";
            break;
          case DaHuy:
            lable = "Đã hủy";
            break;
          default:
            lable = "Tất cả";
            break;
        }
        return lable;
      }
      
      @Override
      public ETinhTrangHoaDon fromString(String string) {
        ETinhTrangHoaDon value = null;
        switch (string) {
          case "Đã thanh toán":
            value =  ETinhTrangHoaDon.DaThanhToan;
            break;
          case "Chưa thanh toán":
            value = ETinhTrangHoaDon.ChuaThanhToan;
            break;
          case "Đã hủy":
            value =  ETinhTrangHoaDon.DaHuy;
            break;
          default:
            value = null;
            break;
        }
        return value;
      }
    });
    refreshNodes();
  }
  private void refreshNodes()
  {
      loadAllBanDat();
  }
  public void loadAllBanDat() {
    List<HoaDonBanDat> list = new HoaDonBanDatDAO().getDSTTBanDatUnique();
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
  
  @FXML
  void showAll(ActionEvent event) {
		loadAllBanDat();
  }
  
  @FXML
  void timBanDat(ActionEvent event) {
	  LocalDate date = dpNgayDat.getValue();
	  String tenKH = txtTenKH.getText().trim();
	  String soCMND = txtCMND.getText().trim();
	  String soDT = txtSoDT.getText().trim();
	  ETinhTrangHoaDon tinhTrang = cbTrangThaiBanDat.getValue();
	  HoaDonBanDatDAO banDatDao = new HoaDonBanDatDAO();
	  List<HoaDonBanDat> lst = banDatDao.timHoaDon(date, tenKH, soCMND, soDT, tinhTrang);
	  loadBanDat(lst);
  }
  
}
