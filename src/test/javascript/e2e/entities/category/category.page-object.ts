import { element, by, ElementFinder } from 'protractor';

export class CategoryComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-category div table .btn-danger'));
    title = element.all(by.css('jhi-category div h2#page-heading span')).first();

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

export class CategoryUpdatePage {
    pageTitle = element(by.id('jhi-category-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    descriptionInput = element(by.id('field_description'));
    categoyParentSelect = element(by.id('field_categoyParent'));
    imageSelect = element(by.id('field_image'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setDescriptionInput(description) {
        await this.descriptionInput.sendKeys(description);
    }

    async getDescriptionInput() {
        return this.descriptionInput.getAttribute('value');
    }

    async categoyParentSelectLastOption() {
        await this.categoyParentSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async categoyParentSelectOption(option) {
        await this.categoyParentSelect.sendKeys(option);
    }

    getCategoyParentSelect(): ElementFinder {
        return this.categoyParentSelect;
    }

    async getCategoyParentSelectedOption() {
        return this.categoyParentSelect.element(by.css('option:checked')).getText();
    }

    async imageSelectLastOption() {
        await this.imageSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async imageSelectOption(option) {
        await this.imageSelect.sendKeys(option);
    }

    getImageSelect(): ElementFinder {
        return this.imageSelect;
    }

    async getImageSelectedOption() {
        return this.imageSelect.element(by.css('option:checked')).getText();
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

export class CategoryDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-category-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-category'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
