/**
 * Created on: 22:08:18 25 thg 4, 2020
 * 
 * @author Dinh Van Dung YKNB
 */

package controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import database.MonAnDAO;
import entites.MonAn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class HomeManager implements Initializable {
  @FXML
  private Button btnThemMA;
  @FXML
  private TextField txtTenMonAn, txtSoLuongNguoiMA, txtGiaTienMA;
  @FXML
  private TextArea txtMoTaMA;
  @FXML
  private ImageView imvHinhAnhMA;
  @FXML
  private TableView<MonAn> danhSachMonAn;
  @FXML
  private TableColumn<MonAn, String> hinhAnhColumn;
  @FXML
  private TableColumn<MonAn, String> idColumn;
  @FXML
  private TableColumn<MonAn, String> tenMonAnColumn;
  @FXML
  private TableColumn<MonAn, String> moTaColumn;
  @FXML
  private TableColumn<MonAn, Integer> soLuongNguoiColumn;
  @FXML
  private TableColumn<MonAn, Long> giaTienColumn;
  
  private ObservableList<MonAn> listMonAn;
  
  private static String hinhAnh = "/images/icon-food-and-drink-hd-png-download.png";
  
  public void themMonAn(ActionEvent e) {
    String tenMonAn = txtTenMonAn.getText();
    String soLuongNguoiString = txtSoLuongNguoiMA.getText();
    String giaTienString = txtGiaTienMA.getText();
    String moTa = txtMoTaMA.getText();
    try {
      int soLuongNguoi = Integer.parseInt(soLuongNguoiString);
      long giaTien = Long.parseLong(giaTienString);
      
      MonAn monAn = new MonAn("MA003", tenMonAn, moTa, soLuongNguoi, hinhAnh, giaTien, false, false);
      
      new MonAnDAO().save(monAn);
      loadMonAn();
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Thêm bàn thành công");
      alert.setContentText("Đã thêm bàn vào hệ thống");
      alert.show();
    } catch (HibernateException he) {
      he.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Thêm bàn thất bại");
      alert.setContentText("Đã xảy ra sự cố hãy thử lại");
      alert.show();
    } catch (Exception ex) {
      ex.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Thêm bàn thất bại");
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
      Image image = new Image(file.toURI().toString(), 149, 120, false, true);
      imvHinhAnhMA.setImage(image);
    }
  }
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    hinhAnhColumn.setCellValueFactory(new PropertyValueFactory<MonAn, String>("hinhAnhMA"));
    idColumn.setCellValueFactory(new PropertyValueFactory<MonAn, String>("idMA"));
    tenMonAnColumn.setCellValueFactory(new PropertyValueFactory<MonAn, String>("tenMA"));
    moTaColumn.setCellValueFactory(new PropertyValueFactory<MonAn, String>("moTaMA"));
    soLuongNguoiColumn.setCellValueFactory(new PropertyValueFactory<MonAn, Integer>("soLuongNguoi"));
    giaTienColumn.setCellValueFactory(new PropertyValueFactory<MonAn, Long>("giaTien"));
    loadMonAn();
  }
  
  public void loadMonAn() {
    List<MonAn> list = new MonAnDAO().getAll();
    listMonAn = FXCollections.observableArrayList(list);
    danhSachMonAn.setItems(listMonAn);
  }
}
