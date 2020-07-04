/**
 * Created on: 01:58:53 1 thg 5, 2020
 * 
 * @author Dinh Van Dung YKNB, Ta Khanh Hoang
 */

package controller;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.HibernateException;

import application.PrimaryConf;
import database.BanAnDAO;
import database.HoaDonBanDatDAO;
import entites.BanAn;
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

public class ItemBanAnController implements Initializable {

	@FXML
	private Pane panBlur;
	@FXML
	private Button btnSuaBanAn;
	@FXML
	private Button btnHuyBanAn;
	@FXML
	private Label lblKySoBA, lblMoTaBA, lblGiaTienBA, lblSoLuongGheBA;
	@FXML
	private ImageView hinhAnhBanAn;

	private BanAnManagerController banAnMGCT;
	private BanAn ttBanAn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		panBlur.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				btnSuaBanAn.setVisible(true);
				btnHuyBanAn.setVisible(true);
			} else {
				btnSuaBanAn.setVisible(false);
				btnHuyBanAn.setVisible(false);
			}
		});
	}

	public void loadData(BanAn b) {
		lblKySoBA.setText(b.getKySoBA().toUpperCase());
		lblMoTaBA.setText(b.getMotaBA());
		lblSoLuongGheBA.setText(String.valueOf(b.getSoLuongGhe()));
		lblGiaTienBA.setText(String.valueOf(b.getPhuGia()) + " Đ");
		Image image = new Image("file:" + PrimaryConf.CUSTOM_FILE_PATH_HEAD + b.getHinhAnh(), 200, 150, false, true);
		hinhAnhBanAn.setImage(image);
		ttBanAn = b;
	}

	public void setBanAnMGCT(BanAnManagerController banAnMGCT) {
		this.banAnMGCT = banAnMGCT;
	}

	public void suaBanAn(ActionEvent e) {
		banAnMGCT.getTxtKySoBanAn().setText(ttBanAn.getKySoBA());
		banAnMGCT.getTxtMoTaBA().setText(ttBanAn.getMotaBA());
		banAnMGCT.getTxtSoLuongGheBA().setText(String.valueOf(ttBanAn.getSoLuongGhe()));
		banAnMGCT.getTxtGiaTienBA().setText(String.valueOf(ttBanAn.getPhuGia()));
		File chosenHinhAnh = new File(PrimaryConf.CUSTOM_FILE_PATH_HEAD + ttBanAn.getHinhAnh());
		banAnMGCT.getImvHinhAnhBA().setImage(
				new Image("file:" + PrimaryConf.CUSTOM_FILE_PATH_HEAD + ttBanAn.getHinhAnh(), 200, 165, false, true));
		banAnMGCT.setIdBanAnUpdate(ttBanAn.getMaBA());
		banAnMGCT.setChosenHinhAnh(chosenHinhAnh);
		banAnMGCT.setCurrentHinhAnh(PrimaryConf.CUSTOM_FILE_PATH_HEAD + ttBanAn.getHinhAnh());
		banAnMGCT.setChosenFileExtension(FilenameUtils.getExtension(chosenHinhAnh.getPath()));
	}

	public void huyBanAn(ActionEvent e) {
		BanAnDAO banAnDao = new BanAnDAO();
		HoaDonBanDatDAO hoaDonDao = new HoaDonBanDatDAO();
		if(hoaDonDao.checkBanDaDat(ttBanAn.getMaBA(), Timestamp.valueOf(LocalDateTime.now()))) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Xóa bàn thất bại");
			alert.setContentText("Bàn đang được đặt không xóa được");
			alert.showAndWait();
			return;
		}
		if(banAnDao.checkPreviouslyBooked(ttBanAn)) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bàn đã từng được đặt ít nhất 1 lần, có xác nhận không dùng bàn này nữa?", ButtonType.YES, ButtonType.NO);
			alert.setTitle("Xác nhận?");
			alert.showAndWait();
			
			if (alert.getResult() == ButtonType.YES) {
				try {
				    ttBanAn.setCoBan(false);
				    BanAn f = new BanAnDAO().update(ttBanAn);
				    if(f != null) {
					    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
						alert2.setTitle("Gỡ dùng bàn thành công");
						alert2.setContentText("Bàn ăn đã gỡ dùng thành công");
						alert2.show();
						banAnMGCT.loadAllBanAn();
				    }
				    else {
				    	Alert alert2 = new Alert(Alert.AlertType.ERROR);
						alert2.setTitle("Xóa bàn thất bại");
						alert2.setContentText("Đã xảy ra sự cố, hãy thử lại");
						alert2.show();
						banAnMGCT.xoaInput();
				    }
				    return;
				}
			    catch(HibernateException ex1) {
			    	Alert alert2 = new Alert(Alert.AlertType.ERROR);
					alert2.setTitle("Xóa bàn thất bại");
					alert2.setContentText("Đã xảy ra sự cố, hãy thử lại");
					alert2.show();
					return;
			    }
			}
			banAnMGCT.loadAllBanAn();
			return;
		}
		banAnMGCT.xoaInput();
		try {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Có thực sự xóa bàn này?", ButtonType.YES, ButtonType.NO);
			alert.setTitle("Xác nhận?");
			alert.showAndWait();
			
			if (alert.getResult() == ButtonType.YES) {
				String path = PrimaryConf.CUSTOM_FILE_PATH_HEAD.concat(ttBanAn.getHinhAnh());
				File file = new File(path);
				if (!file.delete()) {
					throw new Exception("Không xóa được file, không tiếp tục xóa bàn ăn");
				}
				new BanAnDAO().delete(ttBanAn);
	
				Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
				alert2.setTitle("Xóa bàn thành công");
				alert2.setContentText("Đã xóa bàn ăn trong hệ thống");
				alert2.show();
				banAnMGCT.loadAllBanAn();
			}
		} catch (HibernateException ex1) {
			ex1.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Xóa bàn thất bại");
			alert.setContentText("Đã xảy ra sự cố, hãy thử lại");
			alert.show();
		} catch (Exception ex) {
			ex.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Xóa bàn thất bại");
			alert.setContentText("Đã xảy ra sự cố, hãy thử lại");
			alert.show();
		}
	}
}
