/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { GradeComponentsPage, GradeDeleteDialog, GradeUpdatePage } from './grade.page-object';

const expect = chai.expect;

describe('Grade e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let gradeUpdatePage: GradeUpdatePage;
    let gradeComponentsPage: GradeComponentsPage;
    /*let gradeDeleteDialog: GradeDeleteDialog;*/

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Grades', async () => {
        await navBarPage.goToEntity('grade');
        gradeComponentsPage = new GradeComponentsPage();
        expect(await gradeComponentsPage.getTitle()).to.eq('newLocalApp.grade.home.title');
    });

    it('should load create Grade page', async () => {
        await gradeComponentsPage.clickOnCreateButton();
        gradeUpdatePage = new GradeUpdatePage();
        expect(await gradeUpdatePage.getPageTitle()).to.eq('newLocalApp.grade.home.createOrEditLabel');
        await gradeUpdatePage.cancel();
    });

    /* it('should create and save Grades', async () => {
        const nbButtonsBeforeCreate = await gradeComponentsPage.countDeleteButtons();

        await gradeComponentsPage.clickOnCreateButton();
        await promise.all([
            gradeUpdatePage.setGradeInput('5'),
            gradeUpdatePage.setNbVoterInput('5'),
            gradeUpdatePage.sellerSelectLastOption(),
            gradeUpdatePage.productTypeSelectLastOption(),
        ]);
        expect(await gradeUpdatePage.getGradeInput()).to.eq('5');
        expect(await gradeUpdatePage.getNbVoterInput()).to.eq('5');
        await gradeUpdatePage.save();
        expect(await gradeUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await gradeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });*/

    /* it('should delete last Grade', async () => {
        const nbButtonsBeforeDelete = await gradeComponentsPage.countDeleteButtons();
        await gradeComponentsPage.clickOnLastDeleteButton();

        gradeDeleteDialog = new GradeDeleteDialog();
        expect(await gradeDeleteDialog.getDialogTitle())
            .to.eq('newLocalApp.grade.delete.question');
        await gradeDeleteDialog.clickOnConfirmButton();

        expect(await gradeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
