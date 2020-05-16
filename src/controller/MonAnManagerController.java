/**
 * Created on: 14:44:24 26 thg 4, 2020
 * 
 * @author Dinh Van Dung YKNB
 */

package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;
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
	private TextField txtTenMonAn, txtSoLuongNguoiMA, txtGiaTienMA;
	@FXML
	private TextArea txtMoTaMA;
	@FXML
	private ImageView imvHinhAnhMA;

	@FXML
	private FlowPane dsMonAn;

	private String chosenFileExtension = FilenameUtils.getExtension("./src/images/food-view.png");
	private File chosenHinhAnh = new File("./src/images/food-view.png");
	private String currentHinhAnh;
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

	public void themMonAn(ActionEvent e) {
		String tenMonAn = txtTenMonAn.getText();
		String soLuongNguoiString = txtSoLuongNguoiMA.getText();
		String giaTienString = txtGiaTienMA.getText();
		String moTa = txtMoTaMA.getText();
		try {
			int soLuongNguoi = Integer.parseInt(soLuongNguoiString);
			long giaTien = Long.parseLong(giaTienString);

			String dataPath = "images/mon-an/" + chosenHinhAnh.getName();
			MonAn monAn = new MonAn(tenMonAn, moTa, soLuongNguoi, dataPath, giaTien);
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
		String tenMonAn = txtTenMonAn.getText();
		String soLuongNguoiString = txtSoLuongNguoiMA.getText();
		String giaTienString = txtGiaTienMA.getText();
		String moTa = txtMoTaMA.getText();
		try {
			int soLuongNguoi = Integer.parseInt(soLuongNguoiString);
			long giaTien = Long.parseLong(giaTienString);

			String newDataPath = "images/mon-an/" + chosenHinhAnh.getName();
			MonAn monAn = new MonAn(idMonAnUpdate, tenMonAn, moTa, soLuongNguoi, newDataPath, giaTien, false);
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
		List<MonAn> list = new MonAnDAO().getAll();
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

	public void xoaInput() {
		idMonAnUpdate = "";
		txtTenMonAn.setText("");
		txtMoTaMA.setText("");
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
