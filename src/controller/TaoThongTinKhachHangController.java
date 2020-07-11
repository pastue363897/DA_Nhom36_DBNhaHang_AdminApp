package controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import database.AccountDAO;
import database.CustomerDAO;
import entites.Account;
import entites.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.bytebuddy.utility.RandomString;

public class TaoThongTinKhachHangController implements Initializable {
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
	private Button btnThemKhachHang;

	@FXML
	private TextField txtPassword;

	@FXML
	private Button btnRandomPass;

	@FXML
	private Button btnClose;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	void dongGiaoDien(ActionEvent event) {
		Stage currentStage = (Stage) btnClose.getScene().getWindow();
		currentStage.close();
	}

	@FXML
	void randomPass(ActionEvent event) {
		txtPassword.setText(new RandomString(8).nextString().concat("@"));
	}

	@FXML
    void themKhachHang(ActionEvent event) {
    	boolean autoGeneratePass = false;
    	if(txtTenKH.getText().equals("")) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thêm khách hàng");
			alert.setContentText("Tên không được bỏ trống");
			alert.show();
			return;
    	}
    	else if(!txtTenKH.getText().matches("^\\p{L}{1,7}( \\p{L}{1,7}){0,5}$")) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thêm khách hàng");
			alert.setContentText("Tên không hợp lệ");
			alert.show();
			return;
    	}
    	else if(txtSoCMND.getText().equals("")) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thêm khách hàng");
			alert.setContentText("Số CMND được bỏ trống");
			alert.show();
			return;
    	}
    	else if(!txtSoCMND.getText().matches("^[1-9](\\d{8}|\\d{11})$")) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thêm khách hàng");
			alert.setContentText("Số CMND/CCCD không hợp lệ, số CMND/CCCD là 9 hay 12 số");
			alert.show();
			return;
    	}
    	else if(txtUsername.getText().equals("")) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thêm khách hàng");
			alert.setContentText("Username không được bỏ trống");
			alert.show();
			return;
    	}
    	else if(!txtUsername.getText().matches("^[a-zA-Z][a-zA-Z0-9]{2,19}")) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thêm khách hàng");
			alert.setContentText("Username không hợp lệ, phải bắt đầu chữ cái sau đó là số hoặc chữ cái, độ dài từ 3-20 ký tự");
			alert.show();
			return;
    	}
    	else if(txtPassword.getText().equals("")) {
    		ButtonType autoChange = new ButtonType("Tự phát sinh và thêm", ButtonData.OK_DONE);
    		ButtonType cancel = new ButtonType("Hủy", ButtonData.CANCEL_CLOSE);
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Mật khẩu đang bị trống? Tự phát sinh mật khẩu và thêm khách hàng, hoặc chọn hủy và tự nhập", autoChange, cancel);
			alert.setTitle("Lỗi thêm khách hàng");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.orElse(cancel) == cancel)
				return;
			else
				autoGeneratePass = true;
    	}
    	AccountDAO accountDao = new AccountDAO();
    	Account testExist = accountDao.getAccountByUsername(txtUsername.getText());
    	if(testExist != null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thêm khách hàng");
			alert.setContentText("Username đã được dùng");
			alert.show();
			return;
    	}
    	if(autoGeneratePass)
    		randomPass(null);
    	Customer newCus = new Customer(txtTenKH.getText(), txtDiaChi.getText(), txtSoCMND.getText(), txtSoDT.getText(), txtEmail.getText(), new Account(txtUsername.getText(), txtPassword.getText()));
    	CustomerDAO customerDao = new CustomerDAO();
    	String cusSuccess = customerDao.addCustomerReturn(newCus);
    	if(cusSuccess == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Lỗi thêm khách hàng");
			alert.setContentText("Không thể thêm khách hàng, hãy thử lại");
			alert.show();
			return;
    	}
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Thêm khách hàng thành công");
		alert.setContentText("Đã thêm thành công, mã KH được phát sinh là "+cusSuccess);
		alert.showAndWait();
		dongGiaoDien(null);
    }

}
