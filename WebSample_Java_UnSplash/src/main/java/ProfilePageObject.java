import java.util.List;

public class ProfilePageObject extends AbtractPage {
    String editButton = "//a[contains(text(),'Edit profile')]";
    String usernameInput = "//input[@name='user[username]']";
    String updateButton = "(//input[@name='commit'])[1]";
    String userFullName = "//div[contains(text(),'%s')]";
    String numberOfLikedImage = "(//a[@data-test='user-nav-link-likes'])";
    String likedOrAddedImage = "//a[@href = '/photos/%s']";
    String likeButton = "(//div[@data-test='photos-route']//button[@title='Like photo'])[1]";
    String closeButton = "(//div[@class='ReactModalPortal']//button)[1]";
    String updateMessage = "//div[contains(text(),'Account updated!')]";
    String remainingImage = "//span[text() = '%s photo']";
    WebAPI webAPI;

    public ProfilePageObject() {
        webAPI = new WebAPI();
    }

    public void clickEditProfile() {
        clickOnElement(editButton);
    }

    public void changeUsername(String username) {
        sendTextToElement(usernameInput, username);
    }
    
    public void clickUpdateButton() {
        clickOnElement(updateButton);
        waitToElementPresence(updateMessage);
    }

    public boolean verifyUsernameInProfile(String username, String fullname) {
        String xpath = String.format(userFullName, fullname);
        gotoUrl("https://unsplash.com/@" + username);
        webAPI.changeUsernameToDefault();
        return (isElementDisplay(xpath)
        && getTitle().contains(username));
    }

    public boolean verifyLikedImage(List<String> images) {
        //Get number of liked image
        boolean value = getTextValue(numberOfLikedImage).contains(String.valueOf(images.size()));
        //Check liked image is display correctly by using url
        for (String url : images) {
            String xpath = String.format(likedOrAddedImage, url);
            value = isElementDisplay(xpath);
            webAPI.unlikePhoto(url);
        }
        return value;
    }

    public boolean verifyCollection(List<String> images, String id) {
        String numberOfPhotoXpath = String.format(remainingImage, images.size());
        boolean value = isElementDisplay(numberOfPhotoXpath);
        for (String url : images) {
            String xpath = String.format(likedOrAddedImage, url);
            value = isElementDisplay(xpath);
        }
        webAPI.deleteCollection(id);
        return value;
    }
}
