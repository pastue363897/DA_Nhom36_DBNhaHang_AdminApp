/**
 * Created on: 14:44:24 26 thg 4, 2020
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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entites.MonAn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class MonAnManagerController implements Initializable {
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
  @FXML
  private TableColumn<MonAn, Void> huyMAColumn;
  
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
      
      MonAn monAn = new MonAn(danhSachMonAn.getSelectionModel().getSelectedItem().getMaMA(), tenMonAn, moTa,
          soLuongNguoi, hinhAnh, giaTien, false, false);
      
      new MonAnDAO().update(monAn);
      loadMonAn();
      xoaInput();
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Sửa món ăn bàn thành công");
      alert.setContentText("Đã thêm món ăn vào hệ thống");
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
      Image image = new Image(file.toURI().toString(), 149, 120, false, true);
      imvHinhAnhMA.setImage(image);
    }
  }
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    hinhAnhColumn.setCellValueFactory(new PropertyValueFactory<MonAn, String>("hinhAnhMA"));
    idColumn.setCellValueFactory(new PropertyValueFactory<MonAn, String>("maMA"));
    tenMonAnColumn.setCellValueFactory(new PropertyValueFactory<MonAn, String>("tenMA"));
    moTaColumn.setCellValueFactory(new PropertyValueFactory<MonAn, String>("moTaMA"));
    soLuongNguoiColumn.setCellValueFactory(new PropertyValueFactory<MonAn, Integer>("soLuongNguoi"));
    giaTienColumn.setCellValueFactory(new PropertyValueFactory<MonAn, Long>("giaTien"));
    huyMAColumn.setCellFactory(param -> new TableCell<MonAn, Void>() {
      private Button btnHuy = new Button("Hủy");
      {
        btnHuy.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            try {
              MonAn monAn = danhSachMonAn.getSelectionModel().getSelectedItem();
              new MonAnDAO().delete(monAn);
              loadMonAn();
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Xóa món ăn bàn thành công");
              alert.setContentText("Đã xóa món ăn trong hệ thống");
              alert.show();
            } catch (HibernateException he) {
              he.printStackTrace();
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Xóa món ăn thất bại");
              alert.setContentText("Đã xảy ra sự cố hãy thử lại");
              alert.show();
            } catch (Exception ex) {
              ex.printStackTrace();
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Xóa món ăn thất bại");
              alert.setContentText("Đã xảy ra sự cố hãy thử lại");
              alert.show();
            }
          }
        });
      }
      
      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        btnHuy.setTextFill(Color.WHITE);
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE);
        icon.setSize("18");
        icon.setFill(Color.WHITE);
        btnHuy.setGraphic(icon);
        setGraphic(empty ? null : btnHuy);
      }
    });
    loadMonAn();
  }
  
  public void loadMonAn() {
    List<MonAn> list = new MonAnDAO().getAll();
    listMonAn = FXCollections.observableArrayList(list);
    danhSachMonAn.setItems(listMonAn);
  }
  
  public void chooseMonAn(MouseEvent event) {
    MonAn monAn = danhSachMonAn.getSelectionModel().getSelectedItem();
    txtTenMonAn.setText(monAn.getTenMA());
    txtMoTaMA.setText(monAn.getMoTaMA());
    txtSoLuongNguoiMA.setText(String.valueOf(monAn.getSoLuongNguoi()));
    txtGiaTienMA.setText(String.valueOf(monAn.getGiaTien()));
  }
  
  public void xoaInput() {
    txtTenMonAn.setText("");
    txtMoTaMA.setText("");
    txtSoLuongNguoiMA.setText("");
    txtGiaTienMA.setText("");
    txtTenMonAn.requestFocus();
  }
}
