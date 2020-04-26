package application;
	
import java.io.IOException;
import java.util.List;

import database.AccountDAO;
import entites.Account;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
      Parent root = FXMLLoader.load(getClass().getResource("/view/HomeManager.fxml"));
      primaryStage.setTitle("Hệ thống quản lý đặt bàn nhà hàng");
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
		
	}
	
	public static void main(String[] args) {
		//testConnect();
		launch(args);
	}
	
	public static void testConnect() {
		AccountDAO accManager = new AccountDAO();
		List<Account> dsAccount = accManager.getAll();
	    for (Account account : dsAccount) {
	      System.out.println(account);
	    }
	}
}
