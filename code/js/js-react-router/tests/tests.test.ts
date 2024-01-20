import {test, expect} from '@playwright/test';

test('Go Home', async ({page}) => {

    await page.goto("http://localhost:8081/")

    const header = await page.getByRole('heading').allInnerTexts()
    console.log(header)
    expect(header).toEqual(["Home"])
})