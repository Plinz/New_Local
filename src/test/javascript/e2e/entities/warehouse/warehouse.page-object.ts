import { element, by, ElementFinder } from 'protractor';

export class WarehouseComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-warehouse div table .btn-danger'));
    title = element.all(by.css('jhi-warehouse div h2#page-heading span')).first();

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

export class WarehouseUpdatePage {
    pageTitle = element(by.id('jhi-warehouse-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    descriptionInput = element(by.id('field_description'));
    telInput = element(by.id('field_tel'));
    imageSelect = element(by.id('field_image'));
    locationSelect = element(by.id('field_location'));

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

    async setTelInput(tel) {
        await this.telInput.sendKeys(tel);
    }

    async getTelInput() {
        return this.telInput.getAttribute('value');
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

    async locationSelectLastOption() {
        await this.locationSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async locationSelectOption(option) {
        await this.locationSelect.sendKeys(option);
    }

    getLocationSelect(): ElementFinder {
        return this.locationSelect;
    }

    async getLocationSelectedOption() {
        return this.locationSelect.element(by.css('option:checked')).getText();
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

export class WarehouseDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-warehouse-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-warehouse'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
