import java.util.ArrayList;
import java.util.List;

public class HomePageObject extends AbtractPage {
    String imageView = "(//figure//a[@itemprop='contentUrl'])[%s]";
    String authorLog = "(//div[@data-test='photos-route']//following::a[@href])[1]";
    String followButton = "(//div[@data-test='photos-route']//following::button)[1]";
    String logoButton = "//div[@id='popover-avatar-loggedin-menu-desktop']";
    String profileLink = "//a[contains(text(),'profile')]";
    String likeButton = "(//div[@data-test='photos-route']//button[@title='Like photo'])[1]";
    String addCollectionButton = "(//div[@data-test='photos-route']//button[@title='Add to collection'])[1]";
    String closeButton = "(//div[@class='ReactModalPortal']//button)[1]";
    String downloadButton = "(//div[@data-test='photos-route']//a[@title='Download photo'])[1]";
    String collectionName = "//h4[contains(text(), '%s')]";
    String imageInCollection = "//preceding::p[contains(text(), '%s')]";
    String addedImage = "//a[@href = '/photos/%s']";
    WebAPI webAPI;

    public HomePageObject() {
        webAPI = new WebAPI();
    }

    public void clickFirstImgge() {
        String xpath = String.format(imageView, "1");
        clickOnElement(xpath);
    }

    public void hoverUserIcon() {
        hoverOnElement(authorLog);
    }

    public void clickFollow() {
        clickOnElement(followButton);
    }

    public void gotoProfile() {
        clickOnElement(logoButton);
        clickOnElement(profileLink);
    }

    public boolean verifyFollowButton() {
        sleepInSecond(2);
        boolean value = (getColorValue(followButton).equals("rgba(238, 238, 238, 1)")
            && getTextValue(followButton).equals("Following"));
        clickOnElement(followButton); //Done test return the original state
        return value;
    }

    public List<String> likePhotos(int num) {
        List<String> listLikedImange = new ArrayList<String>();
        String xpathImageView;
        String imageName;
        for(int i = 0; i < num; i++) {
            xpathImageView = String.format(imageView, String.valueOf(i+1));
            imageName = getLinkText(xpathImageView);
            listLikedImange.add(imageName.substring(imageName.indexOf("photos/") + 7));
            clickOnElement(xpathImageView);
            clickOnElement(likeButton);
            clickOnElement(closeButton);
        }
        return listLikedImange;
    }

    public List<String> clickDownload(int num) {
        List<String> listDownloadedImange = new ArrayList<String>();
        String xpathImageView;
        String imageName;
        for(int i = 0; i < num; i++) {
            xpathImageView = String.format(imageView, String.valueOf(i+1));
            imageName = getLinkText(xpathImageView);
            listDownloadedImange.add(imageName.substring(imageName.indexOf("photos/") + 7));
            clickOnElement(xpathImageView);
            clickOnElement(downloadButton);
            sleepInSecond(5);
            clickOnElement(closeButton);
        }
        return listDownloadedImange;
    }

    public boolean verifyDownloadedImage(List<String> images) {
        boolean value = false;
        //Handle if need to download multiple image
        for(String image : images) {
            value = false;
            for(String name : getDownloadedName()) {
                if(name.contains(image)) {
                    value = true;
                }
            }
        }
        //Delete images after verify
        CleanDownloadDirectory();
        return value;
    }

    public String createNewPrivateCollection(String name) {
        return webAPI.createCollection(name);
    }

    public List<String> addImagesToCollection(int num, String name) {
        List<String> listDownloadedImange = new ArrayList<String>();
        String xpathImageView;
        String collectionXpath;
        String imageName;
        for(int i = 0; i < num; i++) {
            xpathImageView = String.format(imageView, String.valueOf(i+1));
            collectionXpath = String.format(collectionName, name);
            imageName = getLinkText(xpathImageView);
            listDownloadedImange.add(imageName.substring(imageName.indexOf("photos/") + 7));
            clickOnElement(xpathImageView);
            clickOnElement(addCollectionButton);
            clickOnElement(collectionXpath);
            waitToElementPresence(collectionXpath 
                + String.format(imageInCollection, String.valueOf(i+1)));
            clickCoordinate(10, 10);
            clickOnElement(closeButton);
        }
        return listDownloadedImange;
    }

    public List<String> removeLastImagesFromCollection(List<String> images, String name) {
        List<String> listDownloadedImange = images;
        String imageName;
        String collectionXpath;
        String xpath = String.format(addedImage, images.get(images.size()-1));
        collectionXpath = String.format(collectionName, name);
        imageName = getLinkText(xpath);
        listDownloadedImange.remove(imageName.substring(imageName.indexOf("photos/") + 7));
        clickOnElement(xpath);
        clickOnElement(addCollectionButton);
        clickOnElement(collectionXpath);
        waitToElementPresence(collectionXpath 
            + String.format(imageInCollection, String.valueOf(listDownloadedImange.size())));
        clickCoordinate(10, 10);
        clickOnElement(closeButton);
        return listDownloadedImange;
    }

}
