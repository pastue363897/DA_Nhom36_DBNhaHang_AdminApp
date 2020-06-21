package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import application.PrimaryConf;
import database.HoaDonBanDatDAO;
import entites.CTHoaDonBanDat;
import entites.HoaDonBanDat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

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
    private TextArea txtBanAn;

    @FXML
    private TableView<CTHoaDonBanDat> lvMonAn;

    @FXML
    private TableColumn<CTHoaDonBanDat, String> tenMon;

    @FXML
    private TableColumn<CTHoaDonBanDat, Integer> soLuong;

    @FXML
    private TableColumn<CTHoaDonBanDat, Long> donGia;

    @FXML
    private TableColumn<String, Long> tongGia;

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
	
	@FXML
	private Button btnThemMonHoaDon;
	
	@FXML
    private Button btnInHoaDon;
	
    @FXML
    private CheckBox cbAutoInHoaDon;

	private BanDatManagerController banDatMGCT;
	private HoaDonBanDat ttBanDat;
	
	private List<CTHoaDonBanDat> dsMonAn;
    private ObservableList<CTHoaDonBanDat> dsOBMonAn;
    
    public static Stage primaryStage;

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
		
		tenMon.setCellValueFactory(new Callback<CellDataFeatures<CTHoaDonBanDat, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<CTHoaDonBanDat, String> param) {
				return new SimpleStringProperty(param.getValue().getMonAn().getTenMA());
			}
		});
		soLuong.setCellValueFactory(new PropertyValueFactory<CTHoaDonBanDat, Integer>("soLuong"));
		donGia.setCellValueFactory(new PropertyValueFactory<CTHoaDonBanDat, Long>("donGia"));
	}

	@SuppressWarnings("deprecation")
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
		dsMonAn = ttBanDat.getDsMonAn();
		dsOBMonAn = FXCollections.observableArrayList(dsMonAn);
		lvMonAn.setItems(dsOBMonAn);
		lvMonAn.refresh();
		txtBanAn.setText("* Ký số bàn: "+ttBanDat.getBanAn().getKySoBA()+"\n");
		txtBanAn.appendText("* Số lượng ghế: "+ttBanDat.getBanAn().getSoLuongGhe()+"\n");
		txtBanAn.appendText("* Mô tả bàn: "+ttBanDat.getBanAn().getMotaBA()+"\n");
		txtBanAn.appendText("* Giá phụ thu: "+ttBanDat.getBanAn().getPhuGia());
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
	    	ttBanDat = das.update(ttBanDat);
    		
	    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thanh toán thành công");
			alert.setContentText("Thanh toán bàn đã đặt thành công");
			alert.show();
			
			if(cbAutoInHoaDon.isSelected())
				inHoaDon(ttBanDat);
			Stage frame = (Stage) btnThanhToan.getScene().getWindow();
	    	banDatMGCT.loadAllBanDat();
	    	frame.close();
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
		btnInHoaDon.setVisible(false);
		if(ttBanDat.isDaThanhToan()) {
			txtTienKhachDua.setText(String.valueOf(ttBanDat.getTienDaDua()));
			txtTienKhachDua.setEditable(false);
			lblTienThoiLai.setText(String.valueOf(ttBanDat.getTienDaDua()-ttBanDat.getTongTien())+" Đ");
			btnThanhToan.setVisible(false);
			btnHuyBan.setVisible(false);
			lblDaThanhToan.setVisible(true);
			btnInHoaDon.setVisible(true);
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
	
    @FXML
    void themMonHoaDon(ActionEvent event) {
    	if(ttBanDat.getNgayThanhToan() != null) {
	    	if(Date.valueOf(LocalDate.now()).after(ttBanDat.getNgayThanhToan())) {
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
	    		alert.setTitle("Lỗi không được phép");
	    		alert.setContentText("Bây giờ đã qua khỏi ngày thanh toán, không thể đặt thêm món nữa");
	    		alert.showAndWait();
	    		return;
	    	}
    	}
    	Parent root;
    	FXMLLoader fx = new FXMLLoader(getClass().getResource("/view/ThemMonBanDatManager.fxml"));
    	try {
			root = fx.load();
			ThemMonBanDatController it = fx.getController();
			it.setHoaDonHienTai(ttBanDat);
			it.setBanDatDetailController(this);
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(ItemTTBanDatDetailController.primaryStage);
			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng - Thêm món mới cho bàn hiện tại");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.centerOnScreen();
			stage.showAndWait();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    void inHoaDon(HoaDonBanDat hoaDon) {	
    	String stringHoaDon = "";
		for(int i = 0; i < 73; i++) {
			stringHoaDon += "*";
		}
		stringHoaDon += "\n";
		
		int beforeName = (73 - PrimaryConf.RESTAURANT_NAME.length()) / 2;
		int afterName = 73 - beforeName - PrimaryConf.RESTAURANT_NAME.length();
		for(int i = 0; i < beforeName; i++) {
			stringHoaDon += " ";
		}
		stringHoaDon += PrimaryConf.RESTAURANT_NAME;
		for(int i = 0; i < afterName; i++) {
			stringHoaDon += " ";
		}
		stringHoaDon += "\n";
		
		for(int i = 0; i < 73; i++) {
			stringHoaDon += "*";
		}
		stringHoaDon += "\n";
		stringHoaDon += "Ngày giờ thanh toán: "+hoaDon.getNgayThanhToan().toLocalDateTime().toString()+"\n";
		stringHoaDon += "Ký số bàn: " + hoaDon.getBanAn().getKySoBA() + "\n";
		stringHoaDon += "Số chỗ: " + hoaDon.getBanAn().getSoLuongGhe() + "\n";
		stringHoaDon += "Phụ giá: " + hoaDon.getBanAn().getPhuGia() + "\n";
		for(int i = 0; i < 73; i++) {
			stringHoaDon += "*";
		}
		stringHoaDon += "\n";
		stringHoaDon += "STT   Tên                            Đơn giá    Số lượng        Tổng tiền\n";
		int xTT = 1;
		for(CTHoaDonBanDat ma : hoaDon.getDsMonAn()) {
			stringHoaDon +=
					String.format("%-5d %-26s %9d Đ %11d %14d Đ\n", xTT++, ma.getMonAn().getTenMA(), ma.getDonGia(),
							ma.getSoLuong(), ma.getDonGia() * ma.getSoLuong());
		}
		for(int i = 0; i < 73; i++) {
			stringHoaDon += "*";
		}
		stringHoaDon += "\n";
		stringHoaDon += String.format("                                             Tổng tiền: %15d Đ\n", hoaDon.getTongTien());
		stringHoaDon += String.format("                                        Tiền khách đưa: %15d Đ\n", hoaDon.getTienDaDua());
		stringHoaDon += String.format("                                         Tiền thối lại: %15d Đ\n", hoaDon.getTienDaDua() - hoaDon.getTongTien());
		
		javafx.print.PrinterJob psjob = javafx.print.PrinterJob.createPrinterJob();
		Text textToPrint = new Text(stringHoaDon);
		textToPrint.setFont(new javafx.scene.text.Font("Courier New", 11));
		TextFlow d = new TextFlow(textToPrint);
		boolean ok = psjob.showPrintDialog(btnInHoaDon.getScene().getWindow());
		if(ok) {
			boolean success = psjob.printPage(d);
	        if (success)
	        	psjob.endJob();
	        else {
	        	Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("Lỗi");
    			alert.setContentText("Không thể in hóa đơn, giao diện sẽ đóng lại. Bạn có thể thử in lại trong xem chi tiết bàn đặt.");
    			alert.showAndWait();
	        }
		}
    }

    @FXML
    void inHoaDon(ActionEvent event) {
    	if(event.getSource() == btnInHoaDon) {
    		inHoaDon(ttBanDat);
    	}
    }
}
