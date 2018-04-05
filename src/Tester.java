import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;

public class Tester {


    public static byte[] testMethod(){
        byte[] mBytes;
        File file=new File("C:\\Users\\adminn\\Documents\\IntelliJ projects\\SPPServer\\IMG_xyz.jpg");
        BufferedImage bufferedImage= null;
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WritableRaster raster=bufferedImage.getRaster();
        DataBufferByte data=(DataBufferByte) raster.getDataBuffer();
        mBytes=data.getData();
        System.out.println("Legth of image bytes: "+mBytes.length);
        return mBytes;
    }

    public static void main(String[] args) {
        try {
            // test code
            BufferedImage bufferedImage=ImageIO.read(new File("src\\IMG_xyz.jpg"));
            ByteArrayOutputStream bos =new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"jpg",bos);
            byte[] data=bos.toByteArray();
            byte[] imageInByte={-1,-2,-3,-4}; // ta tablica nie dziala
            ByteArrayInputStream bis=new ByteArrayInputStream(data);
            BufferedImage bufferedImage1=ImageIO.read(bis);
            ImageIO.write(bufferedImage1,"jpg",new File("testOutputFile.jpg"));
            System.out.println("Created in Tester");

//            //good code but doesnt work
//            byte[] imageInByte={-1,-2,-3,-4};
//            InputStream in=new ByteArrayInputStream(imageInByte);
//            BufferedImage bfImage =ImageIO.read(in);//returns null
//            File file=new File("testFile.jpg");
//            ImageIO.write(bfImage,"jpg",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//    File originalFile=new File("C:\\Users\\adminn\\Documents\\IntelliJ projects\\SPPServer\\IMG_xyz.jpg");
//    BufferedImage bufferedImage= null;
//        try {
//                bufferedImage = ImageIO.read(originalFile);
//                } catch (IOException e) {
//                e.printStackTrace();
//                }
//                ByteArrayOutputStream baos=new ByteArrayOutputStream();
//                ImageIO.write()
//
//                WritableRaster raster=bufferedImage.getRaster();
//                DataBufferByte data=(DataBufferByte) raster.getDataBuffer();
//                mBytes=data.getData();
//
//                System.out.println("Legth of image bytes: "+mBytes.length);
//                //System.out.println(Arrays.toString(mBytes));
//
//                try {
//                InputStream in=new ByteArrayInputStream(mBytes);
//                BufferedImage img=ImageIO.read(in);
//                File file=new File("AAAAAAAAAA.jpg");
//                ImageIO.write(img,"jpg",file);
//                } catch (IOException e) {
//                e.printStackTrace();
//                }
//
////        FileOutputStream fos= null;
////        try {
////            fos = new FileOutputStream("sdigndsoigndsongo.txt");
////            fos.write(mBytes);
////            fos.close();
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
