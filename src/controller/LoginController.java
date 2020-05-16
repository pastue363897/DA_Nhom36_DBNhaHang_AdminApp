/**
 * Created on: 19:29:11 2 thg 5, 2020
 * @author Dinh Van Dung YKNB, Ta Khanh Hoang
 */

package controller;

import java.io.IOException;

import database.AdminDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private TextField txtUserName;
	@FXML
	private PasswordField txtPassWord;
	@FXML
	private Button btnSignin;

	@FXML
	void signIn(ActionEvent event) {
		// dang nhap ko password de debug: nhap username: DEBUG_LOGIN rui sign in
		String username = txtUserName.getText();
		String password = txtPassWord.getText();
		AdminDAO adminDao = new AdminDAO();
		int ref = adminDao.signIn(username, password);
		if (ref != 1) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Đăng nhập thất bại");
			alert.setContentText("Sai username hay mật khẩu, hãy kiểm tra lại.");
			alert.show();
		} else {
			try {
				// Close sign in window
				Stage currentStage = (Stage) btnSignin.getScene().getWindow();
				currentStage.close();
				// load main
				Parent root = FXMLLoader.load(getClass().getResource("/view/HomeManager.fxml"));
				Stage stage = new Stage();
				stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng");
				Scene scene = new Scene(root);
				stage.setScene(scene);
				HomeManagerController.primaryStage = stage;
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
	}
}
