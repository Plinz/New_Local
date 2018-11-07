/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PurchasePendingComponentsPage, PurchasePendingDeleteDialog, PurchasePendingUpdatePage } from './purchase-pending.page-object';

const expect = chai.expect;

describe('PurchasePending e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let purchasePendingUpdatePage: PurchasePendingUpdatePage;
    let purchasePendingComponentsPage: PurchasePendingComponentsPage;
    let purchasePendingDeleteDialog: PurchasePendingDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load PurchasePendings', async () => {
        await navBarPage.goToEntity('purchase-pending');
        purchasePendingComponentsPage = new PurchasePendingComponentsPage();
        expect(await purchasePendingComponentsPage.getTitle()).to.eq('newLocalApp.purchasePending.home.title');
    });

    it('should load create PurchasePending page', async () => {
        await purchasePendingComponentsPage.clickOnCreateButton();
        purchasePendingUpdatePage = new PurchasePendingUpdatePage();
        expect(await purchasePendingUpdatePage.getPageTitle()).to.eq('newLocalApp.purchasePending.home.createOrEditLabel');
        await purchasePendingUpdatePage.cancel();
    });

    it('should create and save PurchasePendings', async () => {
        const nbButtonsBeforeCreate = await purchasePendingComponentsPage.countDeleteButtons();

        await purchasePendingComponentsPage.clickOnCreateButton();
        await promise.all([
            purchasePendingUpdatePage.setQuantityInput('5'),
            purchasePendingUpdatePage.stockSelectLastOption(),
            purchasePendingUpdatePage.personSelectLastOption()
        ]);
        expect(await purchasePendingUpdatePage.getQuantityInput()).to.eq('5');
        await purchasePendingUpdatePage.save();
        expect(await purchasePendingUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await purchasePendingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last PurchasePending', async () => {
        const nbButtonsBeforeDelete = await purchasePendingComponentsPage.countDeleteButtons();
        await purchasePendingComponentsPage.clickOnLastDeleteButton();

        purchasePendingDeleteDialog = new PurchasePendingDeleteDialog();
        expect(await purchasePendingDeleteDialog.getDialogTitle()).to.eq('newLocalApp.purchasePending.delete.question');
        await purchasePendingDeleteDialog.clickOnConfirmButton();

        expect(await purchasePendingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
