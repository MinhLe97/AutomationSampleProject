import { test, expect, type Page, request } from '@playwright/test';

test.describe('Unsplash page', () => {
    test('', async ({ page, request }) => {
        await page.goto('https://unsplash.com/');
        await page.locator('[href="https://unsplash.com/login"]').click();
        await page.locator('#user_email').fill('leminh.tran.1997@gmail.com');
        await page.locator('#user_password').fill('Admin@123456')
        await page.locator('[type=submit]').click();
        await page.locator('[itemprop="contentUrl"]').first().click();
        const url = page.url();
        await page.locator('[data-test="photos-route"] [title="Like"]').click();
        await page.locator('[class="ReactModalPortal"] button').first().click();
        await page.goto('https://unsplash.com/@leminh1997/likes');
        const image = await page.locator('[itemprop="contentUrl"]').getAttribute('href');
        const imageId = image?.substring(image.indexOf('/photos' + 7));
        expect(url).toContain(image);
        const response = await request.delete(`/photos/${imageId}/like`)
        expect(response.ok());
    })
});