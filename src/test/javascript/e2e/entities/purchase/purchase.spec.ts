/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PurchaseComponentsPage, PurchaseDeleteDialog, PurchaseUpdatePage } from './purchase.page-object';

const expect = chai.expect;

describe('Purchase e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let purchaseUpdatePage: PurchaseUpdatePage;
    let purchaseComponentsPage: PurchaseComponentsPage;
    let purchaseDeleteDialog: PurchaseDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Purchases', async () => {
        await navBarPage.goToEntity('purchase');
        purchaseComponentsPage = new PurchaseComponentsPage();
        expect(await purchaseComponentsPage.getTitle()).to.eq('newLocalApp.purchase.home.title');
    });

    it('should load create Purchase page', async () => {
        await purchaseComponentsPage.clickOnCreateButton();
        purchaseUpdatePage = new PurchaseUpdatePage();
        expect(await purchaseUpdatePage.getPageTitle()).to.eq('newLocalApp.purchase.home.createOrEditLabel');
        await purchaseUpdatePage.cancel();
    });

    it('should create and save Purchases', async () => {
        const nbButtonsBeforeCreate = await purchaseComponentsPage.countDeleteButtons();

        await purchaseComponentsPage.clickOnCreateButton();
        await promise.all([
            purchaseUpdatePage.setSaleDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            purchaseUpdatePage.setQuantityInput('5'),
            purchaseUpdatePage.stockSelectLastOption(),
            purchaseUpdatePage.clientSelectLastOption()
        ]);
        expect(await purchaseUpdatePage.getSaleDateInput()).to.contain('2001-01-01T02:30');
        expect(await purchaseUpdatePage.getQuantityInput()).to.eq('5');
        await purchaseUpdatePage.save();
        expect(await purchaseUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await purchaseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Purchase', async () => {
        const nbButtonsBeforeDelete = await purchaseComponentsPage.countDeleteButtons();
        await purchaseComponentsPage.clickOnLastDeleteButton();

        purchaseDeleteDialog = new PurchaseDeleteDialog();
        expect(await purchaseDeleteDialog.getDialogTitle()).to.eq('newLocalApp.purchase.delete.question');
        await purchaseDeleteDialog.clickOnConfirmButton();

        expect(await purchaseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
