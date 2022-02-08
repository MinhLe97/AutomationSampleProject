import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.*;

public class TestWeb {
    String email;
    String password;
    String userName;
    String fullName;

    @BeforeTest
    public void setUp() {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(System.getProperty("user.dir") + "/Data.json"));
            JSONObject jsonObject = (JSONObject) obj;
            email = jsonObject.get("email").toString();
            password = jsonObject.get("password").toString();
            userName = jsonObject.get("userName").toString();
            fullName = jsonObject.get("fullName").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1)
    public void loginPage() {
        LoginPageObject loginPageObject = new LoginPageObject();
        HomePageObject homePageObject = new HomePageObject();

        //1. Given I log in with account "<account_name>"
        loginPageObject.loginWithUserNamePassword(email, password);
        //2. And I click the first photo on home page
        homePageObject.clickFirstImgge();
        //3. When I hover on icon user at the top left corner
        homePageObject.hoverUserIcon();
        //4. And I click the Follow button
        homePageObject.clickFollow();
        //5. Then I observe button background color turn into white and button text turn into Following
        Assert.assertTrue(homePageObject.verifyFollowButton()
            , "Color or Text is not correct");
    }

    @Test(priority = 2)
    public void updateUsername() {
        LoginPageObject loginPageObject = new LoginPageObject();
        HomePageObject homePageObject = new HomePageObject();
        ProfilePageObject profilePageObject = new ProfilePageObject();
        String newUserName = userName + new Random().nextInt(10);

        //1. Given I log in with account "<account_name>"
        loginPageObject.loginWithUserNamePassword(email, password);
        //2. And I go to the Profile page
        homePageObject.gotoProfile();
        //3. When I click Edit tags link
        profilePageObject.clickEditProfile();
        //4. And I edit the username field
        profilePageObject.changeUsername(newUserName);
        //5. And I click the Update Account button
        profilePageObject.clickUpdateButton();
        /*6. And I go to Profile page
             Verify that it will take me to the Profile page
             And my Fullname is display correctly*/
        Assert.assertTrue(profilePageObject.verifyUsernameInProfile(newUserName, fullName)
            , "Username or FullName is incorrect");
    }

    @Test(priority = 3)
    public void listOfLikedPhotos() {
        List<String> listLikedImange = new ArrayList<String>();
        LoginPageObject loginPageObject = new LoginPageObject();
        HomePageObject homePageObject = new HomePageObject();
        ProfilePageObject profilePageObject = new ProfilePageObject();

        //1. Given I log in with account "<account_name>"
        loginPageObject.loginWithUserNamePassword(email, password);
        //2. And I like 3 random photos
        listLikedImange = homePageObject.likePhotos(3);
        //3. Go to liked image page
        loginPageObject.gotoUrl("https://unsplash.com/@" + userName + "/likes");
        //4. Verify liked image
        Assert.assertTrue(profilePageObject.verifyLikedImage(listLikedImange)
            , "Image is diplay incorrectly");
    }

    @Test(priority = 4)
    public void removePhotosFromCollection() {
        List<String> listAddedImange = new ArrayList<String>();
        String collectionId;
        LoginPageObject loginPageObject = new LoginPageObject();
        HomePageObject homePageObject = new HomePageObject();
        ProfilePageObject profilePageObject = new ProfilePageObject();

        //1. Given I log in with account "<account_name>"
        loginPageObject.loginWithUserNamePassword(email, password);
        //2. And I create a private collection
        collectionId = homePageObject.createNewPrivateCollection(fullName);
        //3. And I add 2 random photos to the newly created collection
        listAddedImange = homePageObject.addImagesToCollection(2, fullName);
        //4. And I remove 1 photo from the newly created collection
        listAddedImange = homePageObject.removeLastImagesFromCollection(listAddedImange, fullName);
        //5. When I go to *https://unsplash.com/collections/collection_id*
        loginPageObject.gotoUrl("https://unsplash.com/collections/" + collectionId);
        /*6. Then I notice that the photo has been removed successfully from the collection
            And there is only 1 remaining photo in the collection*/
        Assert.assertTrue(profilePageObject.verifyCollection(listAddedImange, collectionId));
    }

    @Test(priority = 5)
    public void downloadPhotos() {
        List<String> listDownloadedImange = new ArrayList<String>();

        LoginPageObject loginPageObject = new LoginPageObject();
        HomePageObject homePageObject = new HomePageObject();

        //1. Given I log in with account "<account_name>"
        loginPageObject.loginWithUserNamePassword(email, password);
        //2. When I open a random photo And I download this photo
        listDownloadedImange = homePageObject.clickDownload(2);
        Assert.assertTrue(homePageObject.verifyDownloadedImage(listDownloadedImange)
            ,"Image is not downloaded successfully");
    }

    @AfterMethod
    public  void cleanUp() {
        if(AbtractPage.getDriver() != null)
            AbtractPage.driver.quit();
            AbtractPage.setDriver(null);
    }
}
