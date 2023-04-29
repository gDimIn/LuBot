package Photo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class QrCodeGenerator {

    private static String botName = "";
    private static String botToken = "";
    //private Update update;
    private int width = 250;
    private int height = 250;

    public QrCodeGenerator(String name, String token, Update update) {
        if (botName.equals("") || botToken.equals("") ) {
            botName = name;
            botToken = token;
        }
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    private byte[] generateQRCode(String id) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(id, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
        return bos.toByteArray();
    }

    public SendPhoto getQRImage(String qrInfo) throws IOException, WriterException {
        byte[] qrCode = generateQRCode(qrInfo);
        InputStream inputStream = new ByteArrayInputStream(qrCode);
        InputFile inputFile = new InputFile(inputStream,"qr_code.png");

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(inputFile);

        return sendPhoto;
    }
}
