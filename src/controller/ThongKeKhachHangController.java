package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.PrimaryConf;
import database.CTHoaDonBanDatDAO;
import database.CustomerDAO;
import database.HoaDonBanDatDAO;
import database.MonAnDAO;
import entites.CTHoaDonBanDat;
import entites.Customer;
import entites.HoaDonBanDat;
import entites.MonAn;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ThongKeKhachHangController implements Initializable {

    @FXML
    private Label lblDoanhThuKH;

    @FXML
    private Button btnDong;

    @FXML
    private TableView<Customer> lvKhachHang;

    @FXML
    private TableColumn<Customer, String> colMaKH;

    @FXML
    private TableColumn<Customer, String> colTenKH;

    @FXML
    private TableColumn<Customer, String> colSoCMND;

    @FXML
    private TableColumn<Customer, String> colSoDT;

    @FXML
    private TableColumn<Customer, Long> colTongDoanhThu;

    @FXML
    private Button btnThongKe;

    @FXML
    private Label lblNgayGanNhat;

    @FXML
    private Label lblSoLanDat;

    @FXML
    private ImageView imgHinhAnhMA;

    @FXML
    private TextField txtMaMA;

    @FXML
    private TextField txtTenMA;

    @FXML
    private TextField txtSoNguoi;

    @FXML
    private TextArea txtNguyenLieu;

    @FXML
    private TextArea txtMoTaMA;

    @FXML
    private TextField txtDonGia;

    @FXML
    private Button btnXuatExcel;

    @FXML
    private Label lblSoLanHuy;

    @FXML
    private CheckBox cbChiConBan;

    @FXML
    private Button btnXemChiTiet;

    @FXML
    private Label lblTongDoanhThu;

    @FXML
    private Label lblSoKhachHangUnique;

    @FXML
    private Label lblDoanhThuKHGanNhat;
    
    @FXML
    private Label lblThongSoMonGanNhat;
    
    private List<Customer> dsKhachHang;
    private ObservableList<Customer> dsOBKhachHang;
    
    private List<HoaDonBanDat> dsHoaDon;
    private List<CTHoaDonBanDat> dsCTHD;
    private List<CTHoaDonBanDat> dsCTHDMonConBan;
    
    /**
     * 0 doanh thu khách hàng
     * 1 số lần đặt bàn
     * 2 số lần hủy bàn
     * 3 ngày đặt bàn gần nhất
     * 4 doanh thu bàn gần nhất
     * 5 mã món ăn gọi nhiều nhất (all)
     * 6 số phần đặt món gọi nhiều nhất (all)
     * 7 số lần đặt bàn có món gọi nhiều nhất (all)
     * 8 ngày đặt món gọi nhiều nhất gần nhất (all)
     * 9 mã món ăn gọi nhiều nhất (còn bán)
     * 10 số phần đặt món gọi nhiều nhất (còn bán)
     * 11 số lần đặt bàn có món gọi nhiều nhất (còn bán)
     * 12 ngày đặt món gọi nhiều nhất gần nhất (còn bán)
     */
    private Map<String, List<Object>> thongKeData;
    
    private String textBtnThongKe;

    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	textBtnThongKe = btnThongKe.getText();
    	
    	colMaKH.setCellValueFactory(new Callback<CellDataFeatures<Customer,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Customer, String> param) {
				return new ReadOnlyObjectWrapper<String>(param.getValue().getTaiKhoan().getMaTK());
			}
    		
    	});
    	colTenKH.setCellValueFactory(new PropertyValueFactory<Customer, String>("hoTen"));
    	colSoCMND.setCellValueFactory(new PropertyValueFactory<Customer, String>("cmnd"));
    	colSoDT.setCellValueFactory(new PropertyValueFactory<Customer, String>("sdt"));
    	colTongDoanhThu.setCellValueFactory(new Callback<CellDataFeatures<Customer,Long>, ObservableValue<Long>>() {

			@Override
			public ObservableValue<Long> call(CellDataFeatures<Customer, Long> param) {
				return new ReadOnlyObjectWrapper<Long>((Long)thongKeData.get(param.getValue().getTaiKhoan().getMaTK()).get(0));
			}
    		
    	});
    	
    	lvKhachHang.setRowFactory(x -> {
			TableRow<Customer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(!row.isEmpty()) {
					setChiTiet(row.getItem());
				}
			});
			return row;
		});
    	
    	dsKhachHang = new ArrayList<Customer>();
	}
    
    void setChiTiet(Customer cs) {
    	List<Object> s = thongKeData.get(cs.getTaiKhoan().getMaTK());
    	if (((Long)s.get(0)) > 0) {
    	  DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblDoanhThuKH.setText(String.valueOf((Long)s.get(0) + " Đ"));
        lblSoLanDat.setText(String.valueOf((Long)s.get(1)));
        lblSoLanHuy.setText(String.valueOf((Long)s.get(2)));
        if(s.get(3) != null) {
          lblNgayGanNhat.setText(((Timestamp)s.get(3)).toLocalDateTime().format(dtFormat));
          lblDoanhThuKHGanNhat.setText(String.valueOf((Long)s.get(4) + " Đ"));
        }
        else {
          lblNgayGanNhat.setText("Không có");
          lblDoanhThuKHGanNhat.setText("Không có");
        }
        
        MonAnDAO monAnDao = new MonAnDAO();
        int buffer = cbChiConBan.isSelected() ? 4 : 0;
        MonAn ms = monAnDao.get((String)s.get(5+buffer));
        if(ms != null) {
          txtMaMA.setText(ms.getMaMA());
          txtTenMA.setText(ms.getTenMA());
          txtSoNguoi.setText(String.valueOf(ms.getSoLuongNguoi()));
          txtDonGia.setText(String.valueOf(ms.getGiaTien()));
          txtNguyenLieu.setText(ms.getNguyenLieu());
          txtMoTaMA.setText(ms.getMoTaMA());
          lblThongSoMonGanNhat.setText(String.format("Đặt %d phần trong %d lần đặt bàn. Lần cuối đặt món này vào ngày %s",
              (Long)s.get(6+buffer), (Long)s.get(7+buffer), ((Timestamp)s.get(8+buffer)).toLocalDateTime().format(dtFormat)));
          Image image = new Image("file:" + PrimaryConf.CUSTOM_FILE_PATH_HEAD + ms.getHinhAnhMA(), 200, 143, false, true);
          imgHinhAnhMA.setImage(image);
        }
        else {
          xoaThongTinMonAn();
        }
    	} else {
    	  xoaThongTinThongKe();
    	}
    }
    
    private void xoaThongTinMonAn() {
      txtMaMA.setText("");
      txtTenMA.setText("");
      txtSoNguoi.setText("");
      txtDonGia.setText("");
      txtNguyenLieu.setText("");
      txtMoTaMA.setText("");
      lblThongSoMonGanNhat.setText("Đặt %d phần trong %d lần đặt bàn. Lần cuối đặt món này vào ngày dd/MM/yyyy");
      imgHinhAnhMA.setImage(null);
    }
    
    private void xoaThongTinThongKeKhachHang() {
      lblDoanhThuKH.setText("0 Đ");
      lblSoLanDat.setText("0");
      lblSoLanHuy.setText("0");
      lblNgayGanNhat.setText("01/01/2020");
      lblDoanhThuKHGanNhat.setText("0 Đ");
    }
    
    private void xoaThongTinThongKe() {
      xoaThongTinThongKeKhachHang();
      xoaThongTinMonAn();
    }

	@FXML
    void chiConBanChange(ActionEvent event) {
		Customer cs = lvKhachHang.getSelectionModel().getSelectedItem();
		if(cs != null) {
			setChiTiet(cs);
		}
    }

    @FXML
    void dongGiaoDien(ActionEvent event) {
    	Stage currentStage = (Stage) btnDong.getScene().getWindow();
		currentStage.close();
    }

    @FXML
    void thongKeKhachHang(ActionEvent event) {
    	btnThongKe.setText("Đang xử lý...");
    	btnThongKe.setDisable(true);
    	
    	CustomerDAO customerDao = new CustomerDAO();
    	dsKhachHang = customerDao.timKhachHang("");
    	thongKeData = new TreeMap<String, List<Object>>();
    	thongKeData.clear();
    	for(Customer cs : dsKhachHang) {
    		String currentMaKH = cs.getTaiKhoan().getMaTK();
	    	ArrayList<Object> firstOb = new ArrayList<Object>();
	    	for(int i = 0; i < 3; i++)
	    		firstOb.add((long)0); // 0 1 2
	    	firstOb.add(null); // 3
	    	firstOb.add((long)0); // 4
	    	firstOb.add(""); // 5
	    	firstOb.add((long)0); // 6
	    	firstOb.add((long)0); // 7
	    	firstOb.add(null); // 8
	    	firstOb.add(""); // 9
	    	for(int i = 10; i < 12; i++)
	    		firstOb.add((long)0); // 10 11
	    	firstOb.add(null); // 12
	    	thongKeData.put(currentMaKH, firstOb);
    	}
    	lblSoKhachHangUnique.setText(String.valueOf(dsKhachHang.size()));
    	long tongDoanhThu = 0;
    	HoaDonBanDatDAO hoaDonDao = new HoaDonBanDatDAO();
    	dsHoaDon = hoaDonDao.getDSTTBanDatTheoKhachHang();
    	if(dsHoaDon.size() >= 1) {
    		String currentMaKH = dsHoaDon.get(0).getKhachHang().getTaiKhoan().getMaTK();
    		boolean isFirst = true;
    		for(int i = 0; i < dsHoaDon.size(); i++) {
    			HoaDonBanDat curr = dsHoaDon.get(i);
    			if(!curr.getKhachHang().getTaiKhoan().getMaTK().equals(currentMaKH)) {
    				isFirst = true;
    				currentMaKH = curr.getKhachHang().getTaiKhoan().getMaTK();
    			}
    			if(isFirst) {
    				if(curr.isDaThanhToan()) {
    					thongKeData.get(currentMaKH).set(3, curr.getNgayThanhToan());
    					thongKeData.get(currentMaKH).set(4, curr.getPhuGiaBanAn() + curr.tinhTongTien());
    				}
    				isFirst = false;
    			}
    			if(curr.isDaThanhToan()) {
    				thongKeData.get(currentMaKH).set(1, (Long)thongKeData.get(currentMaKH).get(1) + 1);
    				tongDoanhThu += curr.getPhuGiaBanAn() + curr.tinhTongTien();
    				thongKeData.get(currentMaKH).set(0, (Long)thongKeData.get(currentMaKH).get(0) + curr.getPhuGiaBanAn() + curr.tinhTongTien());
    			}
    			else if(curr.isDaHuy()) {
    				thongKeData.get(currentMaKH).set(2, (Long)thongKeData.get(currentMaKH).get(2) + 1);
    			}
	    	}
    	}
    	CTHoaDonBanDatDAO cthdDao = new CTHoaDonBanDatDAO();
    	dsCTHD = cthdDao.getDSCTTBanDatTheoKhachHang();
    	dsCTHDMonConBan = cthdDao.getDSCTTBanDatTheoKhachHangMonConBan();
    	if(dsCTHD.size() >= 1) {
    		String currentMaKH = dsCTHD.get(0).getTtBanDat().getKhachHang().getTaiKhoan().getMaTK();
    		String currentMaBD = dsCTHD.get(0).getTtBanDat().getMaBD();
    		String maxFavoriteMonAn = dsCTHD.get(0).getMonAn().getMaMA();
    		String currFavoriteMonAn = dsCTHD.get(0).getMonAn().getMaMA();
    		long soPhanMax = 0, soPhanCurr = 0;
    		long soDatMax = 0, soDatCurr = 0;
    		Timestamp maxFavoriteMonAnDatGanNhat = dsCTHD.get(0).getTtBanDat().getNgayThanhToan();
    		Timestamp currFavoriteMonAnDatGanNhat = dsCTHD.get(0).getTtBanDat().getNgayThanhToan();
    		boolean isFirst = true;
    		for(int i = 0; i < dsCTHD.size(); i++) {
    			CTHoaDonBanDat curr = dsCTHD.get(i);
    			if(!curr.getTtBanDat().getKhachHang().getTaiKhoan().getMaTK().equals(currentMaKH)) {
    				if(soPhanCurr > soPhanMax) {
    					soPhanMax = soPhanCurr;
    					soDatMax = soDatCurr;
    					maxFavoriteMonAn = currFavoriteMonAn;
    					maxFavoriteMonAnDatGanNhat = currFavoriteMonAnDatGanNhat;
    				}
    				thongKeData.get(currentMaKH).set(5, maxFavoriteMonAn);
    				thongKeData.get(currentMaKH).set(6, soPhanMax);
    				thongKeData.get(currentMaKH).set(7, soDatMax);
    				thongKeData.get(currentMaKH).set(8, maxFavoriteMonAnDatGanNhat);
    				currentMaKH = curr.getTtBanDat().getKhachHang().getTaiKhoan().getMaTK();
    				soPhanMax = soPhanCurr = 0;
    				soDatMax = soDatCurr = 0;
    				maxFavoriteMonAnDatGanNhat = currFavoriteMonAnDatGanNhat = curr.getTtBanDat().getNgayThanhToan();
    				maxFavoriteMonAn = currFavoriteMonAn = curr.getMonAn().getMaMA();
    				isFirst = true;
    			}
    			if(!curr.getMonAn().getMaMA().equals(currFavoriteMonAn)) {
    				if(soPhanCurr > soPhanMax) {
    					soPhanMax = soPhanCurr;
    					soDatMax = soDatCurr;
    					maxFavoriteMonAn = currFavoriteMonAn;
    					maxFavoriteMonAnDatGanNhat = currFavoriteMonAnDatGanNhat;
    				}
    				currFavoriteMonAn = curr.getMonAn().getMaMA();
    				isFirst = true;
    			}
    			if(isFirst) {
    				currFavoriteMonAnDatGanNhat = curr.getTtBanDat().getNgayThanhToan();
    				soPhanCurr = 0;
    				soDatCurr = 1;
    				currentMaBD = curr.getTtBanDat().getMaBD();
    				isFirst = false;
    			}
    			else if(!curr.getTtBanDat().getMaBD().equals(currentMaBD)) {
    				soDatCurr++;
    				currentMaBD = curr.getTtBanDat().getMaBD();
    			}
    			soPhanCurr += curr.getSoLuong();
    			
    			if(i == dsCTHD.size()-1) {
    				if(soPhanCurr > soPhanMax) {
    					soPhanMax = soPhanCurr;
    					soDatMax = soDatCurr;
    					maxFavoriteMonAn = currFavoriteMonAn;
    					maxFavoriteMonAnDatGanNhat = currFavoriteMonAnDatGanNhat;
    				}
    				thongKeData.get(currentMaKH).set(5, maxFavoriteMonAn);
    				thongKeData.get(currentMaKH).set(6, soPhanMax);
    				thongKeData.get(currentMaKH).set(7, soDatMax);
    				thongKeData.get(currentMaKH).set(8, maxFavoriteMonAnDatGanNhat);
    			}
    		}
    		
    	}
    	
    	if(dsCTHDMonConBan.size() >= 1) {
    		String currentMaKH = dsCTHDMonConBan.get(0).getTtBanDat().getKhachHang().getTaiKhoan().getMaTK();
    		String currentMaBD = dsCTHDMonConBan.get(0).getTtBanDat().getMaBD();
    		String maxFavoriteMonAn = dsCTHDMonConBan.get(0).getMonAn().getMaMA();
    		String currFavoriteMonAn = dsCTHDMonConBan.get(0).getMonAn().getMaMA();
    		long soPhanMax = 0, soPhanCurr = 0;
    		long soDatMax = 0, soDatCurr = 0;
    		Timestamp maxFavoriteMonAnDatGanNhat = dsCTHDMonConBan.get(0).getTtBanDat().getNgayThanhToan();
    		Timestamp currFavoriteMonAnDatGanNhat = dsCTHDMonConBan.get(0).getTtBanDat().getNgayThanhToan();
    		boolean isFirst = true;
    		for(int i = 0; i < dsCTHDMonConBan.size(); i++) {
    			CTHoaDonBanDat curr = dsCTHDMonConBan.get(i);
    			if(!curr.getTtBanDat().getKhachHang().getTaiKhoan().getMaTK().equals(currentMaKH)) {
    				if(soPhanCurr > soPhanMax) {
    					soPhanMax = soPhanCurr;
    					soDatMax = soDatCurr;
    					maxFavoriteMonAn = currFavoriteMonAn;
    					maxFavoriteMonAnDatGanNhat = currFavoriteMonAnDatGanNhat;
    				}
    				thongKeData.get(currentMaKH).set(9, maxFavoriteMonAn);
    				thongKeData.get(currentMaKH).set(10, soPhanMax);
    				thongKeData.get(currentMaKH).set(11, soDatMax);
    				thongKeData.get(currentMaKH).set(12, maxFavoriteMonAnDatGanNhat);
    				currentMaKH = curr.getTtBanDat().getKhachHang().getTaiKhoan().getMaTK();
    				soPhanMax = soPhanCurr = 0;
    				soDatMax = soDatCurr = 0;
    				maxFavoriteMonAnDatGanNhat = currFavoriteMonAnDatGanNhat = curr.getTtBanDat().getNgayThanhToan();
    				maxFavoriteMonAn = currFavoriteMonAn = curr.getMonAn().getMaMA();
    				isFirst = true;
    			}
    			if(!curr.getMonAn().getMaMA().equals(currFavoriteMonAn)) {
    				if(soPhanCurr > soPhanMax) {
    					soPhanMax = soPhanCurr;
    					soDatMax = soDatCurr;
    					maxFavoriteMonAn = currFavoriteMonAn;
    					maxFavoriteMonAnDatGanNhat = currFavoriteMonAnDatGanNhat;
    				}
    				currFavoriteMonAn = curr.getMonAn().getMaMA();
    				isFirst = true;
    			}
    			if(isFirst) {
    				currFavoriteMonAnDatGanNhat = curr.getTtBanDat().getNgayThanhToan();
    				soPhanCurr = 0;
    				soDatCurr = 1;
    				currentMaBD = curr.getTtBanDat().getMaBD();
    				isFirst = false;
    			}
    			else if(!curr.getTtBanDat().getMaBD().equals(currentMaBD)) {
    				soDatCurr++;
    				currentMaBD = curr.getTtBanDat().getMaBD();
    			}
    			soPhanCurr += curr.getSoLuong();
    			
    			if(i == dsCTHDMonConBan.size()-1) {
    				if(soPhanCurr > soPhanMax) {
    					soPhanMax = soPhanCurr;
    					soDatMax = soDatCurr;
    					maxFavoriteMonAn = currFavoriteMonAn;
    					maxFavoriteMonAnDatGanNhat = currFavoriteMonAnDatGanNhat;
    				}
    				thongKeData.get(currentMaKH).set(9, maxFavoriteMonAn);
    				thongKeData.get(currentMaKH).set(10, soPhanMax);
    				thongKeData.get(currentMaKH).set(11, soDatMax);
    				thongKeData.get(currentMaKH).set(12, maxFavoriteMonAnDatGanNhat);
    			}
    		}
    	}
    	
    	lblTongDoanhThu.setText(String.valueOf(tongDoanhThu) + " Đ");
    	
    	Collections.sort(dsKhachHang, new Comparator<Customer>() {

			@Override
			public int compare(Customer o1, Customer o2) {
				return (int)((Long)thongKeData.get(o2.getTaiKhoan().getMaTK()).get(0) - (Long)thongKeData.get(o1.getTaiKhoan().getMaTK()).get(0));
			}
    		
		});
    	
    	dsOBKhachHang = FXCollections.observableArrayList(dsKhachHang);
    	lvKhachHang.setItems(dsOBKhachHang);
    	
    	btnThongKe.setText(textBtnThongKe);
    	btnThongKe.setDisable(false);
    }

    @FXML
    void xemChiTietKH(ActionEvent event) {

    }

    @FXML
    void xuatFileExcel(ActionEvent event) {
    	if(dsKhachHang.isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi");
			alert.setContentText("Không có khách hàng nào để thống kê");
			alert.show();
			return;
    	}
    	else {
	    	XSSFWorkbook outputWorkbook = new XSSFWorkbook();
			XSSFSheet mainSheet = outputWorkbook.createSheet("Thống kê khách hàng");
			int colNum = 0;
			int rowNum = 0;
			// row 1
			XSSFRow row = mainSheet.createRow(rowNum++);
			XSSFCell cellRow = row.createCell(colNum++);
			cellRow.setCellValue("THỐNG KÊ KHÁCH HÀNG THEO DOANH THU");
			mainSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
			
			XSSFCellStyle cellStyleTitle = outputWorkbook.createCellStyle();
			cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
			cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFFont cellFont = outputWorkbook.createFont();
			cellFont.setFontName("Arial");
			cellFont.setFontHeightInPoints((short) 16);
			cellFont.setBold(true);
			cellStyleTitle.setFont(cellFont);
			cellRow.setCellStyle(cellStyleTitle);
			((Row)row).setHeightInPoints(30);
			
			XSSFCellStyle cellStyleHeader = outputWorkbook.createCellStyle();
			cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
			cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFFont cellFontHeader = outputWorkbook.createFont();
			cellFontHeader.setFontName("Arial");
			cellFontHeader.setFontHeightInPoints((short) 9);
			cellFontHeader.setBold(true);
			cellStyleHeader.setFont(cellFontHeader);
			
			XSSFCellStyle cellStyleContent = outputWorkbook.createCellStyle();
			XSSFFont cellFontContent = outputWorkbook.createFont();
			cellFontContent.setFontName("Arial");
			cellFontContent.setFontHeightInPoints((short) 9);
			cellFontContent.setBold(false);
			cellStyleContent.setFont(cellFontContent);
			
			System.out.println("Count "+outputWorkbook.getNumCellStyles());
			// row 2: 
			row = mainSheet.createRow(rowNum++);
			colNum = 0;
			cellRow = row.createCell(colNum++);
			cellRow.setCellValue("Ngày giờ thống kê: ");
			cellRow.setCellStyle(cellStyleContent);
			colNum++;
			cellRow = row.createCell(colNum++);
			cellRow.setCellValue(String.format(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/yyyy"))));
			cellRow.setCellStyle(cellStyleContent);
			mainSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
			mainSheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 4));
			// row 3:
			row = mainSheet.createRow(rowNum++);
			String headers[] = {"STT", "Mã khách hàng", "Tên khách hàng", "Số CMND", "Số ĐT", "Địa chỉ", "Email", "Tổng doanh thu",
					"Số lần đặt bàn (đã thanh toán)", "Số lần hủy bàn","Ngày đặt bàn gần nhất","Doanh thu bàn gần nhất", "Ký số và tên món ăn yêu thích còn bán",
					"Số phần đặt món yêu thích còn bán","Số lần đặt bàn có gọi món yêu thích còn bán","Ngày đặt món yêu thích gần nhất còn bán",
					"Ký số và tên món ăn yêu thích","Số phần đặt món yêu thích","Số lần đặt bàn có gọi món yêu thích",
					"Ngày đặt món yêu thích gần nhất",
			};
			colNum = 0;
			for(String hd : headers) {
	    		cellRow = row.createCell(colNum++);
	    		cellRow.setCellValue(hd);
	    		cellRow.setCellStyle(cellStyleHeader);
			}
			// row 4-end:
			int stt = 1;
			DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			MonAnDAO monAnDao = new MonAnDAO();
			Object time;
			for(Customer ma : dsKhachHang) {
				row = mainSheet.createRow(rowNum++);
				String maKH = ma.getTaiKhoan().getMaTK();
				colNum = 0;
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue(stt++);
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue(maKH);
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue(ma.getHoTen());
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue(ma.getCmnd());
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue(ma.getSdt());
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue(ma.getDiaChi());
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue((ma.getEmail()));
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue((Long)thongKeData.get(maKH).get(0));
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue((Long)thongKeData.get(maKH).get(1));
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue((Long)thongKeData.get(maKH).get(2));
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				time = thongKeData.get(maKH).get(3);
				cellRow.setCellValue((time == null) ? null : ((Timestamp)time).toLocalDateTime().format(dtFormat));
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue((Long)thongKeData.get(maKH).get(4));
				cellRow.setCellStyle(cellStyleContent);
				
				MonAn ms1 = monAnDao.get((String)thongKeData.get(maKH).get(5));
				String msString1 = "";
				if(ms1 != null) {
					msString1 = String.format("%s : %s", ms1.getMaMA(), ms1.getTenMA());
				}
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue(msString1);
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue((Long)thongKeData.get(maKH).get(6));
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue((Long)thongKeData.get(maKH).get(7));
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				time = thongKeData.get(maKH).get(8);
				cellRow.setCellValue((time == null) ? null : ((Timestamp)time).toLocalDateTime().format(dtFormat));
				cellRow.setCellStyle(cellStyleContent);
				
				MonAn ms2 = monAnDao.get((String)thongKeData.get(maKH).get(9));
				String msString2 = "";
				if(ms2 != null) {
					msString2 = String.format("%s : %s", ms2.getMaMA(), ms2.getTenMA());
				}
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue(msString2);
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue((Long)thongKeData.get(maKH).get(10));
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				cellRow.setCellValue((Long)thongKeData.get(maKH).get(11));
				cellRow.setCellStyle(cellStyleContent);
				cellRow = row.createCell(colNum++);
				time = thongKeData.get(maKH).get(12);
				cellRow.setCellValue((time == null) ? null : ((Timestamp)time).toLocalDateTime().format(dtFormat));
				cellRow.setCellStyle(cellStyleContent);
			}
			
			for(int i = 0; i <= headers.length; i++) {
				mainSheet.autoSizeColumn(i, true);
			}
			
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel 2007 or newer files (*.xlsx)", "*.xlsx");
			fileChooser.getExtensionFilters().add(extFilter);
			File outputExcelFile = fileChooser.showSaveDialog(btnXuatExcel.getScene().getWindow());
			if(outputExcelFile != null) {
				FileOutputStream outStream;
				try {
					outStream = new FileOutputStream(outputExcelFile);
					outputWorkbook.write(outStream);
					outStream.close();
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Thông báo");
					alert.setContentText("Xuất ra file excel thành công");
					alert.showAndWait();
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Lỗi");
					alert.setContentText("Không thể lưu file excel, hãy thử lại.");
					alert.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Lỗi");
					alert.setContentText("Không thể lưu file excel, hãy thử lại.");
					alert.showAndWait();
				}
			}
			try {
				outputWorkbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

}
