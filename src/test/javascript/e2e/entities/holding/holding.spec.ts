/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { HoldingComponentsPage, HoldingDeleteDialog, HoldingUpdatePage } from './holding.page-object';

const expect = chai.expect;

describe('Holding e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let holdingUpdatePage: HoldingUpdatePage;
    let holdingComponentsPage: HoldingComponentsPage;
    /*let holdingDeleteDialog: HoldingDeleteDialog;*/

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Holdings', async () => {
        await navBarPage.goToEntity('holding');
        holdingComponentsPage = new HoldingComponentsPage();
        expect(await holdingComponentsPage.getTitle()).to.eq('newLocalApp.holding.home.title');
    });

    it('should load create Holding page', async () => {
        await holdingComponentsPage.clickOnCreateButton();
        holdingUpdatePage = new HoldingUpdatePage();
        expect(await holdingUpdatePage.getPageTitle()).to.eq('newLocalApp.holding.home.createOrEditLabel');
        await holdingUpdatePage.cancel();
    });

    /* it('should create and save Holdings', async () => {
        const nbButtonsBeforeCreate = await holdingComponentsPage.countDeleteButtons();

        await holdingComponentsPage.clickOnCreateButton();
        await promise.all([
            holdingUpdatePage.setSiretInput('siret'),
            holdingUpdatePage.setNameInput('name'),
            holdingUpdatePage.setDescriptionInput('description'),
            holdingUpdatePage.locationSelectLastOption(),
            holdingUpdatePage.ownerSelectLastOption(),
            // holdingUpdatePage.imageSelectLastOption(),
        ]);
        expect(await holdingUpdatePage.getSiretInput()).to.eq('siret');
        expect(await holdingUpdatePage.getNameInput()).to.eq('name');
        expect(await holdingUpdatePage.getDescriptionInput()).to.eq('description');
        await holdingUpdatePage.save();
        expect(await holdingUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await holdingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });*/

    /* it('should delete last Holding', async () => {
        const nbButtonsBeforeDelete = await holdingComponentsPage.countDeleteButtons();
        await holdingComponentsPage.clickOnLastDeleteButton();

        holdingDeleteDialog = new HoldingDeleteDialog();
        expect(await holdingDeleteDialog.getDialogTitle())
            .to.eq('newLocalApp.holding.delete.question');
        await holdingDeleteDialog.clickOnConfirmButton();

        expect(await holdingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
