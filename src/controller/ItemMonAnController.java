/**
 * Created on: 21:24:22 30 thg 4, 2020
 * 
 * @author Dinh Van Dung YKNB
 */

package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.HibernateException;

import application.PrimaryConf;
import database.MonAnDAO;
import entites.MonAn;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class ItemMonAnController implements Initializable {
	@FXML
	private Pane panBlur;
	@FXML
	private Button btnSuaMonAn;
	@FXML
	private Button btnHuyMonAn;
	@FXML
	private Label lblTenMA, lblMoTaMA, lblGiaTienMA, lblSoLuongNguoiMA;
	@FXML
	private ImageView hinhAnhMonAn;

	private MonAnManagerController monAnMGCT;
	private MonAn ttMonAn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		panBlur.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				btnSuaMonAn.setVisible(true);
				btnHuyMonAn.setVisible(true);
			} else {
				btnSuaMonAn.setVisible(false);
				btnHuyMonAn.setVisible(false);
			}
		});
	}

	public void loadData(MonAn monAn) {
		lblTenMA.setText(monAn.getTenMA().toUpperCase());
		lblMoTaMA.setText(monAn.getMoTaMA());
		lblSoLuongNguoiMA.setText(String.valueOf(monAn.getSoLuongNguoi()));
		lblGiaTienMA.setText(String.valueOf(monAn.getGiaTien()) + " Đ");
		Image image = new Image("file:" + PrimaryConf.CUSTOM_FILE_PATH_HEAD + monAn.getHinhAnhMA(), 200, 150, false,
				true);
		hinhAnhMonAn.setImage(image);
		ttMonAn = monAn;
	}

	public void setMonAnMGCT(MonAnManagerController monAnMGCT) {
		this.monAnMGCT = monAnMGCT;
	}

	public void suaMonAn(ActionEvent e) {
		monAnMGCT.getTxtTenMonAn().setText(ttMonAn.getTenMA());
		monAnMGCT.getTxtMoTaMA().setText(ttMonAn.getMoTaMA());
		monAnMGCT.getTxtSoLuongNguoiMA().setText(String.valueOf(ttMonAn.getSoLuongNguoi()));
		monAnMGCT.getTxtGiaTienMA().setText(String.valueOf(ttMonAn.getGiaTien()));
		monAnMGCT.getTxtNguyenLieu().setText(ttMonAn.getNguyenLieu());
		File chosenHinhAnh = new File(PrimaryConf.CUSTOM_FILE_PATH_HEAD + ttMonAn.getHinhAnhMA());
		monAnMGCT.getImvHinhAnhMA().setImage(
				new Image("file:" + PrimaryConf.CUSTOM_FILE_PATH_HEAD + ttMonAn.getHinhAnhMA(), 200, 165, false, true));
		monAnMGCT.setIdMonAnUpdate(ttMonAn.getMaMA());
		monAnMGCT.setChosenHinhAnh(chosenHinhAnh);
		monAnMGCT.setCurrentHinhAnh(PrimaryConf.CUSTOM_FILE_PATH_HEAD + ttMonAn.getHinhAnhMA());
		monAnMGCT.setChosenFileExtension(FilenameUtils.getExtension(chosenHinhAnh.getPath()));
	}

	public void huyMonAn(ActionEvent e) {
		monAnMGCT.xoaInput();
		try {
			MonAnDAO maDao = new MonAnDAO();
			if (maDao.checkPreviouslyBooked(ttMonAn)) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Món ăn đã từng được đặt ít nhất 1 lần, có xác nhận không bán món này nữa?", ButtonType.YES, ButtonType.NO);
				alert.setTitle("Xác nhận?");
				alert.showAndWait();
				
				if (alert.getResult() == ButtonType.YES) {
					maDao.setIsDeleted(ttMonAn);
					monAnMGCT.loadAllMonAn();
					
					Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
					alert2.setTitle("Hủy bán món ăn thành công");
					alert2.setContentText("Đã hủy bán món ăn trong hệ thống");
					alert2.show();
				}
				
			} else {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Có thực sự xóa món ăn nàt?", ButtonType.YES, ButtonType.NO);
				alert.setTitle("Xác nhận?");
				alert.showAndWait();
				
				if (alert.getResult() == ButtonType.YES) {
					String path = PrimaryConf.CUSTOM_FILE_PATH_HEAD.concat(ttMonAn.getHinhAnhMA());
					File file = new File(path);
					if (!file.delete()) {
						throw new Exception("Không xóa được file, không tiếp tục xóa bàn ăn");
					}
					maDao.delete(ttMonAn);
					
					
					Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
					alert2.setTitle("Xóa món ăn bàn thành công");
					alert2.setContentText("Đã xóa món ăn trong hệ thống");
					alert2.show();
					monAnMGCT.loadAllMonAn();
				}
			}
		} catch (HibernateException he) {
			he.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Xóa món ăn thất bại");
			alert.setContentText("Đã xảy ra sự cố, hãy thử lại");
			alert.show();
		} catch (Exception ex) {
			ex.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Xóa món ăn thất bại");
			alert.setContentText("Đã xảy ra sự cố, hãy thử lại");
			alert.show();
		}
	}
}
