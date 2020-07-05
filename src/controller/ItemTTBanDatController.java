/**
 * Created on: 16:46:19 2 thg 5, 2020
 * 
 * @author Dinh Van Dung YKNB, Ta Khanh Hoang
 */

package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import application.PrimaryConf;
import database.HoaDonBanDatDAO;
import entites.HoaDonBanDat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ItemTTBanDatController {
	@FXML
	private Label lblTimeDat, lblDateDat, lblMonthDat, lblYearDat;
	@FXML
	private Label lblTimePhucVu, lblDatePhucVu, lblMonthPhucVu, lblYearPhucVu;
	@FXML
	private Label lblHoTenKhachHang;
	@FXML
	private Label lblKySoBanAn;
	@FXML
	private Label lblTongTien;
	@FXML
	private Label lblDaThanhToan, lblDaHuy, lblChuaThanhToan, lblDangAn;
	@FXML
	private ImageView imvBanAn;

	private BanDatManagerController banDatMGCT;
	private HoaDonBanDat ttBanDat;

	@SuppressWarnings("deprecation")
	public void loadData(HoaDonBanDat b) {
	  Image image = new Image("file:" + PrimaryConf.CUSTOM_FILE_PATH_HEAD + b.getBanAn().getHinhAnh(), 200, 165, false,
        true);
    imvBanAn.setImage(image);
    lblTimeDat.setText(stringTime(b.getNgayDatBan().getHours(), b.getNgayDatBan().getMinutes()));
		lblDateDat.setText(stringDate(b.getNgayDatBan().getDate()));
		lblMonthDat.setText(stringMonth(b.getNgayDatBan().getMonth() + 1));
		lblYearDat.setText(String.valueOf(b.getNgayDatBan().getYear() + 1900));
		lblTimePhucVu.setText(stringTime(b.getNgayPhucVu().getHours(), b.getNgayPhucVu().getMinutes()));
		lblDatePhucVu.setText(stringDate(b.getNgayPhucVu().getDate()));
		lblMonthPhucVu.setText(stringMonth(b.getNgayPhucVu().getMonth() + 1));
		lblYearPhucVu.setText(String.valueOf(b.getNgayPhucVu().getYear() + 1900));
		lblHoTenKhachHang.setText(b.getKhachHang().getHoTen());
		lblKySoBanAn.setText(b.getBanAn().getKySoBA());
		if(b.isDaThanhToan())
			lblTongTien.setText(String.valueOf(b.tinhTongTien() + b.getPhuGiaBanAn()) + " Đ");
		else
			lblTongTien.setText(String.valueOf(b.tinhTongTien() + b.getBanAn().getPhuGia()) + " Đ");
		showStatus(b);
		ttBanDat = b;
	}

	public void showStatus(HoaDonBanDat b) {
		lblDangAn.setVisible(false);
		if (b.isDaHuy()) {
			lblDaThanhToan.setVisible(false);
			lblDaHuy.setVisible(true);
			lblChuaThanhToan.setVisible(false);
			lblDangAn.setVisible(false);
		} else if (b.isDaThanhToan()) {
			lblDaThanhToan.setVisible(true);
			lblDaHuy.setVisible(false);
			lblChuaThanhToan.setVisible(false);
			lblDangAn.setVisible(false);
		} else {
			lblDaThanhToan.setVisible(false);
			lblDaHuy.setVisible(false);
			if(LocalDate.now().toString().equals(b.getNgayPhucVu().toLocalDateTime().toLocalDate().toString()) && !LocalDateTime.now().isBefore(b.getNgayPhucVu().toLocalDateTime())) {
				lblChuaThanhToan.setVisible(false);
				lblDangAn.setVisible(true);
			}
			else {
				lblChuaThanhToan.setVisible(true);
			}
		}
	}
	
	public String stringTime(int hour, int minute) {
    return stringDate(hour) + ":" + stringDate(minute);
  }

	public String stringDate(int date) {
		String result;
		if (date < 10) {
			result = "0" + date;
		} else {
			result = "" + date;
		}
		return result;
	}

	public String stringMonth(int month) {
		String result;
		switch (month) {
		case 1:
			result = "THÁNG 1";
			break;
		case 2:
			result = "THÁNG 2";
			break;
		case 3:
			result = "THÁNG 3";
			break;
		case 4:
			result = "THÁNG 4";
			break;
		case 5:
			result = "THÁNG 5";
			break;
		case 6:
			result = "THÁNG 6";
			break;
		case 7:
			result = "THÁNG 7";
			break;
		case 8:
			result = "THÁNG 8";
			break;
		case 9:
			result = "THÁNG 9";
			break;
		case 10:
			result = "THÁNG 10";
			break;
		case 11:
			result = "THÁNG 11";
			break;
		case 12:
			result = "THÁNG 12";
			break;
		default:
			result = "THÁNG 1";
			break;
		}
		return result;
	}

	public void setBanDatMGCT(BanDatManagerController banDatMGCT) {
		this.banDatMGCT = banDatMGCT;
	}

	@FXML
	void chiTietBanDat(ActionEvent event) {
		/*
		 * Alert alert = new Alert(Alert.AlertType.INFORMATION);
		 * alert.setTitle("Information"); alert.setContentText("chiTietBanDat");
		 * alert.show();
		 */
		Parent root;
		try {
			FXMLLoader fx = new FXMLLoader(getClass().getResource("/view/ItemBanDatDetail.fxml"));
			root = fx.load();
			root.applyCss();
			ItemTTBanDatDetailController ctr = fx.getController();
			ctr.setBanDatMGCT(new BanDatManagerController());
			ctr.loadData(new HoaDonBanDatDAO().getTTBanDat(ttBanDat.getMaBD()));
			
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(HomeManagerController.primaryStage);
			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng - Chi tiết bàn đặt");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.centerOnScreen();
			ItemTTBanDatDetailController.primaryStage = stage;
			stage.showAndWait();
			banDatMGCT.loadAllBanDat();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
