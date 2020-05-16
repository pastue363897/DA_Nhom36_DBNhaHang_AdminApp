/**
 * Created on: 16:46:19 2 thg 5, 2020
 * 
 * @author Dinh Van Dung YKNB
 */

package controller;

import java.io.IOException;
import java.util.List;

import database.HoaDonDAO;
import database.TTBanDatDAO;
import entites.HoaDon;
import entites.TTBanDat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ItemTTBanDatController {
	@FXML
	private Label lblDateDat, lblMonthDat, lblYearDat;
	@FXML
	private Label lblDatePhucVu, lblMonthPhucVu, lblYearPhucVu;
	@FXML
	private Label lblHoTenKhachHang;
	@FXML
	private Label lblKySoBanAn;
	@FXML
	private Label lblTongTien;
	@FXML
	private Label lblDaThanhToan, lblDaHuy, lblChuaThanhToan;

	private BanDatManagerController banDatMGCT;
	private TTBanDat ttBanDat;

	public void loadData(TTBanDat b, HoaDon hd) {
		lblDateDat.setText(stringDate(b.getNgayDatBan().getDate()));
		lblMonthDat.setText(stringMonth(b.getNgayDatBan().getMonth() + 1));
		lblYearDat.setText(String.valueOf(b.getNgayDatBan().getYear() + 1900));
		lblDatePhucVu.setText(stringDate(b.getNgayPhucVu().getDate()));
		lblMonthPhucVu.setText(stringMonth(b.getNgayPhucVu().getMonth() + 1));
		lblYearPhucVu.setText(String.valueOf(b.getNgayPhucVu().getYear() + 1900));
		lblHoTenKhachHang.setText(b.getKhachHang().getHoTen());
		lblKySoBanAn.setText(b.getBanAn().getKySoBA());
		lblTongTien.setText(String.valueOf(b.tinhTongTien()) + " Đ");
		showStatus(b,hd);
		ttBanDat = b;
	}

	public void showStatus(TTBanDat b, HoaDon hd) {
		if (b.isDaHuy()) {
			lblDaThanhToan.setVisible(false);
			lblDaHuy.setVisible(true);
			lblChuaThanhToan.setVisible(false);
		} else if (hd != null) {
			lblDaThanhToan.setVisible(true);
			lblDaHuy.setVisible(false);
			lblChuaThanhToan.setVisible(false);
		} else {
			lblDaThanhToan.setVisible(false);
			lblDaHuy.setVisible(false);
			lblChuaThanhToan.setVisible(true);
		}
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
			result = "JAN";
			break;
		case 2:
			result = "FEB";
			break;
		case 3:
			result = "MAR";
			break;
		case 4:
			result = "APR";
			break;
		case 5:
			result = "MAY";
			break;
		case 6:
			result = "JUN";
			break;
		case 7:
			result = "JUL";
			break;
		case 8:
			result = "AUG";
			break;
		case 9:
			result = "STE";
			break;
		case 10:
			result = "OCT";
			break;
		case 11:
			result = "SEP";
			break;
		case 12:
			result = "DEC";
			break;
		default:
			result = "JAN";
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
			ctr.loadData(new TTBanDatDAO().getTTBanDat(ttBanDat.getMaBD()), new HoaDonDAO().get(ttBanDat.getMaBD()));
			
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(HomeManagerController.primaryStage);
			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.centerOnScreen();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
