package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Equipment;
import application.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import utils.DatabaseConnection;
import utils.Medicine;
import utils.ProductListener;
import utils.UserSession;

public class StoreMainController extends TransitionUtils implements Initializable{
	
	@FXML private StackPane return_btn = new StackPane();
	@FXML private HBox roothb = new HBox();
	
	@FXML private Button viewcart = new Button();
	@FXML private Button medicine = new Button();
	@FXML private Button equipment = new Button();

	
	@FXML private FlowPane itemflow = new FlowPane();
	
	@FXML private StackPane contentPane = new StackPane();
	@FXML private VBox purchasebox = new VBox();
	@FXML private VBox contentbox = new VBox();
	@FXML private Button confirmbuy = new Button();
	@FXML private Button cross_btn = new Button();
	@FXML private Label buyname = new Label();
	@FXML private Label buydetails = new Label();
	@FXML private Label total = new Label();
	@FXML private TextField purchaseqt = new TextField();
	@FXML private ImageView buyimg = new ImageView();
	
	private ProductListener<Product> medlistener;
	private ProductListener<Equipment> eqlistener;
	private List<Product> meds = new ArrayList<>();
	private List<Equipment> eq = new ArrayList<>();
	
	
	private void getMedData()
	{
		String sql = "SELECT * FROM medicine";
		try(
			Connection con = DatabaseConnection.connect();
			PreparedStatement statement = con.prepareStatement(sql);	
			)
				{
					ResultSet rs = statement.executeQuery();
					while(rs.next())
					{
						Product product = new Product(rs.getString("medname"), rs.getString("img"), rs.getString("amount"), rs.getString("doseunit"), rs.getInt("dose"), rs.getDouble("price"));
						meds.add(product);
					}
					con.close();
				}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void getEqData()
	{
		String sql = "SELECT * FROM equipment";
		try(
			Connection con = DatabaseConnection.connect();
			PreparedStatement statement = con.prepareStatement(sql);	
			)
				{
					ResultSet rs = statement.executeQuery();
					while(rs.next())
					{
						Equipment product = new Equipment(rs.getString("name"), rs.getString("img"), rs.getDouble("price"));
						eq.add(product);
					}
					con.close();
				}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private void drawMedFlow()
	{
		for(int i=0;i<meds.size();i++)
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/controllers/Item.fxml"));
			VBox root = null;
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			ItemController itemcontroller = loader.getController();
			itemcontroller.setItemData(meds.get(i), medlistener);
//			System.out.println(
//				    meds.get(i).getName() + " → price: " + meds.get(i).getPrice()
//				);

			itemflow.getChildren().add(root);
		}
	}
	
	private void drawEqFlow()
	{
		for(int i=0;i<eq.size();i++)
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/controllers/EquipmentItem.fxml"));
			VBox root = null;
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			EquipmentController eqcontroller = loader.getController();
			eqcontroller.setItemData(eq.get(i), eqlistener);
			
			itemflow.getChildren().add(root);
		}
	}
	
	
	private void setSelectedMed(Product p)
	{
		purchasebox.setVisible(true);
		contentPane.setAlignment(purchasebox, Pos.TOP_RIGHT);
//		VBox.setMargin(purchasebox, new Insets((-1)*contentbox.getWidth(), 0,0,0));
		buyimg.setImage(new Image(getClass().getResourceAsStream(p.getImg())));
		buyname.setText(p.getName());
		buydetails.setText("Dose: "+p.getDose()+" "+p.getDoseUnit()+
				"\nAmount: "+p.getAmount()+
				"\nPrice/Unit: "+p.getPrice());
		int quantity = Integer.valueOf(purchaseqt.getText());
		double totalprice =  quantity*p.getPrice();
		if(!purchaseqt.getText().isBlank())
			total.setText("Total: "+String.valueOf(totalprice));
		passToDelivery(p, quantity, totalprice);
	}
	
	private void setSelectedEq(Equipment e)
	{
		purchasebox.setVisible(true);
		contentPane.setAlignment(purchasebox, Pos.TOP_RIGHT);
//		VBox.setMargin(purchasebox, new Insets((-1)*contentbox.getWidth(),0,0,contentbox.getWidth() - purchasebox.getWidth()));
		buyimg.setImage(new Image(getClass().getResourceAsStream(e.getImg())));
		buyname.setText(e.getName());
		buydetails.setText("Price: "+e.getPrice());
		int quantity = Integer.valueOf(purchaseqt.getText());
		double totalprice =  quantity*e.getPrice();
		if(!purchaseqt.getText().isBlank())
			total.setText("Total: "+String.valueOf(totalprice));
		passToDelivery(e, quantity, totalprice);
	}
	
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	
    public void passToDelivery(Product p, int q, double t)
    {
		int userId = UserSession.getInstance().getUserId();
	    if (userId == -1) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user is logged in!");
            return;
        }
	    
	    FXMLLoader load = new FXMLLoader();
	    load.setLocation(getClass().getResource("/controllers/StoreDelivery.fxml"));
	    try {
			HBox root = load.load();
			StoreDeliveryController deliverycontrol = new StoreDeliveryController();
			deliverycontrol.setData(p, userId, q, t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    
    }
    public void passToDelivery(Equipment e, int q, double t)
    {
    	int userId = UserSession.getInstance().getUserId();
    	if (userId == -1) {
    		showAlert(Alert.AlertType.ERROR, "Error", "No user is logged in!");
    		return;
    	}
	    FXMLLoader load = new FXMLLoader();
	    load.setLocation(getClass().getResource("/controllers/StoreDelivery.fxml"));
	    try {
			HBox root = load.load();
			StoreDeliveryController deliverycontrol = new StoreDeliveryController();
			deliverycontrol.setData(e, userId, q, t);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

		roothb.setOpacity(0);
        fadeInToScene(roothb);
        initializeSidebar(roothb);
        
        
		return_btn.setOnMouseClicked((e)->{
			fadeOutToScene(roothb, "Home");
		});
		cross_btn.setOnAction((e)->{
			purchasebox.setVisible(false);
//			VBox.setMargin(purchasebox, new Insets(contentbox.getWidth(), 0,0,0));
		});
//		viewcart.setOnAction((e)->{
//			fadeOutToScene(roothb, "StoreCart");
//		});
		purchasebox.setVisible(false);
		
		medlistener = new ProductListener<Product>()
				{
					@Override
					public void selectedItemListener(Product p)
					{
						setSelectedMed(p);
					}
				};
		eqlistener = new ProductListener<Equipment>()
				{
					@Override
					public void selectedItemListener(Equipment eq)
					{
						setSelectedEq(eq);
					}
				};
		
		getMedData();
		getEqData();
		drawMedFlow();
		medicine.setOnAction((e)->{
			itemflow.getChildren().clear();
			drawMedFlow();
		});
		equipment.setOnAction((e)->{
			itemflow.getChildren().clear();
			drawEqFlow();
		});
		
		confirmbuy.setOnAction((e)->{
			
			fadeOutToScene(roothb, "StoreDelivery");
		});
//		search_btn.setOnAction((e)->{
//			//db connection
//		});
		
	}
	
	
	
}
