/**
 * Created on: 01:58:53 1 thg 5, 2020
 * 
 * @author Dinh Van Dung YKNB
 */

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import database.BanAnDAO;
import entites.BanAn;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class ItemBanAnController implements Initializable {
  
  @FXML
  private Pane panBlur;
  @FXML
  private Button btnSuaBanAn;
  @FXML
  private Button btnHuyBanAn;
  @FXML
  private Label lblKySoBA, lblMoTaBA, lblGiaTienBA, lblSoLuongGheBA;
  @FXML
  private ImageView hinhAnhBanAn;
  
  private BanAnManagerController banAnMGCT;
  private BanAn ttBanAn;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    panBlur.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
      if (newValue) {
        btnSuaBanAn.setVisible(true);
        btnHuyBanAn.setVisible(true);
      } else {
        btnSuaBanAn.setVisible(false);
        btnHuyBanAn.setVisible(false);
      }
    });
  }
  
  public void loadData(BanAn b) {
    lblKySoBA.setText(b.getKySoBA().toUpperCase());
    lblMoTaBA.setText(b.getMotaBA());
    lblSoLuongGheBA.setText(String.valueOf(b.getSoLuongGhe()));
    lblGiaTienBA.setText(String.valueOf(b.getGiaTien()) + " Đ");
    Image image = new Image("file:./src" + b.getHinhAnh(), 200, 150, false, true);
    hinhAnhBanAn.setImage(image);
    ttBanAn = b;
  }
  
  public void setMonAnMGCT(BanAnManagerController banAnMGCT) {
    this.banAnMGCT = banAnMGCT;
  }
  
  public void suaBanAn(ActionEvent e) {
    banAnMGCT.getTxtKySoBanAn().setText(ttBanAn.getKySoBA());
    banAnMGCT.getTxtMoTaBA().setText(ttBanAn.getMotaBA());
    banAnMGCT.getTxtSoLuongGheBA().setText(String.valueOf(ttBanAn.getSoLuongGhe()));
    banAnMGCT.getTxtGiaTienBA().setText(String.valueOf(ttBanAn.getGiaTien()));
    banAnMGCT.getImvHinhAnhBA().setImage(new Image("file:./src" + ttBanAn.getHinhAnh(), 200, 165, false, true));
    banAnMGCT.setIdBanAnUpdate(ttBanAn.getMaBA());
  }
  
  public void huyBanAn(ActionEvent e) {
    banAnMGCT.xoaInput();
    try {
      new BanAnDAO().delete(ttBanAn);
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Xóa bàn thành công");
      alert.setContentText("Đã xóa bàn ăn trong hệ thống");
      alert.show();
      banAnMGCT.loadAllBanAn();
    } catch (HibernateException ex1) {
      ex1.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Xóa bàn thất bại");
      alert.setContentText("Đã xảy ra sự cố hãy thử lại");
      alert.show();
    } catch (Exception ex) {
      ex.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Xóa bàn thất bại");
      alert.setContentText("Đã xảy ra sự cố hãy thử lại");
      alert.show();
    }
  }
}
