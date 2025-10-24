package com.example.qrdemo.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// Utilidades para generar y decodificar códigos QR
public class QrUtils {

    // Genera un QR a partir de texto y devuelve bytes PNG
    public static byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        Map<EncodeHintType,Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage img = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "PNG", baos);
        return baos.toByteArray();
    }

    // Decodifica un QR desde un MultipartFile y devuelve el texto
    public static String decodeQRCode(MultipartFile multipartFile) throws Exception {
        try (InputStream is = multipartFile.getInputStream()) {
            BufferedImage bufferedImage = ImageIO.read(is);
            if (bufferedImage == null) return null;
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }
}
