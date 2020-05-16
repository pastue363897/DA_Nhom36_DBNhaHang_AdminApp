package controller;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import database.HoaDonBanDatDAO;
import entites.HoaDonBanDat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ItemTTBanDatDetailController implements Initializable {
	@FXML
	private Label lblNgayDat;

	@FXML
	private Label lblThangDat;

	@FXML
	private Label lblNamDat;

	@FXML
	private Label lblNgayPhucVu;

	@FXML
	private Label lblThangPhucVu;

	@FXML
	private Label lblNamPhucVu;

	@FXML
	private Label lblMaKH;

	@FXML
	private Label lblTenKH;

	@FXML
	private Label lblSoCMND;

	@FXML
	private Label lblDiaChiKH;

	@FXML
	private Pane txtTTBanAn;

	@FXML
	private Pane txtTTMonAn;

	@FXML
	private Label lblDaHuy;

	@FXML
	private Label lblDaThanhToan;

	@FXML
	private Label lblTongTien;

	@FXML
	private Label lblTienThoiLai;

	@FXML
	private TextField txtTienKhachDua;

	@FXML
	private Button btnHuyBan;

	@FXML
	private Button btnThanhToan;

	private BanDatManagerController banDatMGCT;
	private HoaDonBanDat ttBanDat;

	public BanDatManagerController getBanDatMGCT() {
		return banDatMGCT;
	}

	public void setBanDatMGCT(BanDatManagerController banDatMGCT) {
		this.banDatMGCT = banDatMGCT;
	}

	public HoaDonBanDat getTtBanDat() {
		return ttBanDat;
	}

	public void setTtBanDat(HoaDonBanDat ttBanDat) {
		this.ttBanDat = ttBanDat;
	}
	@FXML
	void huyBan(ActionEvent event) {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtTienKhachDua.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > 15) {
					String s = newValue.substring(0, 15);
					txtTienKhachDua.setText(s);
				}
				tinhTienThoi();
			}
		});
	}

	public void loadData(HoaDonBanDat bd) {
		ttBanDat = bd;
		lblMaKH.setText(ttBanDat.getKhachHang().getTaiKhoan().getMaTK());
		lblTenKH.setText(ttBanDat.getKhachHang().getHoTen());
		lblDiaChiKH.setText(ttBanDat.getKhachHang().getDiaChi());
		lblSoCMND.setText(ttBanDat.getKhachHang().getCmnd());
		if(ttBanDat.isDaThanhToan())
		  lblTongTien.setText(String.valueOf(ttBanDat.getTongTien()) + " Đ");
		else
			lblTongTien.setText(String.valueOf(ttBanDat.tinhTongTien()) + " Đ");
		lblNgayDat.setText(stringDate(ttBanDat.getNgayDatBan().getDate()));
		lblThangDat.setText(stringMonth(ttBanDat.getNgayDatBan().getMonth() + 1));
		lblNamDat.setText(String.valueOf(ttBanDat.getNgayDatBan().getYear() + 1900));
		lblNgayPhucVu.setText(stringDate(ttBanDat.getNgayPhucVu().getDate()));
		lblThangPhucVu.setText(stringMonth(ttBanDat.getNgayPhucVu().getMonth() + 1));
		lblNamPhucVu.setText(String.valueOf(ttBanDat.getNgayPhucVu().getYear() + 1900));
		tinhTienThoi();
		updateTrangThai();
	}

	@FXML
    void thanhToan(ActionEvent event) {
		// Khi debug xong thì bỏ comment, vì đây là check ngày thanh toán >= ngày phục vụ
		// vì ko thể nào thanh toán trước khi phục vụ được
		/*if((Date.valueOf(LocalDate.now()).before(ttBanDat.getNgayPhucVu()))) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Thanh toán thất bại");
			alert.setContentText("Ngày thanh toán phải tính từ ngày phục vụ trở đi, thanh toán thất bại");
			alert.show();
			return;
		}*/
    	long tienThoi = tinhTienThoi();
    	if(tienThoi == -1) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Thanh toán thất bại");
			alert.setContentText("Tiền khách đưa không đủ, hãy kiểm tra lại");
			alert.show();
			return;
    	}
    	else if(tienThoi == -2) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Thanh toán thất bại");
			alert.setContentText("Tiền khách đưa nhập sai, hãy kiểm tra lại");
			alert.show();
			return;
    	}
    	try {
    		ttBanDat.setNgayThanhToan(Timestamp.valueOf(LocalDateTime.now()));
	    	ttBanDat.setTongTien(ttBanDat.tinhTongTien());
	    	ttBanDat.setTienDaDua(ttBanDat.getTongTien()+tienThoi);
	    	ttBanDat.setDaThanhToan(true);
	    	
	    	HoaDonBanDatDAO das = new HoaDonBanDatDAO();
	    	das.update(ttBanDat);
    		
	    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thanh toán thành công");
			alert.setContentText("Thanh toán bàn đã đặt thành công");
			alert.show();
    	}
    	catch(HibernateException ex1) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("Thanh toán thất bại");
    		alert.setContentText("Có lỗi xảy ra, kiểm tra lại");
    		alert.show();
    	}
    	catch(Exception ex2) {
    	  Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Thanh toán thất bại");
        alert.setContentText("Có lỗi xảy ra, kiểm tra lại");
        alert.show();
    	}
    	Stage frame = (Stage) btnThanhToan.getScene().getWindow();
    	frame.close();
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

	public String stringDate(int date) {
		String result;
		if (date < 10) {
			result = "0" + date;
		} else {
			result = "" + date;
		}
		return result;
	}

	private long tinhTienThoi() {
		try {
			long value = Long.parseLong(txtTienKhachDua.getText());
			long charge = value - ttBanDat.tinhTongTien();
			if (charge < 0) {
				lblTienThoiLai.setText("Tiền đưa không đủ");
				return -1;
			}
			lblTienThoiLai.setText(String.valueOf(charge) + " Đ");
			return charge;
		} catch (NumberFormatException ex1) {
			lblTienThoiLai.setText("Tiền đưa sai");
			return -2;
		}
	}

	@FXML
	void setTienKhachDua(ActionEvent event) {
		tinhTienThoi();
	}
	
	public void updateTrangThai() {
		if(ttBanDat.isDaThanhToan()) {
			txtTienKhachDua.setText(String.valueOf(ttBanDat.getTienDaDua()));
			txtTienKhachDua.setEditable(false);
			lblTienThoiLai.setText(String.valueOf(ttBanDat.getTienDaDua()-ttBanDat.getTongTien())+" Đ");
			btnThanhToan.setVisible(false);
			btnHuyBan.setVisible(false);
			lblDaThanhToan.setVisible(true);
		}
		else if(ttBanDat.isDaHuy()) {
			txtTienKhachDua.setText("Không áp dụng");
			txtTienKhachDua.setEditable(false);
			lblTienThoiLai.setText("Không áp dụng");
			txtTienKhachDua.setEditable(false);
			btnThanhToan.setVisible(false);
			btnHuyBan.setVisible(false);
			lblDaHuy.setVisible(true);
		}
	}
}
