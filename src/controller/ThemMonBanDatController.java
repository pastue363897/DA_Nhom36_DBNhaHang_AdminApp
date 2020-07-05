package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.PrimaryConf;
import database.CTHoaDonBanDatDAO;
import database.HoaDonBanDatDAO;
import database.MonAnDAO;
import entites.CTHoaDonBanDat;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ThemMonBanDatController implements Initializable {
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
    private Button btnAddMon;

    @FXML
    private Button btnRemoveMon;
    
    private ItemTTBanDatDetailController banDatDetailController;
    
    private List<MonAn> dsMonAnTimThay;
    private ObservableList<MonAn> dsOBMonAnTimThay;
    private List<CTHoaDonBanDat> dsMonAnDaChon;
    private ObservableList<CTHoaDonBanDat> dsOBMonAnDaChon;
    
    private HoaDonBanDat hoaDonHienTai;
    
    public HoaDonBanDat getHoaDonHienTai() {
		return hoaDonHienTai;
	}

	public void setHoaDonHienTai(HoaDonBanDat hoaDonHienTai) {
		this.hoaDonHienTai = hoaDonHienTai;
	}

	private long tempTongTien;

	public ItemTTBanDatDetailController getBanDatDetailController() {
		return banDatDetailController;
	}

	public void setBanDatDetailController(ItemTTBanDatDetailController banDatDetailController) {
		this.banDatDetailController = banDatDetailController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		dsMonAnTimThay = new ArrayList<MonAn>();
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
		
		tinhTongTien();
	}

    @FXML
    void dongGiaoDien(ActionEvent event) {
    	hoaDonHienTai = new HoaDonBanDatDAO().getTTBanDat(hoaDonHienTai.getMaBD());
    	banDatDetailController.loadData(hoaDonHienTai);
    	Stage currentStage = (Stage) btnHuy.getScene().getWindow();
    	currentStage.close();
    }

    @FXML
    void showAll(ActionEvent event) {
    	if (event.getSource() == btnShowAllMon) {
    		MonAnDAO monAnDao = new MonAnDAO();
    		dsMonAnTimThay = monAnDao.getAll();
    		dsOBMonAnTimThay = FXCollections.observableArrayList(dsMonAnTimThay);
    		lvMonAnDangBan.setItems(dsOBMonAnTimThay);
    		lvMonAnDangBan.refresh();
    	}
    }

    @FXML
    void thanhToan(ActionEvent event) {
    	long test = tinhTongTien();
    	if(hoaDonHienTai.isDaThanhToan()) {
	    	if(test == -1 || test == -2) {
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Lỗi");
				alert.setContentText("Vì bàn đã thanh toán, hãy nhập số tiền khách đưa thêm hợp lệ");
				alert.show();
				return;
	    	}
    	}
    	if(lvMonAnDaChon.getItems().isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thanh toán");
			alert.setContentText("Chưa chọn món ăn nào");
			alert.show();
			return;
    	}
    	CTHoaDonBanDatDAO cthdDao = new CTHoaDonBanDatDAO();
    	List<CTHoaDonBanDat> hienTai = hoaDonHienTai.getDsMonAn();
    	for(CTHoaDonBanDat x : dsMonAnDaChon) {
    		boolean existed = false;
    		for(CTHoaDonBanDat y : hienTai) {
    			if(x.getMonAn().getMaMA().equals(y.getMonAn().getMaMA())) {
    				y.setSoLuong(y.getSoLuong() + x.getSoLuong());
    				existed = true;
    				cthdDao.update(y);
    				break;
    			}
    		}
    		if(!existed) {
    			x.setTtBanDat(hoaDonHienTai);
    			cthdDao.addCTHoaDonBanDat(x);
    		}
    	}
    	if(hoaDonHienTai.isDaThanhToan()) {
    		hoaDonHienTai.setTienDaDua(hoaDonHienTai.getTienDaDua() + tinhTienThoi() + tempTongTien);
    	}
    	HoaDonBanDatDAO banDatDao = new HoaDonBanDatDAO();
    	banDatDao.update(hoaDonHienTai);
    	if(PrimaryConf.currentAdmin != null) {
    		HoaDonBanDatDAO hoaDonDao = new HoaDonBanDatDAO();
    		HoaDonBanDat tmp = hoaDonDao.get(hoaDonHienTai.getMaBD());
    		tmp.setNhanVien(PrimaryConf.currentAdmin);
    	}
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Thành công");
		alert.setContentText("Bàn đã được đặt và thông tin thanh toán đã được lưu");
		alert.showAndWait();
		hoaDonHienTai = new HoaDonBanDatDAO().getTTBanDat(hoaDonHienTai.getMaBD());
		banDatDetailController.loadData(hoaDonHienTai);
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
    	if(event.getSource() == btnRemoveMon)
    		xoaMon(false);
    	else
    		xoaMon(true);
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
    	tempTongTien = 0;
    	for(CTHoaDonBanDat x : dsMonAnDaChon) {
    		tempTongTien += x.getDonGia() * x.getSoLuong();
    	}
    	lblTongTien.setText(String.valueOf(tempTongTien) + " Đ");
    	return tinhTienThoi();
    }

}
