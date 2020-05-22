package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import database.CustomerDAO;
import entites.BanAn;
import entites.Customer;
import entites.HoaDonBanDat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class QuanLyKhachHangController implements Initializable {

    @FXML
    private TableView<Customer> lvKhachHang;

    @FXML
    private TableColumn<Customer, String> maKH;

    @FXML
    private TableColumn<Customer, String> tenKH;

    @FXML
    private TableColumn<Customer, String> soCMND;

    @FXML
    private TableColumn<Customer, String> usernameKH;

    @FXML
    private TextField txtMaKH;

    @FXML
    private Button btnTimBanDat;

    @FXML
    private Button btnReset;

    @FXML
    private TextField txtTenKH;

    @FXML
    private TextArea txtDiaChi;

    @FXML
    private TextField txtSoCMND;

    @FXML
    private TextField txtSoDT;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnTim;

    @FXML
    private Button btnThemKhachHang;
    
    private List<Customer> dsKhachHang;
    private ObservableList<Customer> dsOBKhachHang;
    
    public static Stage primaryStage;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	maKH.setCellValueFactory(new Callback<CellDataFeatures<Customer, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Customer, String> param) {
				return new SimpleStringProperty(param.getValue().getTaiKhoan().getMaTK());
			}
		});
    	tenKH.setCellValueFactory(new PropertyValueFactory<Customer, String>("hoTen"));
    	soCMND.setCellValueFactory(new PropertyValueFactory<Customer, String>("cmnd"));
    	usernameKH.setCellValueFactory(new Callback<CellDataFeatures<Customer, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Customer, String> param) {
				return new SimpleStringProperty(param.getValue().getTaiKhoan().getUsername());
			}
		});
    	
    	dsKhachHang = new ArrayList<Customer>();
    	
    	lvKhachHang.setRowFactory(x -> {
			TableRow<Customer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(!row.isEmpty()) {
					Customer f = row.getItem();
					txtMaKH.setText(f.getTaiKhoan().getMaTK());
					txtTenKH.setText(f.getHoTen());
					txtDiaChi.setText(f.getDiaChi());
					txtSoCMND.setText(f.getCmnd());
					txtSoDT.setText(f.getSdt());
					txtEmail.setText(f.getEmail());
					txtUsername.setText(f.getTaiKhoan().getUsername());
				}
			});
			return row;
		});
	}
    
    @FXML
    void resetDSKhachHang(ActionEvent event) {
    	dsKhachHang.clear();
    	dsOBKhachHang = FXCollections.observableArrayList(dsKhachHang);
    	lvKhachHang.setItems(dsOBKhachHang);
    	txtMaKH.setText("");
		txtTenKH.setText("");
		txtDiaChi.setText("");
		txtSoCMND.setText("");
		txtSoDT.setText("");
		txtEmail.setText("");
		txtUsername.setText("");
    }

    @FXML
    void timKhachHang(ActionEvent event) {
    	CustomerDAO customerDao = new CustomerDAO();
    	dsKhachHang = customerDao.timKhachHang(txtSearch.getText());
    	dsOBKhachHang = FXCollections.observableArrayList(dsKhachHang);
    	lvKhachHang.setItems(dsOBKhachHang);
    }
    
    @FXML
    void taoKhachHangMoi(ActionEvent event) {
    	Parent root;
    	try {
			root = FXMLLoader.load(getClass().getResource("/view/TaoThongTinKhachHangManager.fxml"));
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(QuanLyKhachHangController.primaryStage);
			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng - Thêm khách hàng mới");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.centerOnScreen();
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
}