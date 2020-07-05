/**
 * Created on: 14:44:24 26 thg 4, 2020
 * 
 * @author Dinh Van Dung YKNB, Ta Khanh Hoang
 */

package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.CreationHelper;
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
import org.hibernate.HibernateException;

import application.PrimaryConf;
import database.MonAnDAO;
import entites.MonAn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;

public class MonAnManagerController implements Initializable {

	private String idMonAnUpdate;
	@FXML
	private TextField txtTenMonAn, txtSoLuongNguoiMA, txtGiaTienMA, txtNguyenLieu;
	@FXML
	private TextArea txtMoTaMA;
	@FXML
	private ImageView imvHinhAnhMA;
	@FXML
    private TabPane tabPane;
    @FXML
    private Tab tabThem;
    @FXML
    private Tab tabTimKiem;
    @FXML
    private TextField txtTenMon;
    @FXML
    private TextField txtSoNguoi;
    @FXML
    private Button btnTimMonAn;
    @FXML
    private TextField txtGia;
    @FXML
    private CheckBox cbDaHuy;
    @FXML
    private Button btnShowAll;

	@FXML
	private FlowPane dsMonAn;

	private String chosenFileExtension = FilenameUtils.getExtension("./src/images/food-view.png");
	private File chosenHinhAnh = new File("./src/images/food-view.png");
	private String currentHinhAnh;
	@SuppressWarnings("unused")
	private static String hinhAnh = "/images/food-view.png";

	public void setIdMonAnUpdate(String idMonAnUpdate) {
		this.idMonAnUpdate = idMonAnUpdate;
	}

	public TextField getTxtTenMonAn() {
		return txtTenMonAn;
	}

	public TextField getTxtSoLuongNguoiMA() {
		return txtSoLuongNguoiMA;
	}

	public TextField getTxtGiaTienMA() {
		return txtGiaTienMA;
	}

	public TextArea getTxtMoTaMA() {
		return txtMoTaMA;
	}

	public ImageView getImvHinhAnhMA() {
		return imvHinhAnhMA;
	}

	public void setChosenFileExtension(String chosenFileExtension) {
		this.chosenFileExtension = chosenFileExtension;
	}

	public void setChosenHinhAnh(File chosenHinhAnh) {
		this.chosenHinhAnh = chosenHinhAnh;
	}

	public void setCurrentHinhAnh(String currentHinhAnh) {
		this.currentHinhAnh = currentHinhAnh;
	}

	public TextField getTxtNguyenLieu() {
		return txtNguyenLieu;
	}

	public void setTxtNguyenLieu(TextField txtNguyenLieu) {
		this.txtNguyenLieu = txtNguyenLieu;
	}

	public TabPane getTabPane() {
		return tabPane;
	}

	public Tab getTabThem() {
		return tabThem;
	}

	public Tab getTabTimKiem() {
		return tabTimKiem;
	}
	
	boolean validateThongTin(boolean isEdit) {
		String title = "Thêm món thất bại";
		if(isEdit)
			title = "Sửa món thất bại";
		if(txtTenMonAn.getText().trim().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(title);
			alert.setContentText("Tên món ăn không được trống");
			alert.showAndWait();
			return false;
		}
		
		if(txtNguyenLieu.getText().trim().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(title);
			alert.setContentText("Nguyên liệu món ăn không được trống");
			alert.showAndWait();
			return false;
		}
		
		if(txtMoTaMA.getText().trim().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(title);
			alert.setContentText("Mô tả món ăn không được trống");
			alert.showAndWait();
			return false;
		}
		if(txtGiaTienMA.getText().trim().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(title);
			alert.setContentText("Giá tiền không được bỏ trống");
			alert.showAndWait();
			return false;
		}
		else {
			try {
				int test = Integer.parseInt(txtGiaTienMA.getText());
				if(test < 0 || test > 1000000000) {
					throw new Exception();
				}
			}
			catch(Exception ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(title);
				alert.setContentText("Giá tiền phải là số nguyên >= 0 và <= 1000000000");
				alert.showAndWait();
				return false;
			}
		}
		
		
		if(txtSoLuongNguoiMA.getText().trim().equals("")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(title);
			alert.setContentText("Số lượng người ăn không được bỏ trống");
			alert.showAndWait();
			return false;
		}
		else {
			try {
				int test = Integer.parseInt(txtSoLuongNguoiMA.getText());
				if(test < 1 || test > 1000) {
					throw new Exception();
				}
			}
			catch(Exception ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(title);
				alert.setContentText("Số lượng ghế là số nguyên >= 1 và <= 1000");
				alert.showAndWait();
				return false;
			}
		}
		return true;
	}

	public void themMonAn(ActionEvent e) {
		if(!validateThongTin(false))
			return;
		String tenMonAn = txtTenMonAn.getText();
		String soLuongNguoiString = txtSoLuongNguoiMA.getText();
		String giaTienString = txtGiaTienMA.getText();
		String moTa = txtMoTaMA.getText();
		try {
			int soLuongNguoi = Integer.parseInt(soLuongNguoiString);
			long giaTien = Long.parseLong(giaTienString);

			String dataPath = "images/mon-an/" + chosenHinhAnh.getName();
			MonAn monAn = new MonAn(tenMonAn, moTa, soLuongNguoi, dataPath, giaTien);
			monAn.setNguyenLieu(txtNguyenLieu.getText());
			String id = new MonAnDAO().addMonAn(monAn);

			String imageSavePath = PrimaryConf.CUSTOM_FILE_PATH_HEAD + "images/mon-an/" + id
					+ PrimaryConf.MEAL_IMAGE_DEFAULT_SUFFIX + "." + chosenFileExtension;
			FileInputStream fis = null;
			byte[] bArray = new byte[(int) chosenHinhAnh.length()];
			fis = new FileInputStream(chosenHinhAnh);
			fis.read(bArray);
			OutputStream outStream = new FileOutputStream(imageSavePath);
			outStream.write(bArray);
			fis.close();
			outStream.close();

			loadAllMonAn();
			xoaInput();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thêm món ăn thành công");
			alert.setContentText("Đã thêm món ăn vào hệ thống");
			alert.show();
		} catch (HibernateException he) {
			he.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Thêm món ăn thất bại");
			alert.setContentText("Đã xảy ra sự cố hãy thử lại");
			alert.show();
		} catch (Exception ex) {
			ex.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Thêm món ăn thất bại");
			alert.setContentText("Đã xảy ra sự cố hãy thử lại");
			alert.show();
		}
	}

	public void suaMonAn(ActionEvent e) {
		if(!validateThongTin(false))
			return;
		String tenMonAn = txtTenMonAn.getText();
		String soLuongNguoiString = txtSoLuongNguoiMA.getText();
		String giaTienString = txtGiaTienMA.getText();
		String moTa = txtMoTaMA.getText();
		try {
			int soLuongNguoi = Integer.parseInt(soLuongNguoiString);
			long giaTien = Long.parseLong(giaTienString);

			String newDataPath = "images/mon-an/" + chosenHinhAnh.getName();
			MonAn monAn = new MonAn(idMonAnUpdate, tenMonAn, moTa, soLuongNguoi, newDataPath, giaTien, false);
			monAn.setNguyenLieu(txtNguyenLieu.getText());
			new MonAnDAO().suaMonAn(monAn);

			if (!FilenameUtils.getName(currentHinhAnh).equals(chosenHinhAnh.getName())) {
				File file = new File(currentHinhAnh);
				file.delete();
			}

			String imageSavePath = PrimaryConf.CUSTOM_FILE_PATH_HEAD + "images/mon-an/" + idMonAnUpdate
					+ PrimaryConf.MEAL_IMAGE_DEFAULT_SUFFIX + "." + chosenFileExtension;
			FileInputStream fis = null;
			byte[] bArray = new byte[(int) chosenHinhAnh.length()];
			fis = new FileInputStream(chosenHinhAnh);
			fis.read(bArray);
			fis.close();
			OutputStream outStream = new FileOutputStream(imageSavePath);
			outStream.write(bArray);
			outStream.close();

			loadAllMonAn();
			xoaInput();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Sửa món ăn bàn thành công");
			alert.setContentText("Đã sửa thông tin món ăn vào hệ thống");
			alert.show();
		} catch (HibernateException he) {
			he.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Sửa món ăn thất bại");
			alert.setContentText("Đã xảy ra sự cố hãy thử lại");
			alert.show();
		} catch (Exception ex) {
			ex.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Sửa món ăn thất bại");
			alert.setContentText("Đã xảy ra sự cố hãy thử lại");
			alert.show();
		}
	}

	public void chooseImage(MouseEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose image");
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("image", "*.jpg", "*.png");
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
		if (file != null) {
			hinhAnh = file.getPath();
			Image image = new Image(file.toURI().toString(), 200, 150, false, true);
			chosenHinhAnh = file;
			chosenFileExtension = FilenameUtils.getExtension(file.getPath());
			imvHinhAnhMA.setImage(image);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadAllMonAn();
	}

	public void loadAllMonAn() {
		List<MonAn> list = new MonAnDAO().getDSMonAn();
		loadMonAn(list);
	}

	public void loadMonAn(List<MonAn> list) {
		dsMonAn.getChildren().clear();
		Node node;
		FXMLLoader fx;
		for (MonAn ma : list) {
			try {
				fx = new FXMLLoader(getClass().getResource("/view/ItemMonAn.fxml"));
				node = fx.load();
				node.applyCss();
				ItemMonAnController ict = fx.getController();
				ict.loadData(ma);
				ict.setMonAnMGCT(this);
				dsMonAn.getChildren().add(node);

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	@FXML
    void showAll(ActionEvent event) {
		loadAllMonAn();
    }
	
	Integer tryParseInt(String value) {
		try {
			Integer out = Integer.parseInt(value);
			return out;
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}
	
	Long tryParseLong(String value) {
		try {
			Long out = Long.parseLong(value);
			return out;
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}
	
	@FXML
    void timMonAn(ActionEvent event) {
		String monTim = txtTenMon.getText().trim();
		String soNguoiTim = txtSoNguoi.getText().trim();
		String giaTim = txtGia.getText().trim();
		Integer soNguoiTimInt = tryParseInt(soNguoiTim);
		Long giaTimLong = tryParseLong(giaTim);
		MonAnDAO monAnDao = new MonAnDAO();
		List<MonAn> f = null;
		if(!cbDaHuy.isSelected())
			f = monAnDao.timMonAn(monTim, giaTimLong == null ? -1 : giaTimLong.longValue(), soNguoiTimInt == null ? -1 : soNguoiTimInt.intValue());
		else
			f = monAnDao.timMonAnDaHuy(monTim, giaTimLong == null ? -1 : giaTimLong.longValue(), soNguoiTimInt == null ? -1 : soNguoiTimInt.intValue());
		if (f == null)
			f = new ArrayList<MonAn>();
		loadMonAn(f);
	}
	
	public void xuatMonAnExcel() {
		List<MonAn> dsMonAn = new MonAnDAO().getDSMonAn();
		if(dsMonAn == null)
			dsMonAn = new ArrayList<MonAn>();
		if(dsMonAn.isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi");
			alert.setContentText("Không có món ăn nào để thống kê");
			alert.show();
			return;
    	}
    	else {
    		XSSFWorkbook outputWorkbook = new XSSFWorkbook();
    		XSSFSheet mainSheet = outputWorkbook.createSheet("Danh sách các món ăn");
    		int colNum = 0;
    		int rowNum = 0;
    		// row 1
    		XSSFRow row = mainSheet.createRow(rowNum++);
    		XSSFCell cellRow = row.createCell(colNum++);
    		cellRow.setCellValue("DANH SÁCH CÁC MÓN ĂN");
    		mainSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
    		
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
    		
    		XSSFCellStyle cellStyleContentWrap = outputWorkbook.createCellStyle();
    		XSSFFont cellFontContentWrap = outputWorkbook.createFont();
    		cellFontContentWrap.setFontName("Arial");
    		cellFontContentWrap.setFontHeightInPoints((short) 9);
    		cellFontContentWrap.setBold(false);
    		cellStyleContentWrap.setFont(cellFontContentWrap);
    		cellStyleContentWrap.setWrapText(true);
    		
    		CreationHelper createHelper = outputWorkbook.getCreationHelper();
			XSSFCellStyle cellStyleContentMoney = outputWorkbook.createCellStyle();
    		XSSFFont cellFontContentMoney = outputWorkbook.createFont();
    		cellFontContentMoney.setFontName("Arial");
    		cellFontContentMoney.setFontHeightInPoints((short) 9);
    		cellFontContentMoney.setBold(false);
    		cellStyleContentMoney.setFont(cellFontContentMoney);
    		cellStyleContentMoney.setDataFormat(createHelper.createDataFormat().getFormat("#,##0 [$₫-vi-VN];[Red]#,##0 [$₫-vi-VN]"));
    		
    		System.out.println("Count "+outputWorkbook.getNumCellStyles());
    		// row 2: 
    		row = mainSheet.createRow(rowNum++);
    		colNum = 0;
    		cellRow = row.createCell(colNum++);
    		cellRow.setCellValue("Ngày giờ xuất bản: ");
    		cellRow.setCellStyle(cellStyleContent);
    		colNum++;
    		cellRow = row.createCell(colNum++);
    		cellRow.setCellValue(String.format(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"))));
    		cellRow.setCellStyle(cellStyleContent);
    		mainSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
    		mainSheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 4));
    		// row 3:
    		row = mainSheet.createRow(rowNum++);
    		String headers[] = {"STT", "Mã món ăn", "Tên món ăn", "Số người/phần", "Nguyên liệu", "Mô tả", "Đơn giá"};
    		colNum = 0;
    		for(String hd : headers) {
        		cellRow = row.createCell(colNum++);
        		cellRow.setCellValue(hd);
        		cellRow.setCellStyle(cellStyleHeader);
    		}
    		// row 4-end:
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
    			cellRow.setCellValue(ma.getNguyenLieu());
    			cellRow.setCellStyle(cellStyleContentWrap);
    			cellRow = row.createCell(colNum++);
    			cellRow.setCellValue(ma.getMoTaMA());
    			cellRow.setCellStyle(cellStyleContentWrap);
    			cellRow = row.createCell(colNum++);
    			cellRow.setCellValue(ma.getGiaTien());
    			cellRow.setCellStyle(cellStyleContentMoney);
    		}
    		
    		for(int i = 0; i <= 6; i++) {
    			mainSheet.autoSizeColumn(i, true);
    		}
    		
    		mainSheet.setColumnWidth(4, 8500);
    		mainSheet.setColumnWidth(5, 12000);
    		
    		FileChooser fileChooser = new FileChooser();
    		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel 2007 or newer files (*.xlsx)", "*.xlsx");
    		fileChooser.getExtensionFilters().add(extFilter);
    		File outputExcelFile = fileChooser.showSaveDialog(btnShowAll.getScene().getWindow());
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

	public void xoaInput() {
		idMonAnUpdate = "";
		txtTenMonAn.setText("");
		txtMoTaMA.setText("");
		txtNguyenLieu.setText("");
		txtSoLuongNguoiMA.setText("");
		txtGiaTienMA.setText("");
		txtTenMonAn.requestFocus();
		hinhAnh = "/images/food-view.png";
		Image image = new Image("file:./src/images/food-view.png", 200, 150, false, true);
		chosenHinhAnh = new File("./src/images/food-view.png");
		chosenFileExtension = FilenameUtils.getExtension("./src/images/food-view.png");
		imvHinhAnhMA.setImage(image);
	}
}
