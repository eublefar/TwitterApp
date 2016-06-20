package greetingform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.*;
import java.nio.file.Files;

/**
 * Created by Pan on 6/11/2016.
 */

public class TwitterAuth {

    private static TwitterAuth inst=null;
    private static String accessTokenFileName = "a123fa019.bin";
    private  Stage primaryStage;
    private  WebView webView;
    private  AsyncTwitter asyncTwitter;
    private  Twitter twitter;
    private AccessToken accessToken = null;
    private RequestToken requestToken;

    TwitterAuth() throws TwitterException {
        asyncTwitter =new AsyncTwitterFactory().getInstance();
        asyncTwitter.setOAuthConsumer("ZGapCYi7YVJmRGO4WJn8PnFjK", "lrbAFQO5pQKWKdDvsVc8cJSYcMGopeU89MoroCxShLBwmrlEom");
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("ZGapCYi7YVJmRGO4WJn8PnFjK", "lrbAFQO5pQKWKdDvsVc8cJSYcMGopeU89MoroCxShLBwmrlEom");
        requestToken = asyncTwitter.getOAuthRequestToken();
    }

     public void authenticate() throws TwitterException {

         File storedAccessToken = new File(accessTokenFileName);
         storedAccessToken = storedAccessToken.getAbsoluteFile();
         if(storedAccessToken.exists()) {
             try {
                    authFromFile(storedAccessToken);
                    return;
             } catch(IOException | ClassNotFoundException io) {
                    storedAccessToken.delete();
                    TwitterAuth.getInstance().authenticate();
             }
         }

        primaryStage = new Stage();

        final WebEngine webEngine = setUpWebEngine();
        webEngine.load(requestToken.getAuthorizationURL());

        setUpStage();
        primaryStage.show();
    }

    private void authFromFile(File file) throws IOException, ClassNotFoundException {
        AccessToken accessToken = (AccessToken) deserialize(Files.readAllBytes(file.toPath()));
        twitter.setOAuthAccessToken(accessToken);
        asyncTwitter.setOAuthAccessToken(accessToken);
        asyncTwitter.getOAuthAccessTokenAsync();
    }

     private void setUpStage() {

        Scene scene = new Scene(new Group());
        VBox root = new VBox();

        root.getChildren().addAll(webView);

        scene.setRoot(root);
        primaryStage.getIcons().add(new Image("http://stlaz.com/wp-content/uploads/2012/11/white-square.jpg"));
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(Main.primaryStage);
    }

     private WebEngine setUpWebEngine() {
        webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        try {
                            handleWEChange(newState, webEngine);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return webEngine;
    }


    private String getCode(Element el, int level){
        String res = null, temp;
        NodeList childNodes = el.getChildNodes();
        if(el.getNodeName().toLowerCase().equals("code")){
            res =  el.getTextContent();
        }
        for(int i=0; i<childNodes.getLength(); i++){
            Node item = childNodes.item(i);
            if(item instanceof Element){

                temp =getCode((Element)item, level++);
                if(temp !=null)
                    res = temp;
            }
        }
        return res;
    }

    private void handleWEChange(Worker.State newState, WebEngine webEngine ) throws IOException {
        if (newState == Worker.State.SUCCEEDED) {
            Document doc = webEngine.getDocument();
            Element el = doc.getDocumentElement();
            String pin = getCode(el, 0);
            System.out.println(pin + " ");
            try {
                if (pin.length() > 0) {
                    accessToken = asyncTwitter.getOAuthAccessToken(requestToken, pin);
                    twitter.setOAuthAccessToken(accessToken);
                    storeToken(accessToken);
                    asyncTwitter.getOAuthAccessTokenAsync();
                    primaryStage.close();
                }
            } catch (TwitterException | NullPointerException te) {
                return;
            }
        }
    }

    private void storeToken(AccessToken token) throws IOException {
        File file = new File(accessTokenFileName);
        file.createNewFile();
        Files.write(file.toPath(),serialize(token));
    }

     public static TwitterAuth getInstance() throws TwitterException {
        if(inst == null)
            inst = new TwitterAuth();
        return inst;
    }

    public void addListener(TwitterListener listener) {
        asyncTwitter.addListener(listener);
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

}
