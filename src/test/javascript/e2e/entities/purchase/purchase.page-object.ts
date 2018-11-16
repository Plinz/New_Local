import { element, by, ElementFinder } from 'protractor';

export class PurchaseComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-purchase div table .btn-danger'));
    title = element.all(by.css('jhi-purchase div h2#page-heading span')).first();

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

export class PurchaseUpdatePage {
    pageTitle = element(by.id('jhi-purchase-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    saleDateInput = element(by.id('field_saleDate'));
    quantityInput = element(by.id('field_quantity'));
    stockSelect = element(by.id('field_stock'));
    clientSelect = element(by.id('field_client'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setSaleDateInput(saleDate) {
        await this.saleDateInput.sendKeys(saleDate);
    }

    async getSaleDateInput() {
        return this.saleDateInput.getAttribute('value');
    }

    async setQuantityInput(quantity) {
        await this.quantityInput.sendKeys(quantity);
    }

    async getQuantityInput() {
        return this.quantityInput.getAttribute('value');
    }

    async stockSelectLastOption() {
        await this.stockSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async stockSelectOption(option) {
        await this.stockSelect.sendKeys(option);
    }

    getStockSelect(): ElementFinder {
        return this.stockSelect;
    }

    async getStockSelectedOption() {
        return this.stockSelect.element(by.css('option:checked')).getText();
    }

    async clientSelectLastOption() {
        await this.clientSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async clientSelectOption(option) {
        await this.clientSelect.sendKeys(option);
    }

    getClientSelect(): ElementFinder {
        return this.clientSelect;
    }

    async getClientSelectedOption() {
        return this.clientSelect.element(by.css('option:checked')).getText();
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

export class PurchaseDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-purchase-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-purchase'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
