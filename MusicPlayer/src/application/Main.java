package application;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTabPane;

import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;
/**
* 代码较乱，并且界面设置与功能杂糅，想获取歌曲的AudioSpectrumListener()【在143行】
* 会当作一个小功能吧
* 注：使用jdk1.8.0_191好像背景图片会变小，原因不明
* @author 龙之山河
* @version 1.0
*/
public class Main extends Application {

	private boolean istop=false,isFullScreen=false;
	private double dX,dY;
	private static boolean mouse=false;
	private Image image=new Image(this.getClass().getClassLoader().getResource("res/image/js_cg.jpg").toExternalForm());
	private double vbox_ty=380;
	private int lrc_n=0;
	private ParallelTransition pt=new ParallelTransition();
	JFXColorPicker jcp;
	Color c=Color.BLACK;
	Button b1=new Button("上一曲");
	Button b2=new Button("下一曲");
	MediaPlayer mp;
	ArrayList<Media> me;
	ArrayList<URL> URL;
	int index=1;
	Node[] node;
	@Override
	public void start(Stage primaryStage) throws Exception {
		//初始化主键,以后会放在fxml中
		Button iconified=new Button("iconified");
		Button fullScreen=new Button("fullScreen");
		Button close=new Button("close");
		Button alwaysOnTop=new Button("alwaysOnTop");
		
		Label minimality=new Label("最小化托盘：");
		ToggleButton yes=new ToggleButton("是");
		ToggleButton no=new ToggleButton("否");
		ToggleGroup group = new ToggleGroup();
		yes.setToggleGroup(group);
		no.setToggleGroup(group);
		no.setSelected(true);
		
		
		URL cssurl=this.getClass().getClassLoader().getResource("res/css/mycss.css");
		System.out.println(cssurl.toExternalForm());
		
		HBox hbox_top=new HBox(minimality,yes,no,iconified,alwaysOnTop,fullScreen,close);
		hbox_top.setAlignment(Pos.TOP_RIGHT);	
		hbox_top.getStylesheets().add(cssurl.toExternalForm());
		
		AnchorPane.setBottomAnchor(b1, 10.0);
		AnchorPane.setLeftAnchor(b1, 420.0);
		
		AnchorPane.setBottomAnchor(b2, 10.0);
		AnchorPane.setLeftAnchor(b2, 700.0);
		
		Media m1=new Media(this.getClass().getClassLoader().getResource("res/music/だんご大家族.mp3").toExternalForm());
		Media m2=new Media(this.getClass().getClassLoader().getResource("res/music/请你检阅（Cover：70周年｜大阅兵合唱团）.mp3").toExternalForm());
		Media m3=new Media(this.getClass().getClassLoader().getResource("res/music/风中舞.mp3").toExternalForm());
		me=new ArrayList<Media>();
		me.add(m1);
		me.add(m2);
		me.add(m3);
		
		URL lrcURL1=this.getClass().getClassLoader().getResource("res/lyric/だんご大家族.lrc");
		URL lrcURL2=this.getClass().getClassLoader().getResource("res/lyric/请你检阅（Cover：70周年｜大阅兵合唱团）.lrc");
		URL lrcURL3=this.getClass().getClassLoader().getResource("res/lyric/风中舞.lrc");
		URL=new ArrayList<URL>();
		URL.add(lrcURL1);
		URL.add(lrcURL2);
		URL.add(lrcURL3);
		
		
		URL url=this.getClass().getClassLoader().getResource("res/image/js_cg.jpg");
		Image image=new Image(url.toExternalForm());
		ImageView iv=new ImageView(image);
		iv.setFitWidth(image.getWidth());
		iv.setFitHeight(image.getHeight());
		Rectangle rectangle=new Rectangle(iv.getFitWidth(),iv.getFitHeight());
		rectangle.setArcHeight(50);
		rectangle.setArcWidth(50);
		iv.setClip(rectangle);
		
		
		
		
		AnchorPane ap=new AnchorPane(iv,hbox_top,b1,b2);
		
		JFXTabPane tabPane=new JFXTabPane();
		ap.getChildren().addAll(this.getUI(primaryStage,ap));
		tabPane.getTabs().add(new Tab("音乐", ap));
		System.out.println(url.toExternalForm());
		ap.setStyle("-fx-background-color:#ffffff00;"+"-fx-background-radius:20;");
		
//		ap.setStyle("-fx-background-image:url("+url.toExternalForm()+")");
		
		
		b2.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				if(index<me.size()-1){
					index++;
				}
				else {
					index=0;
				}
				ap.getChildren().clear();
				vbox_ty=380;
				lrc_n=0;
				System.gc();
				ap.getChildren().addAll(iv,hbox_top,b1,b2);
				ap.getChildren().addAll(Main.this.getUI(primaryStage,ap));
				
			}
		});
		
		b1.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				if(index>0){
					index--;
				}
				else {
					index=me.size()-1;
				}
				ap.getChildren().clear();
				vbox_ty=380;
				lrc_n=0;
				System.gc();
				ap.getChildren().addAll(iv,hbox_top,b1,b2);
				ap.getChildren().addAll(Main.this.getUI(primaryStage,ap));
				
			}
		});
		primaryStage.getIcons().add(new Image("/res/image/icon.png"));
		Scene scene=new Scene(ap,iv.getFitWidth(),iv.getFitHeight());
		//设置背景舞台透明
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
		

		hbox_top.setPrefWidth(ap.getWidth());
		
		
		
		
		
		
		
		//拖动窗口
		ap.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				dX=event.getScreenX()-primaryStage.getX();
				dY=event.getScreenY()-primaryStage.getY();
			}
		});
		ap.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (!isFullScreen&&event.getButton()==MouseButton.PRIMARY) {
					primaryStage.setX(event.getScreenX()-dX);
					primaryStage.setY(event.getScreenY()-dY);
				}
				}
				
		});
		
		
		
		
		
		//设置窗口上的按钮功能，以后会用fxml
		close.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				close.setStyle("-fx-background-color:#ff9911ff");				
			}
		});
		close.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				close.setStyle("-fx-background-color:ffffff00");				
			}
		});
		close.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				System.out.println(yes.isSelected());
				
				if(yes.isSelected()) {				
					java.awt.Image icon=Toolkit.getDefaultToolkit().getImage("src/res/image/icon.png");
					String tooltip="MusicPlayer";
					PopupMenu popup=new PopupMenu();
					MenuItem item1=new MenuItem("显示");
					MenuItem item2=new MenuItem("退出");
					
					popup.add(item1);
					popup.add(item2);
					SystemTray systemTray = SystemTray.getSystemTray();			
					TrayIcon trayIcon=new TrayIcon(icon, tooltip, popup);
					try {
						systemTray.add(trayIcon);
					} catch (AWTException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					Platform.setImplicitExit(false);
					primaryStage.hide();
					
					item1.addActionListener(new ActionListener() {
						
						public void actionPerformed(java.awt.event.ActionEvent e) {
							Platform.runLater(new Runnable() {
								
								public void run() {
									primaryStage.show();															
								}
							});
							systemTray.remove(trayIcon);								
						}
					});
					item2.addActionListener(new ActionListener() {
						
						public void actionPerformed(java.awt.event.ActionEvent e) {
							Platform.setImplicitExit(true);
							Platform.runLater(new Runnable() {
								
								public void run() {
									primaryStage.close();															
								}
							});
							systemTray.remove(trayIcon);
//							Platform.exit();
						}
					});
				}else {
					Platform.setImplicitExit(true);
					primaryStage.close();
//					Platform.exit();	
				}
			}
		});
		
		fullScreen.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				fullScreen.setStyle("-fx-background-color:#ff9911ff");				
			}
		});
		fullScreen.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				fullScreen.setStyle("-fx-background-color:ffffff00");				
			}
		});
		fullScreen.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				primaryStage.setFullScreen(true);			
			}
		});
		primaryStage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {

			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				isFullScreen=newValue;
				if (newValue) {					
					iv.setFitWidth(Screen.getPrimary().getBounds().getWidth());
					iv.setFitHeight(Screen.getPrimary().getBounds().getHeight());
					iv.setClip(null);
					//这里有bug，已修复（需用类查找删除）
					ap.getChildren().remove(hbox_top);
				}else {
					iv.setFitWidth(image.getWidth());
					iv.setFitHeight(image.getHeight());
					iv.setClip(rectangle);
//					hbox_top.setPrefWidth(iv.getFitWidth());
					ap.getChildren().add(hbox_top);
				}				
			}
		});
		
		alwaysOnTop.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (!istop)
					alwaysOnTop.setStyle("-fx-background-color:#ff9911ff");				
			}
		});
		alwaysOnTop.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (!istop) 
					alwaysOnTop.setStyle("-fx-background-color:ffffff00");				
			}
		});
		alwaysOnTop.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (istop) {
					istop=false;					
				}else {
					istop=true;
					alwaysOnTop.setStyle("-fx-background-color:ff000099");
				}
				primaryStage.setAlwaysOnTop(istop);
				
			}
		});
		
		iconified.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				iconified.setStyle("-fx-background-color:#ff9911ff");				
			}
		});
		iconified.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				iconified.setStyle("-fx-background-color:ffffff00");				
			}
		});
		iconified.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				primaryStage.setIconified(true);			
			}
		});
		
		//以后会用fxml控制，先这样凑合
		
		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Slider slider=(Slider) ap.lookup("#sl");
				slider.setPrefWidth(newValue.doubleValue()-20);			
			}
		});
	}
	/**此方法负责初始化界面的UI
	 * @param 主窗口
	 * @return Node[],控件的数组
	 * @author 龙之山河
	 * @version 0.5
	 * 
	 * */
	private Node[] getUI(Stage primaryStage,AnchorPane ap) {
		
		Media media=me.get(index);
		mp=new MediaPlayer(media);
		
		Button b1=new Button("播放");
		AnchorPane.setBottomAnchor(b1,10.0);
		AnchorPane.setLeftAnchor(b1, 600.0);
		
		jcp=new JFXColorPicker(Color.BLACK);
		AnchorPane.setBottomAnchor(jcp,10.0);
		AnchorPane.setLeftAnchor(jcp, 200.0);
		
		
		ChoiceBox<Double> cb=new ChoiceBox<Double>();
		//虽然设置的倍数范围是[1.0，8.0]，但倍数过高歌词跟不上（主要是声音过于古怪）
		cb.getItems().addAll(0.8,0.9,1.0,1.1,1.2);
		cb.getSelectionModel().select(1.0);
		AnchorPane.setBottomAnchor(cb,10.0);
		AnchorPane.setLeftAnchor(cb, 500.0);
		Tooltip ttip=new Tooltip("控制音乐播放速率,可用区间[0.0,8.0],但速率过快会打乱歌词,不建议使用");
		cb.setTooltip(ttip);
		cb.setConverter(new StringConverter<Double>() {
			
			@Override
			public String toString(Double object) {
				return object+"倍数";
			}		
			@Override
			public Double fromString(String string) {
				// TODO 自动生成的方法存根
				return null;
			}
		});
		
		Slider slider=new Slider(0,1,0.5);
		slider.setOrientation(Orientation.VERTICAL);
		
		JFXSlider slider2=new JFXSlider(0,1,0);
		mp.setOnReady(new Runnable() {
			
			public void run() {
				System.out.println(mp.getTotalDuration().toSeconds());
				slider2.setValue(0);
				slider2.setMin(0);
				slider2.setMax(mp.getTotalDuration().toSeconds());
				System.out.println(mp.getTotalDuration().toSeconds());
			}
		});

		AnchorPane.setLeftAnchor(slider2, 10.0);
		AnchorPane.setBottomAnchor(slider2, 40.0);
		
		URL url=this.getClass().getClassLoader().getResource("res/UI/sound.png");
		ImageView sound=new ImageView(url.toExternalForm());	
		
		
		slider2.setPrefWidth(image.getWidth()-20);
		
		
		
		
		//设置循环（当音乐结束，进度跳到开始位置）
		mp.setOnEndOfMedia(new Runnable() {		
			
			public void run() {
				mp.seek(Duration.millis(0));				
			}
		});
		//应该是这样设置无限循环的，但是seek时却会从头开始（即：无法seek）
//		mp.setCycleCount(MediaPlayer.INDEFINITE);
		
		

		
		//音乐播放
		b1.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				if(b1.getText().equals("播放")) {
					mp.play();
					b1.setText("暂停");
				}else {
					mp.pause();
					b1.setText("播放");
				}
				
			}
		});
		
		//音量控制
		mp.volumeProperty().bind(slider.valueProperty());
		
		//播放按钮
		mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
				if (mouse==false) {
					slider2.setValue(newValue.toSeconds());
				}
				
				
			}
		});
		//音乐进度
		slider2.setOnMousePressed(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				mp.seek(Duration.seconds(slider2.getValue()));
				mouse=true;
				
			}
		});
		slider2.setOnMouseReleased(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				mp.seek(Duration.seconds(slider2.getValue()));
				mouse=false;
				
			}
		});
		//音乐声音显示
		VBox vbox=new VBox(5);
		vbox.getChildren().addAll(sound);
		vbox.setAlignment(Pos.BASELINE_CENTER);
//		vbox.setStyle("-fx-background-color:ff0000f1");
		AnchorPane.setBottomAnchor(vbox, 10.0);
		AnchorPane.setLeftAnchor(vbox, 657.0);
		vbox.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(!vbox.getChildren().contains(slider)) {
					vbox.getChildren().clear();
					vbox.getChildren().addAll(slider,sound);
				}
			}
		});
		vbox.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(vbox.getChildren().contains(slider))
					vbox.getChildren().remove(slider);
			}
		});
		
		//音乐速率控制
		cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Double>() {

			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				mp.setRate(newValue);
				
			}
		});

		//必须分层(好像也可以不分)
		node=new Node[6];
		node[1]= b1;
		node[2]= cb;
		node[3]= slider2;
		node[4]= vbox;
		node[5]=jcp;
		node[0]=this.getLyric(mp,media);
		slider2.setId("sl");
		
		//--------------------------------------
//		mp.setAudioSpectrumInterval(1);//设置获取音频数据的间隔时间，不推荐修改
//		mp.setAudioSpectrumThreshold(-100);
//		mp.setAudioSpectrumNumBands(20);//频谱范围
		//此方法获取音乐频谱数据
		mp.setAudioSpectrumListener(new AudioSpectrumListener() {
			
			public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
				//magnitudes表示：包含每个带的非正频谱幅度（dB）的数组。数组的大小等于带的数量，应被视为只读。
				//phases表示： 包含每个频段[Math.PI，Math.PI]范围内的相位的数组。数组的大小等于频段的数量，应被视为只读。
				
			}
		});
		
		
		//--------------------------------
		
		return node;	
	}
	
	private SubScene getLyric(MediaPlayer mp,Media m) {
		VBox vbox=new VBox();	
		SubScene ss=new SubScene(vbox,500, 500, true, SceneAntialiasing.BALANCED);
		PerspectiveCamera pc=new PerspectiveCamera();
		pc.setTranslateY(157);
		ss.setCamera(pc);
		vbox.setAlignment(Pos.TOP_CENTER);
		vbox.setStyle("-fx-background-color:#ffffff00");
		AnchorPane.setRightAnchor(ss, 100.0);
		AnchorPane.setTopAnchor(ss, 100.0);	
		
		URL lrcURL=URL.get(index);		
		try {
			//路径需转码
			File lrcFile=new File(URLDecoder.decode(lrcURL.getFile(), "utf-8"));
			FileInputStream fis=new FileInputStream(lrcFile);
			InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
			BufferedReader bufIn= new BufferedReader(isr);
			String row = null;
			//此处歌词解析有参考zhaoyijie（Aquarius_genius）所写的播放器
			while((row = bufIn.readLine()) != null) {
				if (row.indexOf("[") == -1 || row.indexOf("]") == -1) {
                    continue;
                }
                if (row.charAt(1) < '0' || row.charAt(1) > '9') {
                    continue;
                }
                String strTime = row.substring(1,row.indexOf("]"));//00:03.29
                String strMinute = strTime.substring(0, strTime.indexOf(":"));//取出：分钟
                String strSecond = strTime.substring(strTime.indexOf(":") + 1);//取出：秒和毫秒
                //转换为int分钟
                int intMinute = Integer.parseInt(strMinute);
                //换算为秒
                double seconds=Double.valueOf(strSecond);
                //换算为总秒数，精确值为0.1
                double total=intMinute*60+((int)(seconds*10))/10.0;
                //
                Text lrc_l=new Text(row.trim().substring(row.indexOf("]") + 1));
                lrc_l.setTranslateZ(100);
                lrc_l.setStyle("-fx-font-size:20");
                //将播放时间设在lrc_l的UserData中,vbox添加的先后顺序存队列
                lrc_l.setUserData(total);
                vbox.getChildren().add(lrc_l);
			}
			bufIn.close();
			isr.close();
			fis.close();
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//初始化歌词
		vbox.setTranslateY(vbox_ty);
		if(!vbox.getChildren().isEmpty()) {
			Text lrc_l=(Text)vbox.getChildren().get(0);
			lrc_l.setTranslateZ(0);
			lrc_l.setFill(Color.RED);
		}
		
		jcp.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				c=jcp.getValue();
				for (int i = 0; i < vbox.getChildren().size(); i++) {
					Text t=(Text) vbox.getChildren().get(i);
					if(t.getTranslateZ()==100) {
						t.setFill(c);
					}
					
				}
				
			}
		});
		
		//推荐这样减小数据精度(因为是0.1秒刷新一次，但是好像歌词太近会造成不明bug)
		mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
				//快速播放加反复seek会导致歌词Y轴坐标错位和Z轴坐标异常（原因未知，待解决）
				if(mp.getStatus().equals(javafx.scene.media.MediaPlayer.Status.PLAYING)) {
				if(pt==null||pt.getStatus().equals(Status.STOPPED)) {
				//测试
				if(lrc_n+1<vbox.getChildren().size())
				if((double)vbox.getChildren().get(lrc_n+1).getUserData()<=newValue.toSeconds()) {	
					double d=500;
					if (lrc_n+2<vbox.getChildren().size()) {   //防空指针
						//歌词进度与音乐播放进度相差太大时，加速
						if(newValue.toSeconds()>(double)vbox.getChildren().get(lrc_n+2).getUserData()) {
							d=30;
						}
					}
					pt=toUp(vbox,Duration.millis(d));	
					lrc_n++;
					//测试
					Text t=(Text) vbox.getChildren().get(lrc_n);
					System.out.println("-->"+t.getText());					
				}
				if ((double)vbox.getChildren().get(lrc_n).getUserData()>newValue.toSeconds()&&lrc_n!=0) {
					double d=500;
					//歌词进度与音乐播放进度相差太大时，加速
					if(newValue.toSeconds()<(double)vbox.getChildren().get(lrc_n-1).getUserData()) {
						d=30;
					}
					pt=toDown(vbox,Duration.millis(d));
					lrc_n--;
					//测试
					Text t=(Text) vbox.getChildren().get(lrc_n);
					System.out.println("<--"+t.getText());
				}
				}
				}
			}
		});
						
		return ss;
	}
	private ParallelTransition toUp(VBox vbox,Duration duration) {
		
		TranslateTransition t1=new TranslateTransition();
		t1.setDuration(duration);
		t1.setInterpolator(Interpolator.LINEAR);
		t1.setByY(-26);
		t1.setNode(vbox);
		
		TranslateTransition t2=new TranslateTransition();
		t2.setDuration(duration);
		t2.setInterpolator(Interpolator.EASE_BOTH);
		t2.setByZ(-100);
		t2.setNode(vbox.getChildren().get(lrc_n+1));
		
		TranslateTransition t3=new TranslateTransition();
		t3.setDuration(duration);
		t3.setInterpolator(Interpolator.EASE_BOTH);
		t3.setByZ(100);
		t3.setNode(vbox.getChildren().get(lrc_n));
		
		Text text1=(Text)vbox.getChildren().get(lrc_n+1);
		text1.setFill(Color.RED);
		Text text2=(Text)vbox.getChildren().get(lrc_n);
		text2.setFill(c);
		
		
		pt.getChildren().clear();
		pt.getChildren().addAll(t1,t2,t3);
		pt.play();
		return pt;
	}
	private ParallelTransition toDown(VBox vbox,Duration duration) {
		TranslateTransition t1=new TranslateTransition();
		t1.setDuration(duration);
		t1.setInterpolator(Interpolator.LINEAR);
		t1.setByY(26);
		t1.setNode(vbox);
		
		TranslateTransition t2=new TranslateTransition();
		t2.setDuration(duration);
		t2.setInterpolator(Interpolator.EASE_BOTH);
		t2.setByZ(100);
		t2.setNode(vbox.getChildren().get(lrc_n));
		
		TranslateTransition t3=new TranslateTransition();
		t3.setDuration(duration);
		t3.setInterpolator(Interpolator.EASE_BOTH);
		t3.setByZ(-100);
		t3.setNode(vbox.getChildren().get(lrc_n-1));
		
		Text text1=(Text)vbox.getChildren().get(lrc_n-1);
		text1.setFill(Color.RED);
		Text text2=(Text)vbox.getChildren().get(lrc_n);
		text2.setFill(c);
		
		pt.getChildren().clear();
		pt.getChildren().addAll(t1,t2,t3);
		pt.play();
		return pt;
	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
}

