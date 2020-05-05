/**
 * Created on: 14:58:41 26 thg 4, 2020
 * @author Dinh Van Dung YKNB, Ta Khanh Hoang
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
import database.BanAnDAO;
import entites.BanAn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;

public class BanAnManagerController implements Initializable {

	private String idBanAnUpdate;
	@FXML
	private Button btnThemBanAn;
	@FXML
	private ImageView imvHinhAnhBA;
	@FXML
	private TextField txtKySoBanAn;
	@FXML
	private TextField txtSoLuongGheBA;
	@FXML
	private TextField txtGiaTienBA;
	@FXML
	private TextArea txtMoTaBA;

	@FXML
	private FlowPane dsBanAn;

	private String chosenFileExtension = FilenameUtils.getExtension("./src/images/table-view.png");
	private File chosenHinhAnh = new File("./src/images/table-view.png");
	private String currentHinhAnh;
	private static String hinhAnh = "/images/table-view.png";

	public ImageView getImvHinhAnhBA() {
		return imvHinhAnhBA;
	}

	public TextField getTxtKySoBanAn() {
		return txtKySoBanAn;
	}

	public TextField getTxtSoLuongGheBA() {
		return txtSoLuongGheBA;
	}

	public TextField getTxtGiaTienBA() {
		return txtGiaTienBA;
	}

	public TextArea getTxtMoTaBA() {
		return txtMoTaBA;
	}

	public void setIdBanAnUpdate(String idBanAnUpdate) {
		this.idBanAnUpdate = idBanAnUpdate;
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

	@FXML
	void themBanAn(ActionEvent e) {
		String kySoBanAn = txtKySoBanAn.getText();
		String soLuongNguoiString = "";
		String giaTienString = "";
		String moTa = "";
		/*
		 * if(kySoBanAn.equals("DEBUG-ADD-REPEAT-10000")) {
		 * 
		 * } else {
		 */
		soLuongNguoiString = txtSoLuongGheBA.getText();
		giaTienString = txtGiaTienBA.getText();
		moTa = txtMoTaBA.getText();
		/* } */

		try {
			/*
			 * if(kySoBanAn.equals("DEBUG-ADD-REPEAT-10000")) { for(int i = 0; i < 10000;
			 * i++) { kySoBanAn = "D-"+String.valueOf(i); soLuongNguoiString =
			 * String.valueOf((int)(15*Math.random()+1)); giaTienString =
			 * String.valueOf((int)(150000*Math.random()+50000)); moTa =
			 * "Bàn ăn context test"; int soLuongNguoi =
			 * Integer.parseInt(soLuongNguoiString); long giaTien =
			 * Long.parseLong(giaTienString); System.out.println("OK2! "+i); BanAn banAn2 =
			 * new BanAn(new BanAnDAO().generateID(), kySoBanAn, soLuongNguoi, moTa,
			 * giaTien, true, false, hinhAnh); new BanAnDAO().save(banAn2); } return; } else
			 * {
			 */
			int soLuongNguoi = Integer.parseInt(soLuongNguoiString);
			long giaTien = Long.parseLong(giaTienString);

			String dataPath = "images/ban-an/" + chosenHinhAnh.getName();
			BanAn banAn = new BanAn(kySoBanAn, soLuongNguoi, moTa, giaTien, true, false, dataPath);
			String id = new BanAnDAO().addBanAn(banAn);

			String imageSavePath = PrimaryConf.CUSTOM_FILE_PATH_HEAD + "images/ban-an/" + id
					+ PrimaryConf.TABLE_IMAGE_DEFAULT_SUFFIX + "." + chosenFileExtension;
			FileInputStream fis = null;
			byte[] bArray = new byte[(int) chosenHinhAnh.length()];
			fis = new FileInputStream(chosenHinhAnh);
			fis.read(bArray);
			OutputStream outStream = new FileOutputStream(imageSavePath);
			outStream.write(bArray);
			fis.close();
			outStream.close();

			xoaInput();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thêm bàn thành công");
			alert.setContentText("Đã thêm bàn vào hệ thống");
			alert.show();
			loadAllBanAn();
			/* } */

		} catch (HibernateException ex1) {
			// ex1.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Thêm bàn thất bại");
			alert.setContentText("Đã xảy ra sự cố hãy thử lại");
			alert.show();
		} catch (Exception ex) {
			ex.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Thêm bàn thất bại");
			alert.setContentText("Đã xảy ra sự cố hãy thử lại");
			alert.show();
		}
	}

	@FXML
	void suaBanAn(ActionEvent e) {
		String kySoBanAn = txtKySoBanAn.getText();
		String soLuongNguoiString = txtSoLuongGheBA.getText();
		String giaTienString = txtGiaTienBA.getText();
		String moTa = txtMoTaBA.getText();

		try {
			int soLuongNguoi = Integer.parseInt(soLuongNguoiString);
			long giaTien = Long.parseLong(giaTienString);

			String newDataPath = "images/ban-an/" + chosenHinhAnh.getName();
			BanAn banAn = new BanAn(idBanAnUpdate, kySoBanAn, soLuongNguoi, moTa, giaTien, true, false, newDataPath);
			new BanAnDAO().suaBanAn(banAn);

			if (!FilenameUtils.getName(currentHinhAnh).equals(chosenHinhAnh.getName())) {
				File file = new File(currentHinhAnh);
				file.delete();
			}

			String imageSavePath = PrimaryConf.CUSTOM_FILE_PATH_HEAD + "images/ban-an/" + idBanAnUpdate
					+ PrimaryConf.TABLE_IMAGE_DEFAULT_SUFFIX + "." + chosenFileExtension;
			FileInputStream fis = null;
			byte[] bArray = new byte[(int) chosenHinhAnh.length()];
			fis = new FileInputStream(chosenHinhAnh);
			fis.read(bArray);
			fis.close();
			OutputStream outStream = new FileOutputStream(imageSavePath);
			outStream.write(bArray);
			outStream.close();

			xoaInput();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Cập nhật bàn thành công");
			alert.setContentText("Đã cập nhật bàn vào hệ thống");
			alert.show();
			loadAllBanAn();

		} catch (HibernateException ex1) {
			// ex1.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cập nhật bàn thất bại");
			alert.setContentText("Đã xảy ra sự cố hãy thử lại");
			alert.show();
		} catch (Exception ex) {
			// ex.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cập nhật bàn thất bại");
			alert.setContentText("Đã xảy ra sự cố hãy thử lại");
			alert.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadAllBanAn();
	}

	public void loadAllBanAn() {
		List<BanAn> list = new BanAnDAO().getAll();
		loadBanAn(list);
	}

	public void loadBanAn(List<BanAn> list) {
		dsBanAn.getChildren().clear();
		Node node;
		FXMLLoader fx;
		for (BanAn b : list) {
			try {
				fx = new FXMLLoader(getClass().getResource("/view/ItemBanAn.fxml"));
				node = fx.load();
				node.applyCss();
				ItemBanAnController ict = fx.getController();
				ict.loadData(b);
				ict.setBanAnMGCT(this);
				dsBanAn.getChildren().add(node);

			} catch (IOException ex) {
				ex.printStackTrace();
			}
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
			imvHinhAnhBA.setImage(image);
		}
	}

	public void xoaInput() {
		idBanAnUpdate = "";
		txtKySoBanAn.setText("");
		txtMoTaBA.setText("");
		txtSoLuongGheBA.setText("");
		txtGiaTienBA.setText("");
		txtKySoBanAn.requestFocus();
		hinhAnh = "/images/table-view.png";
		Image image = new Image("file:./src/images/table-view.png", 200, 150, false, true);
		chosenHinhAnh = new File("./src/images/table-view.png");
		chosenFileExtension = FilenameUtils.getExtension("./src/images/table-view.png");
		imvHinhAnhBA.setImage(image);
	}
}
