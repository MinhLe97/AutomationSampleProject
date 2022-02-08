public class LoginPageObject extends AbtractPage {
    String loginPageButton = "//a[contains(text(), 'Login')]";
    String userName = "//input[@id='user_email']";
    String password = "//input[@id='user_password']";
    String loginButton = "//input[@type='submit']";

    public void loginWithUserNamePassword(String userNameText, String passwordText) {
        gotoUrl("https://unsplash.com/");
        clickOnElement(loginPageButton);
        sendTextToElement(userName, userNameText);
        sendTextToElement(password, passwordText);
        clickOnElement(loginButton);
    }

}
