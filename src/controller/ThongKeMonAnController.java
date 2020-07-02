package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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
import database.MonAnDAO;
import entites.CTHoaDonBanDat;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

public class ThongKeMonAnController implements Initializable {
    @FXML
    private Label lblTongDoanhThu;

    @FXML
    private Label lblSoPhanDD7;

    @FXML
    private Button btnDong;

    @FXML
    private TableView<MonAn> lvMonAn;

    @FXML
    private TableColumn<MonAn, String> colThuHang;

    @FXML
    private TableColumn<MonAn, String> colTenMA;

    @FXML
    private TableColumn<MonAn, Integer> colSoNguoi;

    @FXML
    private TableColumn<MonAn, Long> colDonGia;

    @FXML
    private TableColumn<MonAn, Long> colSoPhanDD;

    @FXML
    private TableColumn<MonAn, Long> colTongDoanhThu;

    @FXML
    private Button btnThongKe;
    
    @FXML
    private Button btnXuatExcel;

    @FXML
    private Label lblSoBanDatMon;

    @FXML
    private Label lblSoPhanDD;

    @FXML
    private Label lblSoPhanDD14;

    @FXML
    private TextArea txtXuHuongAuto;

    @FXML
    private ImageView imgHinhAnhMA;

    @FXML
    private Label lblDoanhThuMon7;

    @FXML
    private Label lblDoanhThuMon14;

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
    private Label lblNgay7;

    @FXML
    private Label lblNgay14;
    
    @FXML
    private CheckBox cbTuyChonKhoangNgay;
    
    @FXML
    private ComboBox<String> cmbChuKyNgay;

    @FXML
    private DatePicker dpNgayFrom7;

    @FXML
    private DatePicker dpNgayTo7;

    @FXML
    private DatePicker dpNgayFrom14;

    @FXML
    private DatePicker dpNgayTo14;
    
    @FXML
    private Label lblKyTruocDo;

    @FXML
    private Label lblKyNay;

    @FXML
    private Label lblLuuYAdvanced;

    @FXML
    private Label lblNgayFrom14;

    @FXML
    private Label lblNgayFrom7;

    @FXML
    private Label lblNgayTo14;

    @FXML
    private Label lblNgayTo7;

    @FXML
    private Label lblLuuYSimple;

    @FXML
    private Button btnXoaTrang;
    
    private List<MonAn> dsMonAn;
	private ObservableList<MonAn> dsOBMonAn;
	private List<CTHoaDonBanDat> dsCTHD;
	/**
	 * 0 số phần đặt
	 * 1 tổng doanh thu
	 * 2 số bàn đặt
	 * 3 - 4 số phần 7 và doanh thu 7
	 * 5 - 6 số phần 14 và doanh thu 14
	 * 7 ngày đặt cũ nhất
	 */
	private Map<String, List<Object>> thongKeData;
	
	private String textBtnThongKe;
	
	private List<String> textArrayChuKyNgay;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textBtnThongKe = btnThongKe.getText();
		colThuHang.setCellValueFactory(new PropertyValueFactory<MonAn, String>("maMA"));
		colTenMA.setCellValueFactory(new PropertyValueFactory<MonAn, String>("tenMA"));
		colSoNguoi.setCellValueFactory(new PropertyValueFactory<MonAn, Integer>("soLuongNguoi"));
		colDonGia.setCellValueFactory(new PropertyValueFactory<MonAn, Long>("giaTien"));
		colSoPhanDD.setCellValueFactory(new Callback<CellDataFeatures<MonAn, Long>, ObservableValue<Long>>() {
			@Override
			public ObservableValue<Long> call(CellDataFeatures<MonAn, Long> param) {
				return new ReadOnlyObjectWrapper<Long>((Long)thongKeData.get(param.getValue().getMaMA()).get(0));
			}
		});
		colTongDoanhThu.setCellValueFactory(new Callback<CellDataFeatures<MonAn, Long>, ObservableValue<Long>>() {
			@Override
			public ObservableValue<Long> call(CellDataFeatures<MonAn, Long> param) {
				return new ReadOnlyObjectWrapper<Long>((Long)thongKeData.get(param.getValue().getMaMA()).get(1));
			}
		});
		
		lvMonAn.setRowFactory(x -> {
			TableRow<MonAn> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(!row.isEmpty()) {
					lblSoPhanDD.setText(String.valueOf((Long)thongKeData.get(row.getItem().getMaMA()).get(0)));
					lblTongDoanhThu.setText(String.valueOf((Long)thongKeData.get(row.getItem().getMaMA()).get(1)) + " Đ");
					lblSoBanDatMon.setText(String.valueOf((Long)thongKeData.get(row.getItem().getMaMA()).get(2)));
					lblSoPhanDD7.setText(String.valueOf((Long)thongKeData.get(row.getItem().getMaMA()).get(3)));
					lblDoanhThuMon7.setText(String.valueOf((Long)thongKeData.get(row.getItem().getMaMA()).get(4)) + " Đ");
					lblSoPhanDD14.setText(String.valueOf((Long)thongKeData.get(row.getItem().getMaMA()).get(5)));
					lblDoanhThuMon14.setText(String.valueOf((Long)thongKeData.get(row.getItem().getMaMA()).get(6)) + " Đ");
					LocalDate oldest = (LocalDate)thongKeData.get(row.getItem().getMaMA()).get(7);
					if(oldest.isBefore(LocalDate.now().minusDays(7))) {
						Long d7 = (Long)thongKeData.get(row.getItem().getMaMA()).get(3);
						Long d14 = (Long)thongKeData.get(row.getItem().getMaMA()).get(5);
						if(d7 > d14) {
							if(d7 * 1.0 / d14 > 1.1)
								txtXuHuongAuto.setText("Món ăn đang có xu hướng được đặt nhiều hơn");
							else
								txtXuHuongAuto.setText("Món ăn đang có xu hướng ổn định về số lần đặt");
						}
						else if((Long)thongKeData.get(row.getItem().getMaMA()).get(3) < (Long)thongKeData.get(row.getItem().getMaMA()).get(5)) {
							if(d14 * 1.0 / d7 > 1.1)
								txtXuHuongAuto.setText("Món ăn đang có xu hướng được đặt ít đi");
							else
								txtXuHuongAuto.setText("Món ăn đang có xu hướng ổn định về số lần đặt");
						}
						else if((Long)thongKeData.get(row.getItem().getMaMA()).get(3) == (Long)thongKeData.get(row.getItem().getMaMA()).get(5)) {
							txtXuHuongAuto.setText("Món ăn đang có xu hướng được đặt ít đi");
						}
					}
					else {
						txtXuHuongAuto.setText("Món ăn không được đặt lần nào từ ngày thứ 8 đổ về trước, không xác định xu hướng được");
					}
					tinhThongSo(row.getItem());
				}
			});
			return row;
		});
		
		textArrayChuKyNgay = Arrays.asList(
			"7 ngày gần đây", "2 tuần gần đây", "1 tháng gần đây", "3 tháng gần đây",
			"6 tháng gần đây", "1 năm gần đây", "3 năm gần đây"
			);
		ObservableList<String> obChuKyNgay = FXCollections.observableArrayList(textArrayChuKyNgay);
		cmbChuKyNgay.setItems(obChuKyNgay);
		cmbChuKyNgay.setPromptText("Chọn chu kỳ ngày");
		dsMonAn = new ArrayList<MonAn>();
		xoaTrangAction(true);
	}

    @FXML
    void dongGiaoDien(ActionEvent event) {
    	Stage currentStage = (Stage) btnDong.getScene().getWindow();
		currentStage.close();
    }
    
    @FXML
    void coTuyChonKhoangNgay(ActionEvent event) {
    	updateKhoangNgayState(cbTuyChonKhoangNgay.isSelected());
    }
    
    @FXML
    void xoaTrang(ActionEvent event) {
    	xoaTrangAction(true);
    }
    
    void xoaTrangAction(boolean clear) {
    	dsMonAn.clear();
    	if(dsOBMonAn != null)
    		dsOBMonAn.clear();
    	txtDonGia.clear();
    	txtMaMA.clear();
    	txtMoTaMA.clear();
    	txtNguyenLieu.clear();
    	txtSoNguoi.clear();
    	txtTenMA.clear();
    	txtXuHuongAuto.clear();
    	
    	lblDoanhThuMon14.setText("0 Đ");
    	lblDoanhThuMon7.setText("0 Đ");
    	lblNgay14.setText("<Chưa xác định>");
    	lblNgay7.setText("<Chưa xác định>");
    	lblSoBanDatMon.setText("0");
    	lblSoPhanDD.setText("0");
    	lblSoPhanDD14.setText("0");
    	lblSoPhanDD7.setText("0");
    	lblTongDoanhThu.setText("0 Đ");
    	if(clear)
    		xoaTrangMini();
    }
    void xoaTrangMini() {
    	cmbChuKyNgay.setValue(null);
    	cmbChuKyNgay.setPromptText("Chọn chu kỳ ngày");
    	dpNgayFrom14.setValue(null);
    	dpNgayFrom7.setValue(null);
    	dpNgayTo14.setValue(null);
    	dpNgayTo7.setValue(null);
    }
    
    void updateKhoangNgayState(boolean isAdvanced) {
    	dpNgayFrom14.setDisable(!isAdvanced);
    	dpNgayFrom7.setDisable(!isAdvanced);
    	dpNgayTo14.setDisable(!isAdvanced);
    	dpNgayTo7.setDisable(!isAdvanced);
    	lblKyTruocDo.setDisable(!isAdvanced);
    	lblKyNay.setDisable(!isAdvanced);
    	lblNgayFrom14.setDisable(!isAdvanced);
    	lblNgayFrom7.setDisable(!isAdvanced);
    	lblNgayTo14.setDisable(!isAdvanced);
    	lblNgayTo7.setDisable(!isAdvanced);
    	lblLuuYSimple.setVisible(!isAdvanced);
    	lblLuuYAdvanced.setVisible(isAdvanced);
    	cmbChuKyNgay.setDisable(isAdvanced);
    }

    @FXML
    void thongKeMonAn(ActionEvent event) {
    	boolean coKyTruoc = false;
    	if(!cbTuyChonKhoangNgay.isSelected()) {
    		if(cmbChuKyNgay.getSelectionModel().isEmpty()) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Lỗi");
				alert.setContentText("Chưa chọn chu kỳ ngày, hãy kiểm tra lại");
				alert.showAndWait();
				return;
    		}
    	}
    	else {
    		if(dpNgayFrom7.getValue() == null || dpNgayTo7.getValue() == null) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Lỗi");
				alert.setContentText("Chưa chọn chu kỳ ngày kỳ này, hãy kiểm tra lại");
				alert.showAndWait();
				return;
    		}
    		else {
    			if(dpNgayFrom7.getValue().isAfter(dpNgayTo7.getValue())) {
    				Alert alert = new Alert(Alert.AlertType.ERROR);
    				alert.setTitle("Lỗi");
    				alert.setContentText("Ngày bắt đầu chu kỳ (từ ngày) phải trước hoặc bằng ngày đến");
    				alert.showAndWait();
    				return;
    			}
    		}
    		
    		if(dpNgayFrom14.getValue() == null && dpNgayTo14.getValue() == null) {
    			
    		}
    		else if(dpNgayFrom14.getValue() != null ^ dpNgayTo14.getValue() != null) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Lỗi");
				alert.setContentText("Nếu đã chọn ngày của chu kỳ trước thì phải chọn đủ từ ngày và đến ngày");
				alert.showAndWait();
				return;
    		}
    		else {
    			if(dpNgayFrom14.getValue().isAfter(dpNgayTo14.getValue())) {
    				Alert alert = new Alert(Alert.AlertType.ERROR);
    				alert.setTitle("Lỗi");
    				alert.setContentText("Ngày bắt đầu chu kỳ (từ ngày) phải trước hoặc bằng ngày đến");
    				alert.showAndWait();
    				return;
    			}
    			else if(!dpNgayTo14.getValue().isBefore(dpNgayFrom7.getValue())) {
    				Alert alert = new Alert(Alert.AlertType.ERROR);
    				alert.setTitle("Lỗi");
    				alert.setContentText("Ngày cuối chu kỳ trước phải trước ngày đầu tiên của chu kỳ hiện tại");
    				alert.showAndWait();
    				return;
    			}
    			coKyTruoc = true;
    		}
    	}
    	btnThongKe.setText("Đang xử lý...");
    	btnThongKe.setDisable(true);
    	
		xoaTrangAction(false);
    	MonAnDAO monAnDao = new MonAnDAO();
    	dsMonAn = monAnDao.getDSMonAn();
    	CTHoaDonBanDatDAO cthdDao = new CTHoaDonBanDatDAO();
    	dsCTHD = cthdDao.getDSCTTBanDatDaThanhToan();
    	thongKeData = new TreeMap<String, List<Object>>();
    	thongKeData.clear();
    	for(MonAn ma : dsMonAn) {
    		String currentMaMA = ma.getMaMA();
	    	ArrayList<Object> firstOb = new ArrayList<Object>();
	    	for(int i = 0; i < 7; i++)
	    		firstOb.add((long)0);
	    	firstOb.add(LocalDate.now());
	    	thongKeData.put(currentMaMA, firstOb);
    	}
    	DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	if(dsCTHD.size() >= 1) {
	    	//int indexNew = 0;
	    	LocalDate now = LocalDate.now();
	    	LocalDate back7 = null;
	    	LocalDate back14 = null;
	    	LocalDate back1 = null, back8 = null;
	    	if(!cbTuyChonKhoangNgay.isSelected()) {
	    		switch(cmbChuKyNgay.getSelectionModel().getSelectedIndex()) {
	    		case 0:
	    		{
	    			back7 = now.minusDays(7);
	    			back14 = back7.minusDays(7);
	    		}
	    		break;
	    		case 1:
	    		{
	    			back7 = now.minusWeeks(2);
	    			back14 = back7.minusWeeks(2);
	    		}
	    		break;
	    		case 2:
	    		{
	    			back7 = now.minusMonths(1);
	    			back14 = back7.minusMonths(1);
	    		}
	    		break;
	    		case 3:
	    		{
	    			back7 = now.minusMonths(3);
	    			back14 = back7.minusMonths(3);
	    		}
	    		break;
	    		case 4:
	    		{
	    			back7 = now.minusMonths(6);
	    			back14 = back7.minusMonths(6);
	    		}
	    		break;
	    		case 5:
	    		{
	    			back7 = now.minusYears(1);
	    			back14 = back7.minusYears(1);
	    		}
	    		break;
	    		case 6:
	    		{
	    			back7 = now.minusYears(3);
	    			back14 = back7.minusYears(3);
	    		}
	    		break;
	    		}
	    		lblNgay7.setText(String.format("%s - %s", df.format(back7), df.format(now)));
	    		lblNgay14.setText(String.format("%s - %s", df.format(back14), df.format(back7.minusDays(1))));
	    	}
	    	else {
	    		back1 = dpNgayTo7.getValue();
	    		back7 = dpNgayFrom7.getValue();
	    		if(coKyTruoc) {
	    			back8 = dpNgayTo14.getValue();
	    			back14 = dpNgayFrom14.getValue();
	    		}
	    		else {
	    			back8 = dpNgayFrom7.getValue().minusDays(1);
	    			back14 = back8.minusDays(ChronoUnit.DAYS.between(back7, back1));
	    		}
	    		if(back7.isEqual(back1))
	    			lblNgay7.setText(String.format("%s", df.format(back7)));
	    		else
	    			lblNgay7.setText(String.format("%s - %s", df.format(back7), df.format(back1)));
	    		
	    		if(back14.isEqual(back8))
	    			lblNgay14.setText(String.format("%s", df.format(back14)));
	    		else
	    			lblNgay14.setText(String.format("%s - %s", df.format(back14), df.format(back8)));
	    	}
	    	String currentMaBD = dsCTHD.get(0).getTtBanDat().getMaBD();
	    	for(int i = 0; i < dsCTHD.size(); i++) 	{
	    		CTHoaDonBanDat curr = dsCTHD.get(i);
	    		String currentMaMA = curr.getMonAn().getMaMA();
	    		//System.out.println("BEFORE "+c.get(0) + " " + c.get(1));
    			thongKeData.get(currentMaMA).set(0, (long)thongKeData.get(currentMaMA).get(0) + curr.getSoLuong());
    			thongKeData.get(currentMaMA).set(1, (long)thongKeData.get(currentMaMA).get(1) + curr.getDonGia() * curr.getSoLuong());
	    		//System.out.println(d.get(0) + " " + d.get(1));
    			if(!curr.getTtBanDat().getMaBD().equals(currentMaBD)) {
    				thongKeData.get(currentMaMA).set(2, (long)thongKeData.get(currentMaMA).get(2) + 1);
    				currentMaBD = curr.getTtBanDat().getMaBD();
    			}
    			LocalDate ngayTT = curr.getTtBanDat().getNgayThanhToan().toLocalDateTime().toLocalDate();
    			if(ngayTT.isBefore((LocalDate)thongKeData.get(currentMaMA).get(7))) {
    				thongKeData.get(currentMaMA).set(7, ngayTT);
    			}
    			if(!cbTuyChonKhoangNgay.isSelected()) {
	    			if(ngayTT.isAfter(back7) && (ngayTT.isBefore(now) || ngayTT.isEqual(now))) {
	    				thongKeData.get(currentMaMA).set(3, (long)thongKeData.get(currentMaMA).get(3) + curr.getSoLuong());
		    			thongKeData.get(currentMaMA).set(4, (long)thongKeData.get(currentMaMA).get(4) + curr.getDonGia() * curr.getSoLuong());
	    			}
	    			else if(ngayTT.isAfter(back14) && (ngayTT.isBefore(back7) || ngayTT.isEqual(back7))) {
	    				thongKeData.get(currentMaMA).set(5, (long)thongKeData.get(currentMaMA).get(5) + curr.getSoLuong());
		    			thongKeData.get(currentMaMA).set(6, (long)thongKeData.get(currentMaMA).get(6) + curr.getDonGia() * curr.getSoLuong());
	    			}
	    		}
    			else {
    				if((ngayTT.isAfter(back7) || ngayTT.isEqual(back7)) && (ngayTT.isBefore(back1) || ngayTT.isEqual(back1))) {
	    				thongKeData.get(currentMaMA).set(3, (long)thongKeData.get(currentMaMA).get(3) + curr.getSoLuong());
		    			thongKeData.get(currentMaMA).set(4, (long)thongKeData.get(currentMaMA).get(4) + curr.getDonGia() * curr.getSoLuong());
	    			}
	    			else if((ngayTT.isAfter(back14) || ngayTT.isEqual(back14)) && (ngayTT.isBefore(back8) || ngayTT.isEqual(back8))) {
	    				thongKeData.get(currentMaMA).set(5, (long)thongKeData.get(currentMaMA).get(5) + curr.getSoLuong());
		    			thongKeData.get(currentMaMA).set(6, (long)thongKeData.get(currentMaMA).get(6) + curr.getDonGia() * curr.getSoLuong());
	    			}
    			}
	    	}
    	}
    	Collections.sort(dsMonAn, new Comparator<MonAn>() {

			@Override
			public int compare(MonAn o1, MonAn o2) {
				return (int)((Long)thongKeData.get(o2.getMaMA()).get(1) - (Long)thongKeData.get(o1.getMaMA()).get(1));
			}
    		
    	});
    	dsOBMonAn = FXCollections.observableArrayList(dsMonAn);
    	lvMonAn.setItems(dsOBMonAn);
    	
    	btnThongKe.setText(textBtnThongKe);
    	btnThongKe.setDisable(false);
    }
    
    @FXML
    void xuatFileExcel(ActionEvent event) {
    	if(dsMonAn.isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi");
			alert.setContentText("Không có món ăn nào để thống kê");
			alert.show();
			return;
    	}
    	else {
    		XSSFWorkbook outputWorkbook = new XSSFWorkbook();
    		XSSFSheet mainSheet = outputWorkbook.createSheet("Doanh thu các món ăn");
    		int colNum = 0;
    		int rowNum = 0;
    		// row 1
    		XSSFRow row = mainSheet.createRow(rowNum++);
    		XSSFCell cellRow = row.createCell(colNum++);
    		cellRow.setCellValue("THỐNG KÊ DOANH THU CÁC MÓN ĂN");
    		mainSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
    		
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
    		cellRow.setCellValue(String.format(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"))));
    		cellRow.setCellStyle(cellStyleContent);
    		mainSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
    		mainSheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 4));
    		// row 3:
    		row = mainSheet.createRow(rowNum++);
    		String headers[] = {"STT", "Mã món ăn", "Tên món ăn", "Số người/phần", "Đơn giá hiện tại", "Doanh thu của món",
    				"Số phần được đặt", "Số bàn đặt món này","Số phần được đặt và doanh thu theo giai đoạn","","","","Xu hướng"
    		};
    		colNum = 0;
    		for(String hd : headers) {
        		cellRow = row.createCell(colNum++);
        		cellRow.setCellValue(hd);
    		}
    		for (int ks = 0; ks <= 7; ks++) {
    			mainSheet.addMergedRegion(new CellRangeAddress(2, 4, ks, ks));
    			cellRow = mainSheet.getRow(2).getCell(ks);
	    		cellRow.setCellStyle(cellStyleHeader);
    		}
    		mainSheet.addMergedRegion(new CellRangeAddress(2, 2, 8, 11));
    		cellRow = mainSheet.getRow(2).getCell(8);
    		cellRow.setCellStyle(cellStyleHeader);
    		mainSheet.addMergedRegion(new CellRangeAddress(2, 4, 12, 12));
    		cellRow = mainSheet.getRow(2).getCell(12);
    		cellRow.setCellStyle(cellStyleHeader);
    		// row 4:
    		row = mainSheet.createRow(rowNum++);
    		String headers2[] = {"","","","","","","","",lblNgay14.getText(), "", lblNgay7.getText(), ""};
    		colNum = 0;
    		for(String hd : headers2) {
        		cellRow = row.createCell(colNum++);
        		cellRow.setCellValue(hd);
    		}
    		for (int ks = 8; ks <= 11; ks+=2) {
	    		mainSheet.addMergedRegion(new CellRangeAddress(3, 3, ks, ks+1));
	    		cellRow = mainSheet.getRow(3).getCell(ks);
	    		cellRow.setCellStyle(cellStyleHeader);
    		}
    		// row 5:
    		row = mainSheet.createRow(rowNum++);
    		String headers3[] = {"","","","","","","","","Số phần", "Doanh thu", "Số phần", "Doanh thu"};
    		colNum = 0;
    		for(String hd : headers3) {
        		cellRow = row.createCell(colNum++);
        		cellRow.setCellValue(hd);
        		cellRow.setCellStyle(cellStyleHeader);
    		}
    		// row 6-end:
    		int stt = 1;
    		for(MonAn ma : dsMonAn) {
    			row = mainSheet.createRow(rowNum++);
    			colNum = 0;
    			cellRow = row.createCell(colNum++);
    			cellRow.setCellValue(stt++);
    			cellRow.setCellStyle(cellStyleContent);
    			cellRow = row.createCell(colNum++);
    			cellRow.setCellValue(ma.getMaMA());
    			cellRow.setCellStyle(cellStyleContent);
    			cellRow = row.createCell(colNum++);
    			cellRow.setCellValue(ma.getTenMA());
    			cellRow.setCellStyle(cellStyleContent);
    			cellRow = row.createCell(colNum++);
    			cellRow.setCellValue(ma.getSoLuongNguoi());
    			cellRow.setCellStyle(cellStyleContent);
    			cellRow = row.createCell(colNum++);
    			cellRow.setCellValue(ma.getGiaTien());
    			cellRow.setCellStyle(cellStyleContent);
    			cellRow = row.createCell(colNum++);
    			cellRow.setCellValue((Long)thongKeData.get(ma.getMaMA()).get(1));
    			cellRow.setCellStyle(cellStyleContent);
    			cellRow = row.createCell(colNum++);
    			cellRow.setCellValue((Long)thongKeData.get(ma.getMaMA()).get(0));
    			cellRow.setCellStyle(cellStyleContent);
    			cellRow = row.createCell(colNum++);
    			cellRow.setCellValue((Long)thongKeData.get(ma.getMaMA()).get(2));
    			cellRow.setCellStyle(cellStyleContent);
    			for(int fs = 5; fs >= 3; fs-=2) {
    				cellRow = row.createCell(colNum++);
        			cellRow.setCellValue((Long)thongKeData.get(ma.getMaMA()).get(fs));
        			cellRow.setCellStyle(cellStyleContent);
        			cellRow = row.createCell(colNum++);
        			cellRow.setCellValue((Long)thongKeData.get(ma.getMaMA()).get(fs+1));
        			cellRow.setCellStyle(cellStyleContent);
    			}
    			cellRow = row.createCell(colNum++);
    			String trend = "";
    			LocalDate oldest = (LocalDate)thongKeData.get(ma.getMaMA()).get(7);
				if(oldest.isBefore(LocalDate.now().minusDays(7))) {
					Long d7 = (Long)thongKeData.get(ma.getMaMA()).get(3);
					Long d14 = (Long)thongKeData.get(ma.getMaMA()).get(5);
					if(d7 > d14) {
						if(d7 * 1.0 / d14 > 1.1)
							trend = "Món ăn đang có xu hướng được đặt nhiều hơn";
						else
							trend = "Món ăn đang có xu hướng ổn định về số lần đặt";
					}
					else if((Long)thongKeData.get(ma.getMaMA()).get(3) < (Long)thongKeData.get(ma.getMaMA()).get(5)) {
						if(d14 * 1.0 / d7 > 1.1)
							trend = "Món ăn đang có xu hướng được đặt ít đi";
						else
							trend = "Món ăn đang có xu hướng ổn định về số lần đặt";
					}
					else if((Long)thongKeData.get(ma.getMaMA()).get(3) == (Long)thongKeData.get(ma.getMaMA()).get(5)) {
						trend = "Món ăn đang có xu hướng được đặt ít đi";
					}
				}
				else {
					trend = "Món ăn không được đặt lần nào từ ngày thứ 8 đổ về trước, không xác định xu hướng được";
				}
    			cellRow.setCellValue(trend);
    			cellRow.setCellStyle(cellStyleContent);
    		}
    		
    		for(int i = 0; i <= 12; i++) {
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
    
    void tinhThongSo(MonAn ma) {
    	txtMaMA.setText(ma.getMaMA());
    	txtTenMA.setText(ma.getTenMA());
    	txtSoNguoi.setText(String.valueOf(ma.getSoLuongNguoi()));
    	txtDonGia.setText(String.valueOf(ma.getGiaTien()));
    	txtNguyenLieu.setText(ma.getNguyenLieu());
    	txtMoTaMA.setText(ma.getMoTaMA());
    	Image image = new Image("file:" + PrimaryConf.CUSTOM_FILE_PATH_HEAD + ma.getHinhAnhMA(), 200, 143, false, true);
		imgHinhAnhMA.setImage(image);
    }

}
