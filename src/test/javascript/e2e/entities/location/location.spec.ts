/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { LocationComponentsPage, LocationDeleteDialog, LocationUpdatePage } from './location.page-object';

const expect = chai.expect;

describe('Location e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let locationUpdatePage: LocationUpdatePage;
    let locationComponentsPage: LocationComponentsPage;
    let locationDeleteDialog: LocationDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Locations', async () => {
        await navBarPage.goToEntity('location');
        locationComponentsPage = new LocationComponentsPage();
        expect(await locationComponentsPage.getTitle()).to.eq('newLocalApp.location.home.title');
    });

    it('should load create Location page', async () => {
        await locationComponentsPage.clickOnCreateButton();
        locationUpdatePage = new LocationUpdatePage();
        expect(await locationUpdatePage.getPageTitle()).to.eq('newLocalApp.location.home.createOrEditLabel');
        await locationUpdatePage.cancel();
    });

    it('should create and save Locations', async () => {
        const nbButtonsBeforeCreate = await locationComponentsPage.countDeleteButtons();

        await locationComponentsPage.clickOnCreateButton();
        await promise.all([
            locationUpdatePage.setCityInput('city'),
            locationUpdatePage.setCountryInput('country'),
            locationUpdatePage.setZipInput('zip'),
            locationUpdatePage.setAddressInput('address'),
            locationUpdatePage.setLonInput('5'),
            locationUpdatePage.setLatInput('5'),
            locationUpdatePage.personSelectLastOption()
        ]);
        expect(await locationUpdatePage.getCityInput()).to.eq('city');
        expect(await locationUpdatePage.getCountryInput()).to.eq('country');
        expect(await locationUpdatePage.getZipInput()).to.eq('zip');
        expect(await locationUpdatePage.getAddressInput()).to.eq('address');
        expect(await locationUpdatePage.getLonInput()).to.eq('5');
        expect(await locationUpdatePage.getLatInput()).to.eq('5');
        await locationUpdatePage.save();
        expect(await locationUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await locationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Location', async () => {
        const nbButtonsBeforeDelete = await locationComponentsPage.countDeleteButtons();
        await locationComponentsPage.clickOnLastDeleteButton();

        locationDeleteDialog = new LocationDeleteDialog();
        expect(await locationDeleteDialog.getDialogTitle()).to.eq('newLocalApp.location.delete.question');
        await locationDeleteDialog.clickOnConfirmButton();

        expect(await locationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
