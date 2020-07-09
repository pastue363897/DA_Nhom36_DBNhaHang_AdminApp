/**
 * Created on: 22:08:18 25 thg 4, 2020
 * 
 * @author Dinh Van Dung YKNB, Ta Khanh Hoang
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.PrimaryConf;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeManagerController implements Initializable {

  @FXML
  private Label lblMenuThoat;
	@FXML
	private Parent banDat;
	@FXML
	private BanDatManagerController banDatController;
	@FXML
	private Parent banAn;
	@FXML
	private BanAnManagerController banAnController;
	@FXML
	private Parent monAn;
	@FXML
	private MonAnManagerController monAnController;
	@FXML
	private Parent datBan;
	@FXML
	private DatBanKhachVangLaiController datBanController;
	@FXML
    private MenuItem menuDatBanVangLai;
    @FXML
    private MenuItem menuTimKiemBanDat;
    @FXML
    private MenuItem menuXemChiTiet;
    @FXML
    private MenuItem menuThemBanAn;
    @FXML
    private MenuItem menuTimKiemBanAn;
    @FXML
    private MenuItem menuThemMonAn;
    @FXML
    private MenuItem menuTimKiemMonAn;
    @FXML
    private MenuItem menuXuatDSMonAn;
    @FXML
    private MenuItem menuTaoKhachHang;
    @FXML
    private MenuItem menuXemDanhSachKhachHang;
    @FXML
    private MenuItem menuTKDTBanDatTheoNgay;
    @FXML
    private MenuItem menuTKMonAn;
    @FXML
    private MenuItem menuTKKhachHang;
    @FXML
    private Label lblMenuHuongDanShortcut;
	
	public static Stage primaryStage;

	/*public void datBanKhachVangLai(ActionEvent e) {
		Parent root;
		try {
			FXMLLoader fx = new FXMLLoader(getClass().getResource("/view/DatBanKhachVangLaiManager.fxml"));
			root = fx.load();
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(HomeManagerController.primaryStage);
			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng - Đặt bàn cho khách vãng lai");
			Scene scene = new Scene(root);
			DatBanKhachVangLaiController it = fx.getController();
			it.setBanDatMGCT(banDatController);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.centerOnScreen();
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}*/

	public void signOut(MouseEvent e) {
		PrimaryConf.currentAdmin = null;
		Stage current = (Stage) lblMenuThoat.getScene().getWindow();
		current.close();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng");
			Scene scene = new Scene(root);
			stage.setWidth(700);
			stage.setHeight(485);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		banDat.setVisible(true);
		banAn.setVisible(false);
		monAn.setVisible(false);
		datBan.setVisible(false);
		datBanController.setBanDatMGCT(banDatController);
		datBanController.setHostMGCT(this);
	}
	
    public void addWindowKeyEvent(Scene scene) {
    	scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
    		final KeyCombination keyDatBan = new KeyCodeCombination(KeyCode.F1, KeyCombination.CONTROL_DOWN);
    		final KeyCombination keyBanDat = new KeyCodeCombination(KeyCode.F2, KeyCombination.CONTROL_DOWN);
    		final KeyCombination keyBanAn = new KeyCodeCombination(KeyCode.F3, KeyCombination.CONTROL_DOWN);
    		final KeyCombination keyMonAn = new KeyCodeCombination(KeyCode.F4, KeyCombination.CONTROL_DOWN);
    		final KeyCombination keyXemDSKH = new KeyCodeCombination(KeyCode.F5, KeyCombination.CONTROL_DOWN);
    		final KeyCombination keyThongKeDoanhThuNgay = new KeyCodeCombination(KeyCode.F6, KeyCombination.CONTROL_DOWN);
    		final KeyCombination keyThongKeDoanhThuMon = new KeyCodeCombination(KeyCode.F7, KeyCombination.CONTROL_DOWN);
    		final KeyCombination keyThongKeDoanhThuKH = new KeyCodeCombination(KeyCode.F8, KeyCombination.CONTROL_DOWN);
    		
			@Override
			public void handle(KeyEvent event) {
				if(keyDatBan.match(event)) {
					showDatBanKhachVangLai();
				}
				else if(keyBanDat.match(event)) {
					showQLBanDat();
				}
				else if(keyBanAn.match(event)) {
					showQLBanAn();
				}
				else if(keyMonAn.match(event)) {
					showQLMonAn();
				}
				else if(keyXemDSKH.match(event)) {
					showXemDanhSachKhachHang();
				}
				else if(keyThongKeDoanhThuNgay.match(event)) {
					showThongKe(1);
				}
				else if(keyThongKeDoanhThuMon.match(event)) {
					showThongKe(2);
				}
				else if(keyThongKeDoanhThuKH.match(event)) {
					showThongKe(3);
				}
				else if(event.getCode() == KeyCode.F1) {
					showHuongDanShortcut();
				}
			}
    		
		});
    }
    
    public void huongDanShortcut(MouseEvent e) {
    	showHuongDanShortcut();
    }
    
    void showHuongDanShortcut() {
    	Parent root;
		try {
			FXMLLoader fload = new FXMLLoader(getClass().getResource("/view/HuongDanShortcut.fxml"));
			root = fload.load();
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(HomeManagerController.primaryStage);
			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng - Hướng dẫn");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.centerOnScreen();
			HuongDanShortcutController ctr = fload.getController();
			ctr.addWindowKeyEvent(scene);
			stage.showAndWait();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    void showQLBanAn() {
    	banDat.setVisible(false);
		banAn.setVisible(true);
		monAn.setVisible(false);
		datBan.setVisible(false);
    }
	
	@FXML
    void menuQLBanAn(ActionEvent event) {
		if(event.getSource() == menuThemBanAn)
			banAnController.getTabPane().getSelectionModel().select(banAnController.getTabThem());
		else if(event.getSource() == menuTimKiemBanAn)
			banAnController.getTabPane().getSelectionModel().select(banAnController.getTabTimKiem());
		showQLBanAn();
    }
	
	void showQLBanDat() {
		banDatController.loadAllBanDat();
    	banDat.setVisible(true);
		banAn.setVisible(false);
		monAn.setVisible(false);
		datBan.setVisible(false);
	}
	
	void showDatBanKhachVangLai() {
		if(datBanController.daDat == 0) {
    	    datBanController.reload();
    	    datBanController.daDat = 1;
    	  }
    	  else if(datBanController.daDat == 1) {
    	    datBanController.showAllMon();
    	    datBanController.showAllBan();
    	  }
    		banDat.setVisible(false);
    		banAn.setVisible(false);
    		monAn.setVisible(false);
    		datBan.setVisible(true);
	}

    @FXML
    void menuQLBanDat(ActionEvent event) {
    	if(event.getSource() == menuDatBanVangLai) {
    		showDatBanKhachVangLai();
    	}
    	else {
    		showQLBanDat();	
    	}
    }
    
    void showXemDanhSachKhachHang() {
    	Parent root;
		FXMLLoader fload;
		try {
			fload = new FXMLLoader(getClass().getResource("/view/QuanLyKhachHangManager.fxml"));
			root = fload.load();
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(HomeManagerController.primaryStage);
			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng - Quản lý khách hàng");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.centerOnScreen();
			stage.show();
			QuanLyKhachHangController.primaryStage = stage;
			QuanLyKhachHangController qlkhct = fload.getController();
			qlkhct.setDatBanKHController(datBanController);
			qlkhct.setHomeController(this);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }

    @FXML
    void menuQLKhachHang(ActionEvent event) {
    	if(event.getSource() == menuXemDanhSachKhachHang) {
    		showXemDanhSachKhachHang();
    	}
    	else if(event.getSource() == menuTaoKhachHang) {
    		Parent root;
    		try {
    			root = FXMLLoader.load(getClass().getResource("/view/TaoThongTinKhachHangManager.fxml"));
    			Stage stage = new Stage();
    			stage.setResizable(false);
    			stage.initModality(Modality.WINDOW_MODAL);
    			stage.initOwner(HomeManagerController.primaryStage);
    			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng - Thêm khách hàng mới");
    			Scene scene = new Scene(root);
    			stage.setScene(scene);
    			stage.sizeToScene();
    			stage.centerOnScreen();
    			stage.show();
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    	}
    }
    
    void showQLMonAn() {
    	banDat.setVisible(false);
		banAn.setVisible(false);
		monAn.setVisible(true);
		datBan.setVisible(false);
    }

    @FXML
    void menuQLMonAn(ActionEvent event) {
    	if(event.getSource() == menuXuatDSMonAn) {
    		monAnController.xuatMonAnExcel();
    		return;
    	}
    	if(event.getSource() == menuThemMonAn)
			monAnController.getTabPane().getSelectionModel().select(monAnController.getTabThem());
		else if(event.getSource() == menuTimKiemMonAn)
			monAnController.getTabPane().getSelectionModel().select(monAnController.getTabTimKiem());
    	showQLMonAn();
    }
    
    void showThongKe(int i) {
    	switch(i) {
    	case 1:
    	{
    		Parent root;
    		try {
    			root = FXMLLoader.load(getClass().getResource("/view/ThongKeDoanhThuTheoNgay.fxml"));
    			Stage stage = new Stage();
    			stage.setResizable(false);
    			stage.initModality(Modality.WINDOW_MODAL);
    			stage.initOwner(HomeManagerController.primaryStage);
    			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng - Thống kê doanh thu theo ngày");
    			Scene scene = new Scene(root);
    			stage.setScene(scene);
    			stage.sizeToScene();
    			stage.centerOnScreen();
    			stage.show();
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    	}
    	break;
    	case 2:
    	{
    		Parent root;
    		try {
    			root = FXMLLoader.load(getClass().getResource("/view/ThongKeMonAn.fxml"));
    			Stage stage = new Stage();
    			stage.setResizable(false);
    			stage.initModality(Modality.WINDOW_MODAL);
    			stage.initOwner(HomeManagerController.primaryStage);
    			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng - Thống kê về các món ăn");
    			Scene scene = new Scene(root);
    			stage.setScene(scene);
    			stage.sizeToScene();
    			stage.centerOnScreen();
    			stage.show();
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    	}
    	break;
    	case 3:
    	{
    		Parent root;
    		try {
    			root = FXMLLoader.load(getClass().getResource("/view/ThongKeKhachHang.fxml"));
    			Stage stage = new Stage();
    			stage.setResizable(false);
    			stage.initModality(Modality.WINDOW_MODAL);
    			stage.initOwner(HomeManagerController.primaryStage);
    			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng - Thống kê các khách hàng theo doanh thu");
    			Scene scene = new Scene(root);
    			stage.setScene(scene);
    			stage.sizeToScene();
    			stage.centerOnScreen();
    			stage.show();
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    	}
    	break;
    	}
    }

    @FXML
    void menuThongKe(ActionEvent event) {
    	if(event.getSource() == menuTKDTBanDatTheoNgay) {
    		showThongKe(1);
    	}
    	else if(event.getSource() == menuTKMonAn) {
    		showThongKe(2);
    	}
    	else if(event.getSource() == menuTKKhachHang) {
    		showThongKe(3);
    	}
    }
}
