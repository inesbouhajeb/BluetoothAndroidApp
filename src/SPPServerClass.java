import javafx.application.Platform;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Based on code from: http://www.jsr82.com/jsr-82-sample-spp-server-and-client/
 */

public class SPPServerClass{

    private StreamConnectionNotifier mStreamConnectionNotifier;

    public void startServer()  {
        String connectionString;

        //1. Constructing the connection URL for StreamConnectionNotifier. Only used to open it. We don't use it in Android app.

        //Create a UUID for SPP
        UUID uuid=new UUID("1101",true);

        //Create the servicve url
        connectionString = "btspp://localhost:"
                + uuid
                +";name=Sample SPP Server";

    //2. Registering service and Waiting for Client connection.

        //open server url
        try {
            mStreamConnectionNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

            //Waits for client connection
            ServerFX.getTextArea().appendText("Server Started. Waiting for clients to connect…\n\n");
            try {
                StreamConnection connection = mStreamConnectionNotifier.acceptAndOpen();//The acceptAndOpen method waits until a client is connected.
                if(connection!=null){

                    RemoteDevice dev=RemoteDevice.getRemoteDevice(connection);
                    ServerFX.getStopBtn().setVisible(false);
                    ServerFX.getTextArea().appendText("Connection started. Please wait...\n");
                    ServerFX.getTextArea().appendText("Remote device address: "+dev.getBluetoothAddress()+"\n");
                    ServerFX.getTextArea().appendText("Remote device name: "+dev.getFriendlyName(true)+"\n");

                    // read data from spp client using InputStream
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    InputStream connectionInputStream=connection.openInputStream();
                    int nRead=0;
                    byte[] testData=new byte[1024];
                    while ((nRead = connectionInputStream.read(testData, 0, testData.length)) != -1) {
                        baos.write(testData,0,nRead);
                    }
                    baos.flush();
                    byte[] endlessByteArray=baos.toByteArray();
                    baos.close();
                    connectionInputStream.close();
                    //file stamp
                    String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String fileName="ImgBluetoothApp_"+timeStamp+"_.jpeg";
                    //saving to the file
                    FileOutputStream fos=new FileOutputStream(fileName);
                    fos.write(endlessByteArray);
                    fos.close();
                    mStreamConnectionNotifier.close();

                    ServerFX.getTextArea().appendText("Success! Your image was saved.\n");
                    ServerFX.getImageBtn().setVisible(true);
                    ServerFX.openImage(fileName);
                    Platform.runLater(()->{
                        ServerFX.getStopBtn().setText("Run again");
                    });
                    ServerFX.getStopBtn().setVisible(true);
                }
            }
            catch (InterruptedIOException e){
                e.getStackTrace();
            }

        } catch (IOException e) {
            Platform.runLater(()->{
                ServerFX.showAlert();
                ServerFX.getStopBtn().setText("Run again");
                ServerFX.getStopBtn().setVisible(true);
            });
        }
    }
}
