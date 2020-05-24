package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import database.BanAnDAO;
import database.CTHoaDonBanDatDAO;
import database.CustomerDAO;
import database.HoaDonBanDatDAO;
import database.MonAnDAO;
import entites.Account;
import entites.BanAn;
import entites.CTHoaDonBanDat;
import entites.Customer;
import entites.HoaDonBanDat;
import entites.MonAn;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.bytebuddy.utility.RandomString;

public class DatBanKhachVangLaiController implements Initializable {

    @FXML
    private TextField txtTenKhachHang;

    @FXML
    private TextField txtDiaChi;

    @FXML
    private TextField txtSoCMND;

    @FXML
    private TextField txtSoDT;

    @FXML
    private CheckBox cbCoTaiKhoan;

    @FXML
    private TextField txtMaKH;

    @FXML
    private Button btnThemMonAn;

    @FXML
    private Button btnXoaMonAn;

    @FXML
    private Label lblTongTien;

    @FXML
    private TextField txtTienKhachDua;

    @FXML
    private Label lblTienThoi;

    @FXML
    private Button btnThanhToan;

    @FXML
    private Button btnHuy;

    @FXML
    private TableView<BanAn> lvBanAn;

    @FXML
    private TableColumn<BanAn, String> kySoBA;

    @FXML
    private TableColumn<BanAn, Integer> soGhe;

    @FXML
    private TableColumn<BanAn, String> moTaBA;

    @FXML
    private TableColumn<BanAn, Long> phuGia;

    @FXML
    private TableView<MonAn> lvMonAnDangBan;
    
    @FXML
    private TableColumn<MonAn, String> tenMon;

    @FXML
    private TableColumn<MonAn, Integer> soLuongNguoi;

    @FXML
    private TableColumn<MonAn, Long> donGia;
    
    @FXML
    private TableView<CTHoaDonBanDat> lvMonAnDaChon;
    
    @FXML
    private TableColumn<CTHoaDonBanDat, String> tenMonChon;
    
    @FXML
    private TableColumn<CTHoaDonBanDat, Long> donGiaMua;

    @FXML
    private TableColumn<CTHoaDonBanDat, Integer> soLuongDat;

    @FXML
    private TextField txtMonAnSearch;

    @FXML
    private Button btnTimMon;

    @FXML
    private Button btnShowAllMon;

    @FXML
    private TextField txtBanAnSearch;

    @FXML
    private Button btnTimBan;

    @FXML
    private Button btnShowAllBan;
    
    private BanDatManagerController banDatMGCT;
    
    private List<BanAn> dsBanAnTimThay;
    private ObservableList<BanAn> dsOBBanAnTimThay;
    private List<MonAn> dsMonAnTimThay;
    private ObservableList<MonAn> dsOBMonAnTimThay;
    private List<CTHoaDonBanDat> dsMonAnDaChon;
    private ObservableList<CTHoaDonBanDat> dsOBMonAnDaChon;
    
    private long tempTongTien;
    
    public BanDatManagerController getBanDatMGCT() {
		return banDatMGCT;
	}

	public void setBanDatMGCT(BanDatManagerController banDatMGCT) {
		this.banDatMGCT = banDatMGCT;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		kySoBA.setCellValueFactory(new PropertyValueFactory<BanAn, String>("kySoBA"));
		soGhe.setCellValueFactory(new PropertyValueFactory<BanAn, Integer>("soLuongGhe"));
		moTaBA.setCellValueFactory(new PropertyValueFactory<BanAn, String>("motaBA"));
		phuGia.setCellValueFactory(new PropertyValueFactory<BanAn, Long>("phuGia"));
		
		tenMon.setCellValueFactory(new PropertyValueFactory<MonAn, String>("tenMA"));
		soLuongNguoi.setCellValueFactory(new PropertyValueFactory<MonAn, Integer>("soLuongNguoi"));
		donGia.setCellValueFactory(new PropertyValueFactory<MonAn, Long>("giaTien"));
		
		tenMonChon.setCellValueFactory(new Callback<CellDataFeatures<CTHoaDonBanDat, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<CTHoaDonBanDat, String> param) {
				return new SimpleStringProperty(param.getValue().getMonAn().getTenMA());
			}
		});
		soLuongDat.setCellValueFactory(new PropertyValueFactory<CTHoaDonBanDat, Integer>("soLuong"));
		donGiaMua.setCellValueFactory(new PropertyValueFactory<CTHoaDonBanDat, Long>("donGia"));
		
		dsMonAnDaChon = new ArrayList<CTHoaDonBanDat>();
		dsBanAnTimThay = new ArrayList<BanAn>();
		dsMonAnTimThay = new ArrayList<MonAn>();
		dsOBBanAnTimThay = FXCollections.observableArrayList(dsBanAnTimThay);
		dsOBMonAnTimThay = FXCollections.observableArrayList(dsMonAnTimThay);
		dsOBMonAnDaChon = FXCollections.observableArrayList(dsMonAnDaChon);
		
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
		txtMaKH.setDisable(true);
		
		lvBanAn.setRowFactory(x -> {
			TableRow<BanAn> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(!row.isEmpty()) {
					tinhTongTien();
				}
			});
			return row;
		});
		
		lvMonAnDangBan.setRowFactory(x -> {
			TableRow<MonAn> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(!row.isEmpty() && event.getClickCount() == 2) {
					themMon();
				}
			});
			return row;
		});
		
		lvMonAnDaChon.setRowFactory(x -> {
			TableRow<CTHoaDonBanDat> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(!row.isEmpty() && event.getClickCount() == 2) {
					xoaMon();
				}
			});
			return row;
		});
		
		tinhTongTien();
	}

    @FXML
    void coTaiKhoanChange(ActionEvent event) {
    	if(cbCoTaiKhoan.isSelected()) {
    		swapKhachHangInput(true);
    		
    	}
    	else {
    		txtMaKH.setText("");
    		swapKhachHangInput(false);
    	}
    }
    
    void swapKhachHangInput(boolean dungMaKH) {
    	txtMaKH.setDisable(!dungMaKH);
		txtTenKhachHang.setDisable(dungMaKH);
		txtSoCMND.setDisable(dungMaKH);
		txtSoDT.setDisable(dungMaKH);
		txtDiaChi.setDisable(dungMaKH);
		
    }

    @FXML
    void dongGiaoDien(ActionEvent event) {
    	banDatMGCT.loadAllBanDat();
    	Stage currentStage = (Stage) btnHuy.getScene().getWindow();
    	currentStage.close();
    }

    @FXML
    void showAll(ActionEvent event) {
    	if(event.getSource() == btnShowAllBan) {
    		BanAnDAO banAnDao = new BanAnDAO();
    		dsBanAnTimThay = banAnDao.getAll();
    		dsOBBanAnTimThay = FXCollections.observableArrayList(dsBanAnTimThay);
    		lvBanAn.setItems(dsOBBanAnTimThay);
    		lvBanAn.refresh();
    	}
    	else if (event.getSource() == btnShowAllMon) {
    		MonAnDAO monAnDao = new MonAnDAO();
    		dsMonAnTimThay = monAnDao.getAll();
    		dsOBMonAnTimThay = FXCollections.observableArrayList(dsMonAnTimThay);
    		lvMonAnDangBan.setItems(dsOBMonAnTimThay);
    		lvMonAnDangBan.refresh();
    	}
    }

    @FXML
    void thanhToan(ActionEvent event) {
    	CustomerDAO cusDao = new CustomerDAO();
    	Customer cus = null;
    	long test = tinhTongTien();
    	if(test == -1 || test == -2) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thanh toán");
			alert.setContentText("Số tiền khách đưa không hợp lệ");
			alert.show();
			return;
    	}
    	if(cbCoTaiKhoan.isSelected()) {
    		cus = cusDao.get(txtMaKH.getText());
    		if(cus == null) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("Lỗi thanh toán");
    			alert.setContentText("Không tìm thấy khách hàng");
    			alert.show();
    			return;
    		}
    	}
    	else {
    		Account acc = new Account();
    		acc.setUsername("generated_"+ new RandomString(15).nextString());
    		cus = new Customer(txtTenKhachHang.getText(), txtDiaChi.getText(), txtSoCMND.getText(), txtSoDT.getText(), "", acc);
    	}
    	HoaDonBanDatDAO hoaDonDao = new HoaDonBanDatDAO();
    	if(lvBanAn.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thanh toán");
			alert.setContentText("Chưa chọn bàn ăn");
			alert.show();
			return;
    	}
    	if(lvMonAnDaChon.getItems().isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thanh toán");
			alert.setContentText("Chưa chọn món ăn nào");
			alert.show();
			return;
    	}
    	HoaDonBanDat hoaDon = new HoaDonBanDat(cus, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), lvBanAn.getSelectionModel().getSelectedItem());
    	for(CTHoaDonBanDat x : dsMonAnDaChon) {
    		x.setTtBanDat(hoaDon);
    	}
    	CTHoaDonBanDatDAO cthdDao = new CTHoaDonBanDatDAO();
    	hoaDon.setDaThanhToan(true);
    	hoaDon.setTongTien(tempTongTien);
    	hoaDon.setTienDaDua(tempTongTien + test);
    	hoaDon.setNgayThanhToan(Timestamp.valueOf(LocalDateTime.now()));
    	hoaDon.setDaHuy(false);
    	hoaDon.setDsMonAn(dsMonAnDaChon);
    	if(!cbCoTaiKhoan.isSelected())
    		cusDao.addVLCustomer(cus);
    	hoaDonDao.addBanDatVL(hoaDon);
    	for(CTHoaDonBanDat x : dsMonAnDaChon) {
    		cthdDao.addCTHoaDonBanDat(x);
    	}
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Thành công");
		alert.setContentText("Bàn đã được đặt và thông tin thanh toán đã được lưu");
		alert.showAndWait();
		banDatMGCT.loadAllBanDat();
		Stage currentStage = (Stage) btnHuy.getScene().getWindow();
    	currentStage.close();
		return;
    }
    
    void themMon() {
    	MonAn ma = lvMonAnDangBan.getSelectionModel().getSelectedItem();
    	if(ma != null) {
	    	int found = -1;
	    	for (CTHoaDonBanDat x : dsMonAnDaChon) {
	    		if (x.getMonAn().getMaMA().equals(ma.getMaMA())) {
	    			found = 1;
	    			x.setSoLuong(x.getSoLuong()+1);
	    			break;
	    		}
	    	}
	    	
	    	if(found == -1) {
	    		CTHoaDonBanDat hd = new CTHoaDonBanDat(null, ma, 1, ma.getGiaTien());
	        	dsMonAnDaChon.add(hd);
	    	}
	    	dsOBMonAnDaChon = FXCollections.observableArrayList(dsMonAnDaChon);
	    	lvMonAnDaChon.setItems(dsOBMonAnDaChon);
	    	lvMonAnDaChon.refresh();
	    	tinhTongTien();
    	}
    	else {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi xóa món ăn");
			alert.setContentText("Bạn chưa chọn món muốn thêm");
			alert.show();
    	}
    }

    @FXML
    void themMonVaoDS(ActionEvent event) {
    	themMon();
    }

    @FXML
    void timBan(ActionEvent event) {
    	BanAnDAO banAnDao = new BanAnDAO();
    	dsBanAnTimThay = banAnDao.timBanAn(txtBanAnSearch.getText());
    	dsOBBanAnTimThay = FXCollections.observableArrayList(dsBanAnTimThay);
    	lvBanAn.setItems(dsOBBanAnTimThay);
		lvBanAn.refresh();
    }

    @FXML
    void timMon(ActionEvent event) {
    	MonAnDAO monAnDao = new MonAnDAO();
    	dsMonAnTimThay = monAnDao.timMonAn(txtMonAnSearch.getText());
    	dsOBMonAnTimThay = FXCollections.observableArrayList(dsMonAnTimThay);
    	lvMonAnDangBan.setItems(dsOBMonAnTimThay);
		lvMonAnDangBan.refresh();
    }
    
    void xoaMon() {
    	CTHoaDonBanDat ma = lvMonAnDaChon.getSelectionModel().getSelectedItem();
    	if (ma != null) {
	    	int index = 0;
	    	for (CTHoaDonBanDat x : dsMonAnDaChon) {
	    		if (x.getMonAn().getMaMA().equals(ma.getMonAn().getMaMA())) {
	    			break;
	    		}
	    		index++;
	    	}
	    	dsMonAnDaChon.remove(index);
	    	dsOBMonAnDaChon = FXCollections.observableArrayList(dsMonAnDaChon);
	    	lvMonAnDaChon.setItems(dsOBMonAnDaChon);
	    	lvMonAnDaChon.refresh();
	    	tinhTongTien();
    	}
    	else {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi xóa món ăn");
			alert.setContentText("Bạn chưa chọn món muốn xóa");
			alert.show();
    	}
    }

    @FXML
    void xoaMonKhoiDS(ActionEvent event) {
    	xoaMon();
    }
    
    long tinhTienThoi() {
    	try {
			long value = Long.parseLong(txtTienKhachDua.getText());
			long charge = value - tempTongTien;
			if (charge < 0) {
				lblTienThoi.setText("Tiền đưa không đủ");
				return -1;
			}
			lblTienThoi.setText(String.valueOf(charge) + " Đ");
			return charge;
		} catch (NumberFormatException ex1) {
			lblTienThoi.setText("Tiền đưa sai");
			return -2;
		}
    }
    
    long tinhTongTien() {
    	if(lvBanAn.getSelectionModel().getSelectedItem() == null)
    		tempTongTien = 0;
    	else
    		tempTongTien = lvBanAn.getSelectionModel().getSelectedItem().getPhuGia();
    	for(CTHoaDonBanDat x : dsMonAnDaChon) {
    		tempTongTien += x.getDonGia() * x.getSoLuong();
    	}
    	lblTongTien.setText(String.valueOf(tempTongTien) + " Đ");
    	return tinhTienThoi();
    }

}
