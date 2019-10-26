package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;


public class MainForm extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fl=new FXMLLoader();
			fl.setLocation(fl.getClassLoader().getResource("res/layout/MainLayout.fxml"));
			AnchorPane root=(AnchorPane)fl.load();
			//上面式子也可用下面的静态方法FXMLLoader.load(URL location)代替
//			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/res/layout/MainLayout.fxml"));
			
			
			Scene scene = new Scene(root);
			//如果用getClass().getResource(".....")需要在相对路径前加"/"，getClass().getClassLoader().getResource(".....")的话就不用，详情请复习java反射
			scene.getStylesheets().add(getClass().getClassLoader().getResource("res/css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			//设置背景舞台透明
			scene.setFill(Color.TRANSPARENT);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
