import { element, by, ElementFinder } from 'protractor';

export class HoldingComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-holding div table .btn-danger'));
    title = element.all(by.css('jhi-holding div h2#page-heading span')).first();

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

export class HoldingUpdatePage {
    pageTitle = element(by.id('jhi-holding-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    siretInput = element(by.id('field_siret'));
    nameInput = element(by.id('field_name'));
    descriptionInput = element(by.id('field_description'));
    imageSelect = element(by.id('field_image'));
    locationSelect = element(by.id('field_location'));
    ownerSelect = element(by.id('field_owner'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setSiretInput(siret) {
        await this.siretInput.sendKeys(siret);
    }

    async getSiretInput() {
        return this.siretInput.getAttribute('value');
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

    async ownerSelectLastOption() {
        await this.ownerSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async ownerSelectOption(option) {
        await this.ownerSelect.sendKeys(option);
    }

    getOwnerSelect(): ElementFinder {
        return this.ownerSelect;
    }

    async getOwnerSelectedOption() {
        return this.ownerSelect.element(by.css('option:checked')).getText();
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

export class HoldingDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-holding-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-holding'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
