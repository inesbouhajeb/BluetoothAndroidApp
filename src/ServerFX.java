import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerFX extends Application{

    private Button startBtn;
    private static Button stopBtn;
    private static Button imageBtn;

    private static TextArea textArea;
    private SPPServerClass mSppServerClass;
    private VBox vBox;
    private HBox stopHbox;
    private HBox startHbox;

    private Thread mThread;
    private LocalDevice localDevice;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("SPP Server Bluetooth Application");

        startBtn=new Button("Start server");
        stopBtn=new Button("Stop server");
        imageBtn=new Button("Open image");

        textArea=new TextArea("");
        textArea.setEditable(false);
        textArea.setVisible(false);
        textArea.setPrefHeight(200);

        vBox=new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().add(textArea);
        stopHbox=new HBox(stopBtn);
        startHbox=new HBox(startBtn);
        startHbox.setAlignment(Pos.CENTER);
        stopHbox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(startHbox);
        vBox.getChildren().add(imageBtn);
        imageBtn.setVisible(false);
        vBox.setAlignment(Pos.CENTER);

        stopBtn.setOnAction(e->{

            ServerFX.getStopBtn().setText("Stop server");
            imageBtn.setVisible(false);
            textArea.clear();
            vBox.getChildren().remove(stopHbox);
            vBox.getChildren().remove(imageBtn);
            vBox.getChildren().add(startHbox);
            vBox.getChildren().add(imageBtn);
            imageBtn.setVisible(false);
            textArea.setVisible(false);
        });

        startBtn.setOnAction(e->{
            getStopBtn().setText("Stop Server");

            try {
                localDevice = LocalDevice.getLocalDevice();

                if(localDevice.getFriendlyName()!=null){
                    vBox.getChildren().remove(startHbox);
                    vBox.getChildren().remove(imageBtn);
                    vBox.getChildren().add(stopHbox);
                    vBox.getChildren().add(imageBtn);
                    textArea.setVisible(true);

                    //shows current time
                    Date date=new Date();
                    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
                    textArea.appendText("Started at: "+sdf.format(date)+"\n\n");
                    textArea.appendText("Local device address: " + localDevice.getBluetoothAddress()+"\n");
                    textArea.appendText("Local device name: " + localDevice.getFriendlyName()+"\n");
                }

                runServerInThread();
            }
            catch (BluetoothStateException exception)
            {
                showAlert();
            }

        });

        Scene scene=new Scene(vBox,500,300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

    /**
     * SPPServerClass takes a long time to run so we run it in a background thread using runServerInThread.
     */

    private void runServerInThread(){

        mThread=new Thread(new Runnable() {
            @Override
            public void run() {
                mSppServerClass = new SPPServerClass();
                 mSppServerClass.startServer();
                }
        });
        mThread.start();
    }

    public static TextArea getTextArea() {
        return textArea;
    }

    public static void showAlert(){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("BluetoothStack not detected!");
        alert.setContentText("Turn on your Bluetooth mode on your server device!");
        alert.showAndWait();
    }

    public static Button getImageBtn() {
        return imageBtn;
    }

    public static void openImage(String fileName){
        imageBtn.setOnAction(event -> {
            File file=new File(fileName);
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException | IllegalArgumentException e) {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Failed opening file!");
                alert.setContentText("Image doesn't exist.");
                alert.showAndWait();
            }
        });
    }

    public static Button getStopBtn() {
        return stopBtn;
    }
}