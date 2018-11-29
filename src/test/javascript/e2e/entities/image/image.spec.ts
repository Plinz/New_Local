/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ImageComponentsPage, ImageDeleteDialog, ImageUpdatePage } from './image.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Image e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let imageUpdatePage: ImageUpdatePage;
    let imageComponentsPage: ImageComponentsPage;
    let imageDeleteDialog: ImageDeleteDialog;
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

    it('should load Images', async () => {
        await navBarPage.goToEntity('image');
        imageComponentsPage = new ImageComponentsPage();
        expect(await imageComponentsPage.getTitle()).to.eq('newLocalApp.image.home.title');
    });

    it('should load create Image page', async () => {
        await imageComponentsPage.clickOnCreateButton();
        imageUpdatePage = new ImageUpdatePage();
        expect(await imageUpdatePage.getPageTitle()).to.eq('newLocalApp.image.home.createOrEditLabel');
        await imageUpdatePage.cancel();
    });

    it('should create and save Images', async () => {
        const nbButtonsBeforeCreate = await imageComponentsPage.countDeleteButtons();

        await imageComponentsPage.clickOnCreateButton();
        await promise.all([
            imageUpdatePage.setNameInput('name'),
            imageUpdatePage.setDescriptionInput('description'),
            imageUpdatePage.setImageInput(absolutePath)
        ]);
        expect(await imageUpdatePage.getNameInput()).to.eq('name');
        expect(await imageUpdatePage.getDescriptionInput()).to.eq('description');
        expect(await imageUpdatePage.getImageInput()).to.endsWith(fileNameToUpload);
        await imageUpdatePage.save();
        expect(await imageUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await imageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Image', async () => {
        const nbButtonsBeforeDelete = await imageComponentsPage.countDeleteButtons();
        await imageComponentsPage.clickOnLastDeleteButton();

        imageDeleteDialog = new ImageDeleteDialog();
        expect(await imageDeleteDialog.getDialogTitle()).to.eq('newLocalApp.image.delete.question');
        await imageDeleteDialog.clickOnConfirmButton();

        expect(await imageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
