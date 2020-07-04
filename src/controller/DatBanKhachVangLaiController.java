package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
    
    @FXML
    private Button btnAddMon;

    @FXML
    private Button btnRemoveMon;
    
    @FXML
    private CheckBox cbDatTruoc;
    
    @FXML
    private DatePicker dpNgayDatBan;

    @FXML
    private ComboBox<String> cmbGioDat;
    
    private BanDatManagerController banDatMGCT;
    
    private HomeManagerController hostMGCT;
    
    private List<BanAn> dsBanAnTimThay;
    private ObservableList<BanAn> dsOBBanAnTimThay;
    private List<MonAn> dsMonAnTimThay;
    private ObservableList<MonAn> dsOBMonAnTimThay;
    private List<CTHoaDonBanDat> dsMonAnDaChon;
    private ObservableList<CTHoaDonBanDat> dsOBMonAnDaChon;
    
    private ArrayList<String> listGioDat = new ArrayList<String>();
    
    public int daDat = 0; // 0 
    
    public BanDatManagerController getBanDatMGCT() {
		return banDatMGCT;
	}

	public void setBanDatMGCT(BanDatManagerController banDatMGCT) {
		this.banDatMGCT = banDatMGCT;
	}

	public HomeManagerController getHostMGCT() {
		return hostMGCT;
	}

	public void setHostMGCT(HomeManagerController hostMGCT) {
		this.hostMGCT = hostMGCT;
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
		
		txtSoDT.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > 10) {
					String s = newValue.substring(0, 10);
					txtSoDT.setText(s);
				}
			}
		});
		txtSoCMND.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > 12) {
					String s = newValue.substring(0, 12);
					txtSoCMND.setText(s);
				}
			}
		});
		txtMaKH.setDisable(true);
		
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
					xoaMon(false);
				}
			});
			return row;
		});
		
		// liệt kê danh sách giờ dùng được
		int time = 300;
		do
		{
			listGioDat.add(String.format("%02d:%02d:00", time/60, time%60));
			time += 30;
		}
		while(time <= 1380);

		ObservableList<String> gioDat = FXCollections.observableArrayList(listGioDat);
		cmbGioDat.setItems(gioDat);
		
		cbDatTruoc.setSelected(false);
		cmbGioDat.setDisable(true);
    	dpNgayDatBan.setDisable(true);
		
		showAllBan();
		showAllMon();
	}
	
    @FXML
    void coDatTruoc(ActionEvent event) {
    	cmbGioDat.setDisable(!cbDatTruoc.isSelected());
    	dpNgayDatBan.setDisable(!cbDatTruoc.isSelected());
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
    
    public void showAllBan() {
    	BanAnDAO banAnDao = new BanAnDAO();
		dsBanAnTimThay = banAnDao.timBanAnDatDuoc();
		dsOBBanAnTimThay = FXCollections.observableArrayList(dsBanAnTimThay);
		lvBanAn.setItems(dsOBBanAnTimThay);
		lvBanAn.refresh();
    }
    public void showAllMon() {
    	MonAnDAO monAnDao = new MonAnDAO();
		dsMonAnTimThay = monAnDao.getDSMonAn();
		dsOBMonAnTimThay = FXCollections.observableArrayList(dsMonAnTimThay);
		lvMonAnDangBan.setItems(dsOBMonAnTimThay);
		lvMonAnDangBan.refresh();
    }
    @FXML
    void showAll(ActionEvent event) {
    	if(event.getSource() == btnShowAllBan) {
    		showAllBan();
    	}
    	else if (event.getSource() == btnShowAllMon) {
    		showAllMon();
    	}
    }

    @FXML
    void thanhToan(ActionEvent event) {
    	CustomerDAO cusDao = new CustomerDAO();
    	Customer cus = null;
    	
    	if(cbCoTaiKhoan.isSelected()) {
    		cus = cusDao.get(txtMaKH.getText());
    		if(cus == null) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("Lỗi đặt bàn");
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
			alert.setTitle("Lỗi đặt bàn");
			alert.setContentText("Chưa chọn bàn ăn");
			alert.show();
			return;
    	}
    	if(lvMonAnDaChon.getItems().isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi đặt bàn");
			alert.setContentText("Chưa chọn món ăn nào");
			alert.show();
			return;
    	}
    	HoaDonBanDat hoaDon = new HoaDonBanDat(cus, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), lvBanAn.getSelectionModel().getSelectedItem());
    	for(CTHoaDonBanDat x : dsMonAnDaChon) {
    		x.setTtBanDat(hoaDon);
    	}
    	CTHoaDonBanDatDAO cthdDao = new CTHoaDonBanDatDAO();
    	if(cbDatTruoc.isSelected()) {
    		LocalDate ngayDat = null;
    		if(dpNgayDatBan.getValue() == null) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("Lỗi đặt bàn");
    			alert.setContentText("Bàn được chọn là đặt trước nhưng chưa chọn ngày phục vụ nào");
    			alert.showAndWait();
    			return;
    		}
    		ngayDat = dpNgayDatBan.getValue();
    		if(cmbGioDat.getValue() == null) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("Lỗi đặt bàn");
    			alert.setContentText("Bàn được chọn là đặt trước nhưng chưa chọn giờ phục vụ nào");
    			alert.showAndWait();
    			return;
    		}
    		;
    		Timestamp timeDat = Timestamp.valueOf(LocalDateTime.of(ngayDat, LocalTime.parse(cmbGioDat.getValue())));
    		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    		LocalDate ngayDatMin = now.toLocalDateTime().toLocalDate();
    		LocalDate ngayDatChon = timeDat.toLocalDateTime().toLocalDate();
    		if(!ngayDatChon.isAfter(ngayDatMin)) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("Lỗi đặt bàn");
    			alert.setContentText("Ngày phục vụ chỉ có thể từ ngày mai trở đi");
    			alert.showAndWait();
    			return;
    		}
    		boolean checkDaDat = hoaDonDao.checkBanTrungDatTruoc(lvBanAn.getSelectionModel().getSelectedItem().getMaBA(), timeDat);
    		if(checkDaDat) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("Lỗi đặt bàn");
    			alert.setContentText("Bàn đã chọn đã có bàn đặt trùng ngày phục vụ với đang chọn, hãy chọn ngày khác.");
    			alert.showAndWait();
    			return;
    		}
    		hoaDon.setNgayPhucVu(timeDat);
    		
    	}
    	hoaDon.setDaThanhToan(false);
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
		alert.setContentText("Thông tin bàn đặt đã được lưu, để thêm món hay thanh toán. Tìm bàn này trong quản lý bàn đặt");
		alert.showAndWait();
		daDat = 0;
		showAllBan();
		showAllMon();
		
		banDatMGCT.loadAllBanDat();
		hostMGCT.showQLBanDat();
		return;
    }
    
    public void reload() {
      dsOBMonAnDaChon.clear();
      showAllBan();
      showAllMon();
      txtBanAnSearch.setText("");
      txtMonAnSearch.setText("");
      txtMaKH.setText("");
      txtTenKhachHang.setText("");
      txtSoCMND.setText("");
      txtSoDT.setText("");
      txtDiaChi.setText("");
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
    	dsBanAnTimThay = banAnDao.timBanAnDatDuoc(txtBanAnSearch.getText());
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
    
    void xoaMon(boolean all) {
    	CTHoaDonBanDat ma = lvMonAnDaChon.getSelectionModel().getSelectedItem();
    	if (ma != null) {
    		boolean het = false;
	    	int index = 0;
	    	for (CTHoaDonBanDat x : dsMonAnDaChon) {
	    		if (x.getMonAn().getMaMA().equals(ma.getMonAn().getMaMA())) {
	    			if(x.getSoLuong() >= 2 && !all) {
	    				x.setSoLuong(x.getSoLuong()-1);
	    				break;
	    			}
	    			else {
	    				het = true; break;
	    			}
	    				
	    		}
	    		index++;
	    	}
	    	if(het)
	    		dsMonAnDaChon.remove(index);
	    	dsOBMonAnDaChon = FXCollections.observableArrayList(dsMonAnDaChon);
	    	lvMonAnDaChon.setItems(dsOBMonAnDaChon);
	    	lvMonAnDaChon.refresh();
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
    	if(event.getSource() == btnRemoveMon)
    		xoaMon(false);
    	else
    		xoaMon(true);
    }

}
