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
    imageSelect = element(by.id('field_image'));
    categoryParentSelect = element(by.id('field_categoryParent'));

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

    async categoryParentSelectLastOption() {
        await this.categoryParentSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async categoryParentSelectOption(option) {
        await this.categoryParentSelect.sendKeys(option);
    }

    getCategoryParentSelect(): ElementFinder {
        return this.categoryParentSelect;
    }

    async getCategoryParentSelectedOption() {
        return this.categoryParentSelect.element(by.css('option:checked')).getText();
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
