package application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
public class MyController {
	@FXML
	private Button close;
	public MyController() {}

	@FXML
	private void initialize() {

	}
	
	@FXML
	private void MouseEntered(){
		System.out.println("hh");
		close.setStyle("-fx-background-color:0000ffff");
	}
}
