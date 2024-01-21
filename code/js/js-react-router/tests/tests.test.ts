import {test, expect} from '@playwright/test';
import {Simulate} from "react-dom/test-utils";
import click = Simulate.click;

test('Go Home', async ({page}) => {
    await page.goto("http://localhost:8081/")
    const header = await page.getByRole('heading').allInnerTexts()
    const link = await page.getByRole("link").allInnerTexts()
    expect(header).toEqual(["Home"])
    expect(link).toEqual(["Home", "Statistics", "About Us", "Login", "Sign Up", "Get Game By Id"])
})

test('Go About Us', async ({page}) => {
    await page.goto("http://localhost:8081/authors")
    const header = await page.getByRole('heading').allInnerTexts()
    expect(header).toEqual(["Loading"])
})

test('Go Statistics page and search for user Vasco', async ({page}) => {
    await page.goto("http://localhost:8081/stats")

    await page.getByPlaceholder('username').fill('Vasco')
    await page.getByRole('button', {name: 'Search'}).click()
    await expect(page.getByText("Vasco")).toBeVisible()
})

test('Go to login', async ({page}) => {
    await page.goto("http://localhost:8081/login")
    await page.getByTestId('teste').click()
    await expect(page.getByText("Invalid username")).toBeVisible()
})

test('Go to sign', async ({page}) => {
    await page.goto("http://localhost:8081/login")
    await page.getByTestId('teste').click()
    await expect(page.getByText("Invalid username")).toBeVisible()
})



