/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PurchaseDoneComponentsPage, PurchaseDoneDeleteDialog, PurchaseDoneUpdatePage } from './purchase-done.page-object';

const expect = chai.expect;

describe('PurchaseDone e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let purchaseDoneUpdatePage: PurchaseDoneUpdatePage;
    let purchaseDoneComponentsPage: PurchaseDoneComponentsPage;
    let purchaseDoneDeleteDialog: PurchaseDoneDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load PurchaseDones', async () => {
        await navBarPage.goToEntity('purchase-done');
        purchaseDoneComponentsPage = new PurchaseDoneComponentsPage();
        expect(await purchaseDoneComponentsPage.getTitle()).to.eq('newLocalApp.purchaseDone.home.title');
    });

    it('should load create PurchaseDone page', async () => {
        await purchaseDoneComponentsPage.clickOnCreateButton();
        purchaseDoneUpdatePage = new PurchaseDoneUpdatePage();
        expect(await purchaseDoneUpdatePage.getPageTitle()).to.eq('newLocalApp.purchaseDone.home.createOrEditLabel');
        await purchaseDoneUpdatePage.cancel();
    });

    it('should create and save PurchaseDones', async () => {
        const nbButtonsBeforeCreate = await purchaseDoneComponentsPage.countDeleteButtons();

        await purchaseDoneComponentsPage.clickOnCreateButton();
        await promise.all([
            purchaseDoneUpdatePage.setSaleDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            purchaseDoneUpdatePage.setQuantityInput('5'),
            purchaseDoneUpdatePage.stockSelectLastOption(),
            purchaseDoneUpdatePage.personSelectLastOption()
        ]);
        expect(await purchaseDoneUpdatePage.getSaleDateInput()).to.contain('2001-01-01T02:30');
        expect(await purchaseDoneUpdatePage.getQuantityInput()).to.eq('5');
        await purchaseDoneUpdatePage.save();
        expect(await purchaseDoneUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await purchaseDoneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last PurchaseDone', async () => {
        const nbButtonsBeforeDelete = await purchaseDoneComponentsPage.countDeleteButtons();
        await purchaseDoneComponentsPage.clickOnLastDeleteButton();

        purchaseDoneDeleteDialog = new PurchaseDoneDeleteDialog();
        expect(await purchaseDoneDeleteDialog.getDialogTitle()).to.eq('newLocalApp.purchaseDone.delete.question');
        await purchaseDoneDeleteDialog.clickOnConfirmButton();

        expect(await purchaseDoneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
