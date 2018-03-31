import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Scanner;

public class ServerFX extends Application implements Runnable{

    private Button startBtn;
    private TextArea textArea;
    private ImageView imageView;
    private Scanner scanner;
    private SPPServerClass server;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("SPP Server Bluetooth Application");

        server=new SPPServerClass();

        scanner=new Scanner(System.in);

        textArea=new TextArea();
        textArea.setEditable(false);
        imageView=new ImageView();
        startBtn=new Button("Start server");



        startBtn.setOnAction(e->{

            server.startServer();

            imageView.setImage(server.getImageFromBytes());
        });

        HBox hBox=new HBox(startBtn,textArea);
        VBox vBox=new VBox();
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(imageView);

        Scene scene=new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
        (new Thread(new ServerFX())).start();
    }

    @Override
    public void run() {
        while(server.isScannerRunning()){
            textArea.setText(scanner.nextLine());
        }
    }
}
