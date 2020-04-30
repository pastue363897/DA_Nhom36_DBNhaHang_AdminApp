/**
 * Created on: 14:44:24 26 thg 4, 2020
 * 
 * @author Dinh Van Dung YKNB
 */

package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import database.MonAnDAO;
import entites.MonAn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;

public class MonAnManagerController implements Initializable {
  
  private String idMonAnUpdate;
  @FXML
  private TextField txtTenMonAn, txtSoLuongNguoiMA, txtGiaTienMA;
  @FXML
  private TextArea txtMoTaMA;
  @FXML
  private ImageView imvHinhAnhMA;
  
  @FXML
  private FlowPane dsMonAn;
  
  private static String hinhAnh = "/images/food-view.png";
  
  
  public void setIdMonAnUpdate(String idMonAnUpdate) {
    this.idMonAnUpdate = idMonAnUpdate;
  }

  public TextField getTxtTenMonAn() {
    return txtTenMonAn;
  }

  public TextField getTxtSoLuongNguoiMA() {
    return txtSoLuongNguoiMA;
  }

  public TextField getTxtGiaTienMA() {
    return txtGiaTienMA;
  }

  public TextArea getTxtMoTaMA() {
    return txtMoTaMA;
  }

  public ImageView getImvHinhAnhMA() {
    return imvHinhAnhMA;
  }

  public void themMonAn(ActionEvent e) {
    String tenMonAn = txtTenMonAn.getText();
    String soLuongNguoiString = txtSoLuongNguoiMA.getText();
    String giaTienString = txtGiaTienMA.getText();
    String moTa = txtMoTaMA.getText();
    try {
      int soLuongNguoi = Integer.parseInt(soLuongNguoiString);
      long giaTien = Long.parseLong(giaTienString);
      
      MonAn monAn = new MonAn(tenMonAn, moTa, soLuongNguoi, hinhAnh, giaTien);
      new MonAnDAO().addMonAn(monAn);
      loadAllMonAn();
      xoaInput();
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Thêm món ăn thành công");
      alert.setContentText("Đã thêm món ăn vào hệ thống");
      alert.show();
    } catch (HibernateException he) {
      he.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Thêm món ăn thất bại");
      alert.setContentText("Đã xảy ra sự cố hãy thử lại");
      alert.show();
    } catch (Exception ex) {
      ex.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Thêm món ăn thất bại");
      alert.setContentText("Đã xảy ra sự cố hãy thử lại");
      alert.show();
    }
  }
  
  public void suaMonAn(ActionEvent e) {
    String tenMonAn = txtTenMonAn.getText();
    String soLuongNguoiString = txtSoLuongNguoiMA.getText();
    String giaTienString = txtGiaTienMA.getText();
    String moTa = txtMoTaMA.getText();
    try {
      int soLuongNguoi = Integer.parseInt(soLuongNguoiString);
      long giaTien = Long.parseLong(giaTienString);
      
      MonAn monAn = new MonAn(idMonAnUpdate, tenMonAn, moTa,
          soLuongNguoi, hinhAnh, giaTien, false, false);
      
      new MonAnDAO().update(monAn);
      loadAllMonAn();
      xoaInput();
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Sửa món ăn bàn thành công");
      alert.setContentText("Đã sửa thông tin món ăn vào hệ thống");
      alert.show();
    } catch (HibernateException he) {
      he.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Sửa món ăn thất bại");
      alert.setContentText("Đã xảy ra sự cố hãy thử lại");
      alert.show();
    } catch (Exception ex) {
      ex.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Sửa món ăn thất bại");
      alert.setContentText("Đã xảy ra sự cố hãy thử lại");
      alert.show();
    }
  }
  
  public void chooseImage(MouseEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose image");
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("image", "*.jpg", "*.png");
    fileChooser.getExtensionFilters().add(filter);
    File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
    if (file != null) {
      hinhAnh = file.getPath();
      Image image = new Image(file.toURI().toString(), 200, 150, false, true);
      imvHinhAnhMA.setImage(image);
    }
  }
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loadAllMonAn();
  }
  public void loadAllMonAn() {
    List<MonAn> list = new MonAnDAO().getAll();
    loadMonAn(list);
  }
  
  public void loadMonAn(List<MonAn> list) {
    dsMonAn.getChildren().clear();
    Node node;
    FXMLLoader fx;
    for (MonAn ma : list) {
      try {
        fx = new FXMLLoader(getClass().getResource("/view/ItemMonAn.fxml"));
        node = fx.load();
        node.applyCss();
        ItemMonAnController ict = fx.getController();
        ict.loadData(ma);
        ict.setMonAnMGCT(this);
        dsMonAn.getChildren().add(node);
        
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
  
  public void xoaInput() {
    idMonAnUpdate = "";
    txtTenMonAn.setText("");
    txtMoTaMA.setText("");
    txtSoLuongNguoiMA.setText("");
    txtGiaTienMA.setText("");
    txtTenMonAn.requestFocus();
    hinhAnh = "/images/food-view.png";
    Image image = new Image("file:./src/images/food-view.png", 200, 150, false, true);
    imvHinhAnhMA.setImage(image);
  }
}
