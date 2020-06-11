package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.stage.Stage;
import javafx.util.Callback;

public class ThongKeMonAnController implements Initializable {

    @FXML
    private CheckBox cbBaoGomChuaThanhToan;

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
    private Label lblSoBanDatMon;

    @FXML
    private Label lblSoPhanDD;

    @FXML
    private Label lblSoPhanDD14;

    @FXML
    private Label lblSoPhanDD21;

    @FXML
    private Label lblSoPhanDD28;

    @FXML
    private TextArea txtXuHuongAuto;

    @FXML
    private ImageView imgHinhAnhMA;

    @FXML
    private Label lblDoanhThuMon7;

    @FXML
    private Label lblDoanhThuMon14;

    @FXML
    private Label lblDoanhThuMon21;

    @FXML
    private Label lblDoanhThuMon28;

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
    
    private List<MonAn> dsMonAn;
	private ObservableList<MonAn> dsOBMonAn;
	private List<CTHoaDonBanDat> dsCTHD;
	/**
	 * 0 số phần đặt
	 * 1 tổng doanh thu
	 * 2 số bàn đặt
	 * 3 - 4 số phần 7 và doanh thu 7
	 * 5 - 6 số phần 14 và doanh thu 14
	 * 7 - 8 số phần 21 và doanh thu 21
	 * 9 - 10 số phần 28 và doanh thu 28
	 * 11 ngày đặt cũ nhất
	 */
	private List<List<Object>> thongKeData;
	
	private String textBtnThongKe;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textBtnThongKe = btnThongKe.getText();
		colThuHang.setCellValueFactory(new PropertyValueFactory<MonAn, String>("maMA"));
		colThuHang.setSortable(false);
		colTenMA.setCellValueFactory(new PropertyValueFactory<MonAn, String>("tenMA"));
		colTenMA.setSortable(false);
		colSoNguoi.setCellValueFactory(new PropertyValueFactory<MonAn, Integer>("soLuongNguoi"));
		colSoNguoi.setSortable(false);
		colDonGia.setCellValueFactory(new PropertyValueFactory<MonAn, Long>("giaTien"));
		colDonGia.setSortable(false);
		colSoPhanDD.setCellValueFactory(new Callback<CellDataFeatures<MonAn, Long>, ObservableValue<Long>>() {
			@Override
			public ObservableValue<Long> call(CellDataFeatures<MonAn, Long> param) {
				System.out.println("CELL1 "+lvMonAn.getItems().indexOf(param.getValue()));
				return new ReadOnlyObjectWrapper<Long>((Long)thongKeData.get(lvMonAn.getItems().indexOf(param.getValue())).get(0));
			}
		});
		colSoPhanDD.setSortable(false);
		colTongDoanhThu.setCellValueFactory(new Callback<CellDataFeatures<MonAn, Long>, ObservableValue<Long>>() {
			@Override
			public ObservableValue<Long> call(CellDataFeatures<MonAn, Long> param) {
				System.out.println("CELL2 "+lvMonAn.getItems().indexOf(param.getValue()));
				return new ReadOnlyObjectWrapper<Long>((Long)thongKeData.get(lvMonAn.getItems().indexOf(param.getValue())).get(1));
			}
		});
		colTongDoanhThu.setSortable(false);
		
		lvMonAn.setRowFactory(x -> {
			TableRow<MonAn> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(!row.isEmpty()) {
					lblSoPhanDD.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(0)));
					lblTongDoanhThu.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(1)) + " Đ");
					lblSoBanDatMon.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(2)));
					lblSoPhanDD7.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(3)));
					lblDoanhThuMon7.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(4)) + " Đ");
					lblSoPhanDD14.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(5)));
					lblDoanhThuMon14.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(6)) + " Đ");
					lblSoPhanDD21.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(7)));
					lblDoanhThuMon21.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(8)) + " Đ");
					lblSoPhanDD28.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(9)));
					lblDoanhThuMon28.setText(String.valueOf((Long)thongKeData.get(row.getIndex()).get(10)) + " Đ");
					LocalDate oldest = (LocalDate)thongKeData.get(row.getIndex()).get(11);
					if(oldest.isBefore(LocalDate.now().minusDays(7))) {
						Long d7 = (Long)thongKeData.get(row.getIndex()).get(3);
						Long d14 = (Long)thongKeData.get(row.getIndex()).get(5);
						if(d7 > d14) {
							if(d7 * 1.0 / d14 > 1.1)
								txtXuHuongAuto.setText("Món ăn đang có xu hướng được đặt nhiều hơn");
							else
								txtXuHuongAuto.setText("Món ăn đang có xu hướng ổn định về số lần đặt");
						}
						else if((Long)thongKeData.get(row.getIndex()).get(3) < (Long)thongKeData.get(row.getIndex()).get(5)) {
							if(d14 * 1.0 / d7 > 1.1)
								txtXuHuongAuto.setText("Món ăn đang có xu hướng được ít đi");
							else
								txtXuHuongAuto.setText("Món ăn đang có xu hướng ổn định về số lần đặt");
						}
						else if((Long)thongKeData.get(row.getIndex()).get(3) == (Long)thongKeData.get(row.getIndex()).get(5)) {
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
		
		dsMonAn = new ArrayList<MonAn>();
	}

    @FXML
    void baoGomChuaThanhToanChange(ActionEvent event) {

    }

    @FXML
    void dongGiaoDien(ActionEvent event) {
    	Stage currentStage = (Stage) btnDong.getScene().getWindow();
		currentStage.close();
    }

    @FXML
    void thongKeMonAn(ActionEvent event) {
    	btnThongKe.setText("Đang xử lý...");
    	btnThongKe.setDisable(true);
    	
    	MonAnDAO monAnDao = new MonAnDAO();
    	dsMonAn = monAnDao.getDSMonAn();
    	CTHoaDonBanDatDAO cthdDao = new CTHoaDonBanDatDAO();
    	dsCTHD = cthdDao.getDSCTTBanDatDaThanhToan();
    	thongKeData = new ArrayList<List<Object>>();
    	thongKeData.clear();
    	if(dsCTHD.size() >= 1) {
	    	String currentMaMA = dsCTHD.get(0).getMonAn().getMaMA();
	    	String currentMaBD = "";
	    	ArrayList<Object> firstOb = new ArrayList<Object>();
	    	for(int i = 0; i < 11; i++)
	    		firstOb.add((long)0);
	    	firstOb.add(LocalDate.now());
	    	thongKeData.add(firstOb);
	    	int indexNew = 0;
	    	LocalDate now = LocalDate.now();
	    	LocalDate back7 = now.minusDays(7);
	    	LocalDate back14 = back7.minusDays(7);
	    	LocalDate back21 = back14.minusDays(7);
	    	LocalDate back28 = back21.minusDays(7);
	    	for(int i = 0; i < dsCTHD.size(); i++)
	    	{
	    		CTHoaDonBanDat curr = dsCTHD.get(i);
	    		if(currentMaMA.equals(curr.getMonAn().getMaMA()))
	    		{
	    			List<Object> c = thongKeData.get(indexNew);
		    		System.out.println("BEFORE "+c.get(0) + " " + c.get(1));
	    			thongKeData.get(indexNew).set(0, (long)thongKeData.get(indexNew).get(0) + curr.getSoLuong());
	    			thongKeData.get(indexNew).set(1, (long)thongKeData.get(indexNew).get(1) + curr.getDonGia() * curr.getSoLuong());
	    			List<Object> d = thongKeData.get(indexNew);
		    		System.out.println(d.get(0) + " " + d.get(1));
	    			if(!curr.getTtBanDat().getMaBD().equals(currentMaBD)) {
	    				thongKeData.get(indexNew).set(2, (long)thongKeData.get(indexNew).get(2) + 1);
	    				currentMaBD = curr.getTtBanDat().getMaBD();
	    			}
	    			LocalDate ngayTT = curr.getTtBanDat().getNgayThanhToan().toLocalDateTime().toLocalDate();
	    			if(ngayTT.isBefore((LocalDate)thongKeData.get(indexNew).get(11))) {
	    				thongKeData.get(indexNew).set(11, ngayTT);
	    			}
	    			if(ngayTT.isAfter(back7) && (ngayTT.isBefore(now) || ngayTT.isEqual(now))) {
	    				thongKeData.get(indexNew).set(3, (long)thongKeData.get(indexNew).get(3) + curr.getSoLuong());
		    			thongKeData.get(indexNew).set(4, (long)thongKeData.get(indexNew).get(4) + curr.getDonGia() * curr.getSoLuong());
	    			}
	    			else if(ngayTT.isAfter(back14) && (ngayTT.isBefore(back7) || ngayTT.isEqual(back7))) {
	    				thongKeData.get(indexNew).set(5, (long)thongKeData.get(indexNew).get(5) + curr.getSoLuong());
		    			thongKeData.get(indexNew).set(6, (long)thongKeData.get(indexNew).get(6) + curr.getDonGia() * curr.getSoLuong());
	    			}
	    			else if(ngayTT.isAfter(back21) && (ngayTT.isBefore(back14) || ngayTT.isEqual(back14))) {
	    				thongKeData.get(indexNew).set(7, (long)thongKeData.get(indexNew).get(7) + curr.getSoLuong());
		    			thongKeData.get(indexNew).set(8, (long)thongKeData.get(indexNew).get(8) + curr.getDonGia() * curr.getSoLuong());
	    			}
	    			else if(ngayTT.isAfter(back28) && (ngayTT.isBefore(back21) || ngayTT.isEqual(back21))) {
	    				thongKeData.get(indexNew).set(9, (long)thongKeData.get(indexNew).get(9) + curr.getSoLuong());
		    			thongKeData.get(indexNew).set(10, (long)thongKeData.get(indexNew).get(10) + curr.getDonGia() * curr.getSoLuong());
	    			}
	    		}
	    		else
	    		{
	    			indexNew++;
	    			currentMaMA = curr.getMonAn().getMaMA();
	    			i--;
	    			ArrayList<Object> firstOb2 = new ArrayList<Object>();
	    	    	for(int a = 0; a < 11; a++)
	    	    		firstOb2.add((long)0);
	    	    	firstOb2.add(LocalDate.now());
	    	    	thongKeData.add(firstOb2);
	    			continue;
	    		}
	    	}
	    	for(int i = 0; i < thongKeData.size(); i++) {
	    		List<Object> d = thongKeData.get(i);
	    		System.out.println(d.get(0) + " " + d.get(1));
	    	}
    	}
    	dsOBMonAn = FXCollections.observableArrayList(dsMonAn);
    	lvMonAn.setItems(dsOBMonAn);
    	
    	btnThongKe.setText(textBtnThongKe);
    	btnThongKe.setDisable(false);
    }
    
    void tinhThongSo(MonAn ma)
    {
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
