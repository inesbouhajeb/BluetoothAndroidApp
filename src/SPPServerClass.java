import javafx.scene.image.Image;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Code from: http://www.jsr82.com/jsr-82-sample-spp-server-and-client/
 */

public class SPPServerClass {

    private String connectionString;
    private StreamConnectionNotifier mStreamConnectionNotifier;
    private StreamConnection connection;
    private boolean scannerRunning=true;
    private Image imageFromBytes;

    public void startServer() {
        //1. Constructing the connection URL for StreamConnectionNotifier. Only used to open it.

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
            //Wait for client connection

            System.out.println("\nServer Started. Waiting for clients to connect…");
            connection=mStreamConnectionNotifier.acceptAndOpen();//The acceptAndOpen method waits until a client is connected.

            RemoteDevice dev=RemoteDevice.getRemoteDevice(connection);
            System.out.println("Remote device address: "+dev.getBluetoothAddress());
            System.out.println("Remote device name: "+dev.getFriendlyName(true));

            //read data from spp client using InputStream

            int size=240664; //for now, later change it to check amount of bytes received from client //TODO
            InputStream inputStream=connection.openInputStream();
            size=inputStream.available();
            byte[] receivedBytes= new byte[size];
            inputStream.read(receivedBytes,0,size);

            //for me to see comming bytes
            for(int i=0;i<receivedBytes.length;i++){
                System.out.println(receivedBytes[i]);
            }
            System.out.println(" Length of received bytes: "+receivedBytes.length);

            //saving bytes as file
            String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            FileOutputStream fos=new FileOutputStream("ImgBluetoothApp_"+timeStamp+"_.jpeg");
            fos.write(receivedBytes);
            fos.close();

            //TODO
            //setting image in GUI
            //imageFromBytes = new Image(new ByteArrayInputStream(receivedBytes));

            //send response to spp client, not needed now
//            OutputStream outStream=connection.openOutputStream();
//            PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
//            pWriter.write("Response String from SPP Server\r\n");
//            pWriter.flush();

            scannerRunning=false;

//            pWriter.close();
            mStreamConnectionNotifier.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

//display local device address and name
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            System.out.println("Address: " + localDevice.getBluetoothAddress());
            System.out.println("Name: " + localDevice.getFriendlyName());

            SPPServerClass mSppServerClass = new SPPServerClass();
            mSppServerClass.startServer();
        }
        catch (BluetoothStateException e)
        {
            System.out.println("BluetootStack not detected! Turn on your Bluetooth mode on your device!");
        }

    }

    public boolean isScannerRunning() {
        return scannerRunning;
    }

    public Image getImageFromBytes() {
        return imageFromBytes;
    }
}
