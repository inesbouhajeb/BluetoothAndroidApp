import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;

/**
 * Code from: http://www.jsr82.com/jsr-82-sample-spp-server-and-client/
 */

public class SPPServerClass {

    private String connectionString;
    private StreamConnectionNotifier mStreamConnectionNotifier;
    private StreamConnection connection;



    private void startServer() throws IOException {
        //1. Constructing the connection URL.

        //Create a UUID for SPP
        UUID uuid=new UUID("1101",true);

        //Create the servicve url
        connectionString = "btspp://localhost:"
                + uuid
                +";name=Sample SPP Server";


    //2. Registering service and Waiting for Client connection.

        //open server url

            mStreamConnectionNotifier = (StreamConnectionNotifier) Connector.open(connectionString);


        //Wait for client connection

            System.out.println("\nServer Started. Waiting for clients to connect…");
            connection=mStreamConnectionNotifier.acceptAndOpen();//The acceptAndOpen method waits until a client is connected.


        RemoteDevice dev=RemoteDevice.getRemoteDevice(connection);
        System.out.println("Remote device address: "+dev.getBluetoothAddress());
        System.out.println("Remote device name: "+dev.getFriendlyName(true));

        //read string from spp client
        //TODO change it to read bytes
        InputStream inputStream=connection.openInputStream();
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        String lineRead=bufferedReader.readLine();
        System.out.println(lineRead);

        //send response to spp client
        OutputStream outStream=connection.openOutputStream();
        PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
        pWriter.write("Response String from SPP Server\r\n");
        pWriter.flush();

        pWriter.close();
        mStreamConnectionNotifier.close();
    }

    public static void main(String[] args) throws IOException {

//display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: "+localDevice.getBluetoothAddress());
        System.out.println("Name: "+localDevice.getFriendlyName());

        SPPServerClass mSppServerClass=new SPPServerClass();
        mSppServerClass.startServer();

    }

}
