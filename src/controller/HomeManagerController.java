/**
 * Created on: 22:08:18 25 thg 4, 2020
 * 
 * @author Dinh Van Dung YKNB, Ta Khanh Hoang
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeManagerController implements Initializable {
	@FXML
	private Button btnBanDatManager;
	@FXML
	private Button btnBanAnManager;
	@FXML
	private Button btnMonAnManager;
	@FXML
	private Button btnSignOut;

	@FXML
	private AnchorPane banDat;
	@FXML
	private AnchorPane banAn;
	@FXML
	private AnchorPane monAn;

	public void navigationBar(ActionEvent e) {
		if (e.getSource() == btnBanDatManager) {
			banDat.setVisible(true);
			banAn.setVisible(false);
			monAn.setVisible(false);
		} else if (e.getSource() == btnBanAnManager) {
			banDat.setVisible(false);
			banAn.setVisible(true);
			monAn.setVisible(false);
		} else if (e.getSource() == btnMonAnManager) {
			banDat.setVisible(false);
			banAn.setVisible(false);
			monAn.setVisible(true);
		}
	}

	public void signOut(ActionEvent e) {
		Stage current = (Stage) btnSignOut.getScene().getWindow();
		current.close();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Hệ thống quản lý đặt bàn nhà hàng");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		banDat.setVisible(true);
		banAn.setVisible(false);
		monAn.setVisible(false);
	}
}
