/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProductTypeComponentsPage, ProductTypeDeleteDialog, ProductTypeUpdatePage } from './product-type.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('ProductType e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let productTypeUpdatePage: ProductTypeUpdatePage;
    let productTypeComponentsPage: ProductTypeComponentsPage;
    let productTypeDeleteDialog: ProductTypeDeleteDialog;
    const fileNameToUpload = 'logo-jhipster.png';
    const fileToUpload = '../../../../../main/webapp/content/images/' + fileNameToUpload;
    const absolutePath = path.resolve(__dirname, fileToUpload);

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load ProductTypes', async () => {
        await navBarPage.goToEntity('product-type');
        productTypeComponentsPage = new ProductTypeComponentsPage();
        expect(await productTypeComponentsPage.getTitle()).to.eq('newLocalApp.productType.home.title');
    });

    it('should load create ProductType page', async () => {
        await productTypeComponentsPage.clickOnCreateButton();
        productTypeUpdatePage = new ProductTypeUpdatePage();
        expect(await productTypeUpdatePage.getPageTitle()).to.eq('newLocalApp.productType.home.createOrEditLabel');
        await productTypeUpdatePage.cancel();
    });

    it('should create and save ProductTypes', async () => {
        const nbButtonsBeforeCreate = await productTypeComponentsPage.countDeleteButtons();

        await productTypeComponentsPage.clickOnCreateButton();
        await promise.all([
            productTypeUpdatePage.setNameInput('name'),
            productTypeUpdatePage.setDescriptionInput('description'),
            productTypeUpdatePage.setImageInput(absolutePath),
            productTypeUpdatePage.categorySelectLastOption()
        ]);
        expect(await productTypeUpdatePage.getNameInput()).to.eq('name');
        expect(await productTypeUpdatePage.getDescriptionInput()).to.eq('description');
        expect(await productTypeUpdatePage.getImageInput()).to.endsWith(fileNameToUpload);
        await productTypeUpdatePage.save();
        expect(await productTypeUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await productTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last ProductType', async () => {
        const nbButtonsBeforeDelete = await productTypeComponentsPage.countDeleteButtons();
        await productTypeComponentsPage.clickOnLastDeleteButton();

        productTypeDeleteDialog = new ProductTypeDeleteDialog();
        expect(await productTypeDeleteDialog.getDialogTitle()).to.eq('newLocalApp.productType.delete.question');
        await productTypeDeleteDialog.clickOnConfirmButton();

        expect(await productTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
