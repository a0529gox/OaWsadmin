package com.sky.fx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println(getClass().getResource("grid.fxml"));
		try {
			// 載入FXML內容並轉換為Parent
			Parent fxmlRoot = FXMLLoader.load(
			getClass().getResource("grid.fxml"));

			Scene scene = new Scene(fxmlRoot);

			primaryStage.setTitle("OaWsadmin v1.2");

			primaryStage.setScene(scene);

			// Show Stage
			primaryStage.show();
		} 
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
