package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.PrimaryConf;
import enums.EChucVu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class HuongDanShortcutController implements Initializable{

    @FXML
    private TextArea txtAreaHelp;
    @FXML
    private Button btnDong;

    @FXML
    void dongGiaoDien(ActionEvent event) {
    	Stage currentStage = (Stage) btnDong.getScene().getWindow();
    	currentStage.close();
    }
    
    public void addWindowKeyEvent(Scene scene) {
    	scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ESCAPE) {
					Stage currentStage = (Stage) btnDong.getScene().getWindow();
			    	currentStage.close();
				}
			}
		});
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      if (PrimaryConf.currentAdmin != null && PrimaryConf.currentAdmin.getChucVu() == EChucVu.NguoiQuanLy) {
        txtAreaHelp.setText("Hướng dẫn phím tắt nhanh:\r\n" + 
            "CTRL + F1: Mở chức năng đặt bàn\r\n" + 
            "CTRL + F2: Mở chức năng quản lý bàn đặt\r\n" + 
            "CTRL + F3: Mở chức năng quản lý bàn ăn\r\n" + 
            "CTRL + F4: Mở chức năng quản lý món ăn\r\n" + 
            "CTRL + F5: Xem danh sách khách hàng\r\n" + 
            "CTRL + F6: Mở giao diện thống kê doanh thu theo ngày\r\n" + 
            "CTRL + F7: Mở giao diện thống kê về món ăn\r\n" + 
            "CTRL + F8: Mở giao diện thống kê về khách hàng");
      } else {
        txtAreaHelp.setText("Hướng dẫn phím tắt nhanh:\r\n" + 
            "CTRL + F1: Mở chức năng đặt bàn\r\n" + 
            "CTRL + F2: Mở chức năng quản lý bàn đặt\r\n" + 
            "CTRL + F5: Xem danh sách khách hàng");
      }
    }

}
