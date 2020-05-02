/**
 * Created on: 16:46:19 2 thg 5, 2020
 * 
 * @author Dinh Van Dung YKNB
 */

package controller;

import entites.TTBanDat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ItemTTBanDatController {
  @FXML
  private Label lblDateDat, lblMonthDat, lblYearDat;
  @FXML
  private Label lblDatePhucVu, lblMonthPhucVu, lblYearPhucVu;
  @FXML
  private Label lblHoTenKhachHang;
  @FXML
  private Label lblKySoBanAn;
  @FXML
  private Label lblTongTien;
  @FXML
  private Label lblDaThanhToan, lblDaHuy, lblChuaThanhToan;
  
  private BanDatManagerController banDatMGCT;
  private TTBanDat ttBanDat;
  
  public void loadData(TTBanDat b) {
    lblDateDat.setText(stringDate(b.getNgayDatBan().getDate()));
    lblMonthDat.setText(stringMonth(b.getNgayDatBan().getMonth() + 1));
    lblYearDat.setText(String.valueOf(b.getNgayDatBan().getYear() + 1900));
    lblDatePhucVu.setText(stringDate(b.getNgayPhucVu().getDate()));
    lblMonthPhucVu.setText(stringMonth(b.getNgayPhucVu().getMonth() + 1));
    lblYearPhucVu.setText(String.valueOf(b.getNgayPhucVu().getYear() + 1900));
    lblHoTenKhachHang.setText(b.getKhachHang().getHoTen());
    lblKySoBanAn.setText(b.getBanAn().getKySoBA());
    lblTongTien.setText(String.valueOf(b.getTongTien()) + " Đ");
    showStatus(b);
    ttBanDat = b;
  }
  
  public void showStatus(TTBanDat b) {
    if (b.isDaHuy()) {
      lblDaThanhToan.setVisible(false);
      lblDaHuy.setVisible(true);
      lblChuaThanhToan.setVisible(false);
    } else if (b.isDaThanhToan()) {
      lblDaThanhToan.setVisible(true);
      lblDaHuy.setVisible(false);
      lblChuaThanhToan.setVisible(false);
    } else {
      lblDaThanhToan.setVisible(false);
      lblDaHuy.setVisible(false);
      lblChuaThanhToan.setVisible(true);
    }
  }
  public String stringDate(int date) {
    String result;
    if (date < 10) {
      result = "0" + date;
    } else {
      result = "" + date;
    }
    return result;
  }
  public String stringMonth(int month) {
    String result;
    switch (month) {
      case 1:
        result = "JAN";
        break;
      case 2:
        result = "FEB";
        break;
      case 3:
        result = "MAR";
        break;
      case 4:
        result = "APR";
        break;
      case 5:
        result = "MAY";
        break;
      case 6:
        result = "JUN";
        break;
      case 7:
        result = "JUL";
        break;
      case 8:
        result = "AUG";
        break;
      case 9:
        result = "STE";
        break;
      case 10:
        result = "OCT";
        break;
      case 11:
        result = "SEP";
        break;
      case 12:
        result = "DEC";
        break;
      default:
        result = "JAN";
        break;
    }
    return result;
  }
  
  public void setBanDatMGCT(BanDatManagerController banDatMGCT) {
    this.banDatMGCT = banDatMGCT;
  }
  
}
