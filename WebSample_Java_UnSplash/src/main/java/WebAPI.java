import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class WebAPI {
    String userName;
    String token;
    String baseUrl = "https://api.unsplash.com";
    String urlUpdateUserName = "/me?username=%s";
    String urlGetCollection = "/users/%s/collections";
    String urlUnlikePhoto = "/photos/%s/like";
    String urlNewPrivateCollection = "/collections?title=%s&private=true";
    String urlDeleteCollection = "/collections/%s";

    public WebAPI() {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(System.getProperty("user.dir") + "/Data.json"));
            JSONObject jsonObject = (JSONObject) obj;
            userName = jsonObject.get("userName").toString();
            token = jsonObject.get("apiToken").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeUsernameToDefault() {
        String url = baseUrl + String.format(urlUpdateUserName, userName);
        RestAssured.given()
            .header("Accept", "*/*")
            .header("Authorization", token)
            .request().put(url);
    }

    public void unlikePhoto(String id) {
        String url = baseUrl + String.format(urlUnlikePhoto, id);
        RestAssured.given()
            .header("Accept", "*/*")
            .header("Authorization", token)
            .request().delete(url);
    }

    public void getImagesCollection(String userName) {
        String url = baseUrl + String.format(urlGetCollection, userName);
        RestAssured.given()
            .header("Accept", "*/*")
            .header("Authorization", token)
            .request().get(url);
    }

    public String createCollection(String name) {
        String url = baseUrl + String.format(urlNewPrivateCollection, name);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        Response response = RestAssured.given()
            .header("Accept", "*/*")
            .header("Authorization", token)
            .request().post(url);
        try {
            jsonObject = (JSONObject) parser.parse(response.body().asString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return jsonObject.get("id").toString();
    }

    public void deleteCollection(String id) {
        String url = baseUrl + String.format(urlDeleteCollection, id);
        RestAssured.given()
            .header("Accept", "*/*")
            .header("Authorization", token)
            .request().delete(url);
    }
}
