import { element, by, ElementFinder } from 'protractor';

export class PurchasePendingComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-purchase-pending div table .btn-danger'));
    title = element.all(by.css('jhi-purchase-pending div h2#page-heading span')).first();

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

export class PurchasePendingUpdatePage {
    pageTitle = element(by.id('jhi-purchase-pending-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    quantityInput = element(by.id('field_quantity'));
    stockSelect = element(by.id('field_stock'));
    personSelect = element(by.id('field_person'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
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

    async personSelectLastOption() {
        await this.personSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async personSelectOption(option) {
        await this.personSelect.sendKeys(option);
    }

    getPersonSelect(): ElementFinder {
        return this.personSelect;
    }

    async getPersonSelectedOption() {
        return this.personSelect.element(by.css('option:checked')).getText();
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

export class PurchasePendingDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-purchasePending-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-purchasePending'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
