/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { StockComponentsPage, StockDeleteDialog, StockUpdatePage } from './stock.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Stock e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let stockUpdatePage: StockUpdatePage;
    let stockComponentsPage: StockComponentsPage;
    let stockDeleteDialog: StockDeleteDialog;
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

    it('should load Stocks', async () => {
        await navBarPage.goToEntity('stock');
        stockComponentsPage = new StockComponentsPage();
        expect(await stockComponentsPage.getTitle()).to.eq('newLocalApp.stock.home.title');
    });

    it('should load create Stock page', async () => {
        await stockComponentsPage.clickOnCreateButton();
        stockUpdatePage = new StockUpdatePage();
        expect(await stockUpdatePage.getPageTitle()).to.eq('newLocalApp.stock.home.createOrEditLabel');
        await stockUpdatePage.cancel();
    });

    it('should create and save Stocks', async () => {
        const nbButtonsBeforeCreate = await stockComponentsPage.countDeleteButtons();

        await stockComponentsPage.clickOnCreateButton();
        await promise.all([
            stockUpdatePage.setQuantityInitInput('5'),
            stockUpdatePage.setQuantityRemainingInput('5'),
            stockUpdatePage.setPriceUnitInput('5'),
            stockUpdatePage.setImageInput(absolutePath),
            stockUpdatePage.setOnSaleDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            stockUpdatePage.setExpiryDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            stockUpdatePage.productTypeSelectLastOption(),
            stockUpdatePage.personSelectLastOption()
        ]);
        expect(await stockUpdatePage.getQuantityInitInput()).to.eq('5');
        expect(await stockUpdatePage.getQuantityRemainingInput()).to.eq('5');
        expect(await stockUpdatePage.getPriceUnitInput()).to.eq('5');
        expect(await stockUpdatePage.getImageInput()).to.endsWith(fileNameToUpload);
        expect(await stockUpdatePage.getOnSaleDateInput()).to.contain('2001-01-01T02:30');
        expect(await stockUpdatePage.getExpiryDateInput()).to.contain('2001-01-01T02:30');
        const selectedBio = stockUpdatePage.getBioInput();
        if (await selectedBio.isSelected()) {
            await stockUpdatePage.getBioInput().click();
            expect(await stockUpdatePage.getBioInput().isSelected()).to.be.false;
        } else {
            await stockUpdatePage.getBioInput().click();
            expect(await stockUpdatePage.getBioInput().isSelected()).to.be.true;
        }
        const selectedAvailable = stockUpdatePage.getAvailableInput();
        if (await selectedAvailable.isSelected()) {
            await stockUpdatePage.getAvailableInput().click();
            expect(await stockUpdatePage.getAvailableInput().isSelected()).to.be.false;
        } else {
            await stockUpdatePage.getAvailableInput().click();
            expect(await stockUpdatePage.getAvailableInput().isSelected()).to.be.true;
        }
        await stockUpdatePage.save();
        expect(await stockUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await stockComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Stock', async () => {
        const nbButtonsBeforeDelete = await stockComponentsPage.countDeleteButtons();
        await stockComponentsPage.clickOnLastDeleteButton();

        stockDeleteDialog = new StockDeleteDialog();
        expect(await stockDeleteDialog.getDialogTitle()).to.eq('newLocalApp.stock.delete.question');
        await stockDeleteDialog.clickOnConfirmButton();

        expect(await stockComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
