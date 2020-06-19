package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import database.HoaDonBanDatDAO;
import entites.HoaDonBanDat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ThongKeDoanhThuTheoNgayController implements Initializable {

	@FXML
	private CheckBox cbCoTaiKhoan;

	@FXML
	private Label lblTongDoanhThu;

	@FXML
	private Label lblDoanhThuCaoNhat;

	@FXML
	private Label lblDoanhThuThapNhat;

	@FXML
	private Button btnDong;

	@FXML
	private TableView<HoaDonBanDat> lvHoaDon;

	@FXML
	private TableColumn<HoaDonBanDat, String> maBD;

	@FXML
	private TableColumn<HoaDonBanDat, String> tenKH;

	@FXML
	private TableColumn<HoaDonBanDat, String> soCMND;

	@FXML
	private TableColumn<HoaDonBanDat, String> kySoBA;

	@FXML
	private TableColumn<HoaDonBanDat, Timestamp> ngayThanhToan;

	@FXML
	private TableColumn<HoaDonBanDat, Long> tongTien;

	@FXML
	private TextField txtBanAnSearch;

	@FXML
	private Button btnTimBanDat;

	@FXML
	private Button btnReset;

	@FXML
	private Button btnThongKe;

	@FXML
	private DatePicker dpTuNgay;

	@FXML
	private DatePicker dpDenNgay;

	@FXML
	private Label lblDoanhThuVangLai;

	@FXML
	private Label lblDoanhThuKhachHang;

	private List<HoaDonBanDat> dsHoaDon;
	private ObservableList<HoaDonBanDat> dsOBHoaDon;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		maBD.setCellValueFactory(new PropertyValueFactory<HoaDonBanDat, String>("maBD"));
		tenKH.setCellValueFactory(new Callback<CellDataFeatures<HoaDonBanDat, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<HoaDonBanDat, String> param) {
				return new SimpleStringProperty(param.getValue().getKhachHang().getHoTen());
			}
		});
		soCMND.setCellValueFactory(new Callback<CellDataFeatures<HoaDonBanDat, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<HoaDonBanDat, String> param) {
				return new SimpleStringProperty(param.getValue().getKhachHang().getCmnd());
			}
		});
		kySoBA.setCellValueFactory(new Callback<CellDataFeatures<HoaDonBanDat, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<HoaDonBanDat, String> param) {
				return new SimpleStringProperty(param.getValue().getBanAn().getKySoBA());
			}
		});
		ngayThanhToan.setCellValueFactory(new PropertyValueFactory<HoaDonBanDat, Timestamp>("ngayThanhToan"));
		tongTien.setCellValueFactory(new PropertyValueFactory<HoaDonBanDat, Long>("tongTien"));

		dsHoaDon = new ArrayList<HoaDonBanDat>();
		tinhThongSo();
	}

	@FXML
	void coTaiKhoanChange(ActionEvent event) {

	}

	@FXML
	void dongGiaoDien(ActionEvent event) {
		Stage currentStage = (Stage) btnDong.getScene().getWindow();
		currentStage.close();
	}

	@FXML
	void resetDSBanDat(ActionEvent event) {
		dsHoaDon.clear();
		dsOBHoaDon = FXCollections.observableArrayList(dsHoaDon);
		lvHoaDon.setItems(dsOBHoaDon);
		lvHoaDon.refresh();
		tinhThongSo();
	}

	void tinhThongSo() {
		long tongDoanhThu = 0;
		long doanhThuVL = 0;
		long maxBan = -1;
		long minBan = 19000000000000L;
		for (HoaDonBanDat h : dsHoaDon) {
			tongDoanhThu += h.getTongTien();
			if (h.getTongTien() > maxBan)
				maxBan = h.getTongTien();
			if (h.getTongTien() < minBan)
				minBan = h.getTongTien();
			if (h.getKhachHang().getTaiKhoan().getMaTK().contains("VL")) {
				doanhThuVL += h.getTongTien();
			}
		}
		if (maxBan == -1) {
			maxBan = 0;
		}
		if (minBan == 19000000000000L) {
			minBan = 0;
		}
		lblTongDoanhThu.setText(String.valueOf(tongDoanhThu) + " Đ");
		lblDoanhThuKhachHang.setText(String.valueOf(tongDoanhThu - doanhThuVL) + " Đ");
		lblDoanhThuVangLai.setText(String.valueOf(doanhThuVL) + " Đ");
		lblDoanhThuCaoNhat.setText(String.valueOf(maxBan) + " Đ");
		lblDoanhThuThapNhat.setText(String.valueOf(minBan) + " Đ");
	}

	@FXML
	void thongKeDoanhThu(ActionEvent event) {
		LocalDate fromNgay = dpTuNgay.getValue();
		LocalDate toNgay = dpDenNgay.getValue();
		int caseThongKe = -1;
		if (toNgay == null && fromNgay == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi");
			alert.setContentText("Hãy chọn ít nhất 1 trong hai trường Từ ngày hoặc Đến ngày hợp lệ");
			alert.show();
			return;
		} else if (toNgay != null && fromNgay != null) {
			if (toNgay.isBefore(fromNgay)) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Lỗi");
				alert.setContentText("Đến ngày (Ngày kết thúc) phải cùng hoặc sau ngày Từ ngày (ngày bắt đầu)");
				alert.show();
				return;
			}
			caseThongKe = 1;
		} else if (toNgay != null) {
			if (toNgay.isAfter(LocalDate.now())) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Lỗi");
				alert.setContentText("Đến ngày (Ngày kết thúc) đã chọn phải từ ngày hôm nay về trước");
				alert.show();
				return;
			}
			caseThongKe = 2;
		} else if (fromNgay != null) {
			if (fromNgay.isAfter(LocalDate.now())) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Lỗi");
				alert.setContentText("Từ ngày (Ngày kết thúc) đã chọn phải từ ngày hôm nay về trước");
				alert.show();
				return;
			}
			caseThongKe = 3;
		}
		HoaDonBanDatDAO hoaDonDao = new HoaDonBanDatDAO();
		switch (caseThongKe) {
		case 1: {
			dsHoaDon = hoaDonDao.getDSHoaDonFromTo(
					Timestamp.valueOf(LocalDateTime.of(dpTuNgay.getValue().getYear(), dpTuNgay.getValue().getMonth(),
							dpTuNgay.getValue().getDayOfMonth(), 0, 0, 0)),
					Timestamp.valueOf(LocalDateTime.of(dpDenNgay.getValue().getYear(), dpDenNgay.getValue().getMonth(),
							dpDenNgay.getValue().getDayOfMonth(), 23, 59, 59)),
					cbCoTaiKhoan.isSelected());
			dsOBHoaDon = FXCollections.observableArrayList(dsHoaDon);
			lvHoaDon.setItems(dsOBHoaDon);
			lvHoaDon.refresh();
		}
			break;
		case 2: {
			dsHoaDon = hoaDonDao
					.getDSHoaDonTo(
							Timestamp.valueOf(LocalDateTime.of(dpDenNgay.getValue().getYear(),
									dpDenNgay.getValue().getMonth(), dpDenNgay.getValue().getDayOfMonth(), 23, 59, 59)),
							cbCoTaiKhoan.isSelected());
			dsOBHoaDon = FXCollections.observableArrayList(dsHoaDon);
			lvHoaDon.setItems(dsOBHoaDon);
			lvHoaDon.refresh();
		}
			break;
		case 3: {
			dsHoaDon = hoaDonDao
					.getDSHoaDonFrom(
							Timestamp.valueOf(LocalDateTime.of(dpTuNgay.getValue().getYear(),
									dpTuNgay.getValue().getMonth(), dpTuNgay.getValue().getDayOfMonth(), 0, 0, 0)),
							cbCoTaiKhoan.isSelected());
			dsOBHoaDon = FXCollections.observableArrayList(dsHoaDon);
			lvHoaDon.setItems(dsOBHoaDon);
			lvHoaDon.refresh();
		}
			break;
		}
		tinhThongSo();
	}

	@FXML
	void timBanDat(ActionEvent event) {
		
	}

}