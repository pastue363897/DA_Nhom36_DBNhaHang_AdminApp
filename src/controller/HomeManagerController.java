/**
 * Created on: 22:08:18 25 thg 4, 2020
 * 
 * @author Dinh Van Dung YKNB, Ta Khanh Hoang
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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
    private MenuItem menuTaoKhachHang;
    @FXML
    private MenuItem menuXemDanhSachKhachHang;
    @FXML
    private MenuItem menuTKDTBanDatTheoNgay;
    @FXML
    private MenuItem menuTKMonAn;
    @FXML
    private MenuItem menuTKKhachHang;
	
	public static Stage primaryStage;

	public void datBanKhachVangLai(ActionEvent e) {
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
	}

	public void signOut(MouseEvent e) {
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
	}
	
	@FXML
    void menuQLBanAn(ActionEvent event) {
		if(event.getSource() == menuThemBanAn)
			banAnController.getTabPane().getSelectionModel().select(banAnController.getTabThem());
		else if(event.getSource() == menuTimKiemBanAn)
			banAnController.getTabPane().getSelectionModel().select(banAnController.getTabTimKiem());
		banDat.setVisible(false);
		banAn.setVisible(true);
		monAn.setVisible(false);
		
    }

    @FXML
    void menuQLBanDat(ActionEvent event) {
    	if(event.getSource() == menuDatBanVangLai) {
    		datBanKhachVangLai(null);
    	}
    	else {
    		banDatController.loadAllBanDat();
	    	banDat.setVisible(true);
			banAn.setVisible(false);
			monAn.setVisible(false);
    	}
    }

    @FXML
    void menuQLKhachHang(ActionEvent event) {
    	if(event.getSource() == menuXemDanhSachKhachHang) {
    		Parent root;
    		try {
    			root = FXMLLoader.load(getClass().getResource("/view/QuanLyKhachHangManager.fxml"));
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
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
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

    @FXML
    void menuQLMonAn(ActionEvent event) {
    	if(event.getSource() == menuThemMonAn)
			monAnController.getTabPane().getSelectionModel().select(monAnController.getTabThem());
		else if(event.getSource() == menuTimKiemMonAn)
			monAnController.getTabPane().getSelectionModel().select(monAnController.getTabTimKiem());
    	banDat.setVisible(false);
		banAn.setVisible(false);
		monAn.setVisible(true);
    }

    @FXML
    void menuThongKe(ActionEvent event) {
    	if(event.getSource() == menuTKDTBanDatTheoNgay) {
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
    	else if(event.getSource() == menuTKMonAn) {
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
    	else if(event.getSource() == menuTKKhachHang) {
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
    }
}
