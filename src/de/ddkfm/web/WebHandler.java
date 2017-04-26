package de.ddkfm.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by maxsc on 25.04.2017.
 */
public class WebHandler implements HttpHandler {
    private BufferedImage image = null;
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch(httpExchange.getRequestMethod().toLowerCase()) {
            case "get":
                String path = httpExchange.getRequestURI().getPath();
                if(path.endsWith("snapshot.png")) {
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", outStream);
                    httpExchange.sendResponseHeaders(200,outStream.toByteArray().length);
                    ImageIO.write(image, "png", httpExchange.getResponseBody());
                } else {
                    String response = "<html>" +
                            "<head>" +
                            "<script type=\"text/javascript\">" +
                            "function startTimer() {" +
                            "   window.setInterval(function() {" +
                            "       document.getElementById('snapshot').src = \"web/snapshot.png?random=\" + new Date().getTime();" +
                            "}, 500);" +
                            "}" +
                            "</script>" +
                            "</head>" +
                            "<body onLoad=\"startTimer()\">" +
                            "<img src=\"web/snapshot.png\" id=\"snapshot\"/>" +
                            "</body>" +
                            "</html>";
                    httpExchange.getResponseHeaders().add("Content-Type", "text/html");
                    httpExchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
                    writeToResponseBody(httpExchange,response);
                }
                break;
        }
    }
    private void writeToResponseBody(HttpExchange httpExchange, String data) {
        int BUFFER_SIZE = 8192;
        try (BufferedOutputStream out = new BufferedOutputStream(httpExchange.getResponseBody())) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(data.getBytes("UTF-8"))) {
                byte [] buffer = new byte [BUFFER_SIZE];
                int count ;
                while ((count = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
