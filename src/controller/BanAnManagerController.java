/**
 * Created on: 14:58:41 26 thg 4, 2020
 * @author Dinh Van Dung YKNB, Ta Khanh Hoang
 */

package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import database.BanAnDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entites.BanAn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class BanAnManagerController implements Initializable {

	@FXML
	private TableView<BanAn> danhSachBanAn;
	@FXML
	private TableColumn<BanAn, String> hinhAnhColumn;
	@FXML
	private TableColumn<BanAn, String> idColumn;
	@FXML
	private TableColumn<BanAn, String> tenMonAnColumn;
	@FXML
	private TableColumn<BanAn, String> moTaColumn;
	@FXML
	private TableColumn<BanAn, Integer> soLuongNguoiColumn;
	@FXML
	private TableColumn<BanAn, Long> giaTienColumn;
	@FXML
	private TableColumn<BanAn, Void> huyBAColumn;
	@FXML
	private Button btnThemBanAn;
	@FXML
	private ImageView imvHinhAnhBA;
	@FXML
	private TextField txtKySoBanAn;
	@FXML
	private TextField txtSoLuongNguoiBA;
	@FXML
	private TextField txtGiaTienBA;
	@FXML
	private TextArea txtMoTaBA;

	private ObservableList<BanAn> listBanAn;

	private static String hinhAnh = "/images/icon-food-and-drink-hd-png-download.png";

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
		soLuongNguoiString = txtSoLuongNguoiBA.getText();
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

			BanAn banAn = new BanAn(new BanAnDAO().generateID(), kySoBanAn, soLuongNguoi, moTa, giaTien, true, false,
					hinhAnh);
			new BanAnDAO().save(banAn);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thêm bàn thành công");
			alert.setContentText("Đã thêm bàn vào hệ thống");
			alert.show();
			loadBanAn();
			/* } */

		} catch (HibernateException ex1) {
			// ex1.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Thêm bàn thất bại");
			alert.setContentText("Đã xảy ra sự cố hãy thử lại");
			alert.show();
		} catch (Exception ex) {
			// ex.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Thêm bàn thất bại");
			alert.setContentText("Đã xảy ra sự cố hãy thử lại");
			alert.show();
		}
	}

	@FXML
	void suaBanAn(ActionEvent e) {
		String kySoBanAn = txtKySoBanAn.getText();
		String soLuongNguoiString = txtSoLuongNguoiBA.getText();
		String giaTienString = txtGiaTienBA.getText();
		String moTa = txtMoTaBA.getText();

		try {
			int soLuongNguoi = Integer.parseInt(soLuongNguoiString);
			long giaTien = Long.parseLong(giaTienString);

			BanAn banAn = new BanAn(danhSachBanAn.getSelectionModel().getSelectedItem().getMaBA(), kySoBanAn,
					soLuongNguoi, moTa, giaTien, true, false, hinhAnh);
			new BanAnDAO().update(banAn);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Cập nhật bàn thành công");
			alert.setContentText("Đã cập nhật bàn vào hệ thống");
			alert.show();
			loadBanAn();

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
		hinhAnhColumn.setCellValueFactory(new PropertyValueFactory<BanAn, String>("hinhAnhBA"));
		idColumn.setCellValueFactory(new PropertyValueFactory<BanAn, String>("maBA"));
		tenMonAnColumn.setCellValueFactory(new PropertyValueFactory<BanAn, String>("kySoBA"));
		moTaColumn.setCellValueFactory(new PropertyValueFactory<BanAn, String>("motaBA"));
		soLuongNguoiColumn.setCellValueFactory(new PropertyValueFactory<BanAn, Integer>("soLuongGhe"));
		giaTienColumn.setCellValueFactory(new PropertyValueFactory<BanAn, Long>("giaTien"));
		huyBAColumn.setCellFactory(param -> new TableCell<BanAn, Void>() {
			private Button btnHuy = new Button("Hủy");

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				btnHuy.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							System.out.println("WF: "+getIndex());
							danhSachBanAn.getSelectionModel().clearSelection();
							danhSachBanAn.getSelectionModel().select(getIndex());
							BanAn banAn = danhSachBanAn.getSelectionModel().getSelectedItem();
							new BanAnDAO().delete(banAn);
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("Xóa bàn thành công");
							alert.setContentText("Đã xóa bàn ăn trong hệ thống");
							alert.show();
							loadBanAn();
						} catch (HibernateException ex1) {
							ex1.printStackTrace();
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Xóa bàn thất bại");
							alert.setContentText("Đã xảy ra sự cố hãy thử lại");
							alert.show();
						} catch (Exception ex) {
							ex.printStackTrace();
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Xóa bàn thất bại");
							alert.setContentText("Đã xảy ra sự cố hãy thử lại");
							alert.show();
						}
					}
				});
				btnHuy.setTextFill(Color.WHITE);
				FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE);
				icon.setSize("18");
				icon.setFill(Color.WHITE);
				btnHuy.setGraphic(icon);
				setGraphic(empty ? null : btnHuy);
			}
		});
		loadBanAn();
	}

	public void loadBanAn() {
		List<BanAn> list = new BanAnDAO().getAll();
		listBanAn = FXCollections.observableArrayList(list);
		danhSachBanAn.setItems(listBanAn);
	}
}
