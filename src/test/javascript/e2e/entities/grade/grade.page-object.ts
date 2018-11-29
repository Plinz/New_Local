import { element, by, ElementFinder } from 'protractor';

export class GradeComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-grade div table .btn-danger'));
    title = element.all(by.css('jhi-grade div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class GradeUpdatePage {
    pageTitle = element(by.id('jhi-grade-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    gradeInput = element(by.id('field_grade'));
    nbVoterInput = element(by.id('field_nbVoter'));
    sellerSelect = element(by.id('field_seller'));
    productTypeSelect = element(by.id('field_productType'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setGradeInput(grade) {
        await this.gradeInput.sendKeys(grade);
    }

    async getGradeInput() {
        return this.gradeInput.getAttribute('value');
    }

    async setNbVoterInput(nbVoter) {
        await this.nbVoterInput.sendKeys(nbVoter);
    }

    async getNbVoterInput() {
        return this.nbVoterInput.getAttribute('value');
    }

    async sellerSelectLastOption() {
        await this.sellerSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async sellerSelectOption(option) {
        await this.sellerSelect.sendKeys(option);
    }

    getSellerSelect(): ElementFinder {
        return this.sellerSelect;
    }

    async getSellerSelectedOption() {
        return this.sellerSelect.element(by.css('option:checked')).getText();
    }

    async productTypeSelectLastOption() {
        await this.productTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async productTypeSelectOption(option) {
        await this.productTypeSelect.sendKeys(option);
    }

    getProductTypeSelect(): ElementFinder {
        return this.productTypeSelect;
    }

    async getProductTypeSelectedOption() {
        return this.productTypeSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class GradeDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-grade-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-grade'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
