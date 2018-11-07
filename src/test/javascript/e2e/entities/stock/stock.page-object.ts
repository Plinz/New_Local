import { element, by, ElementFinder } from 'protractor';

export class StockComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-stock div table .btn-danger'));
    title = element.all(by.css('jhi-stock div h2#page-heading span')).first();

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

export class StockUpdatePage {
    pageTitle = element(by.id('jhi-stock-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    quantityInitInput = element(by.id('field_quantityInit'));
    quantityRemainingInput = element(by.id('field_quantityRemaining'));
    priceUnitInput = element(by.id('field_priceUnit'));
    imageInput = element(by.id('file_image'));
    onSaleDateInput = element(by.id('field_onSaleDate'));
    expiryDateInput = element(by.id('field_expiryDate'));
    bioInput = element(by.id('field_bio'));
    availableInput = element(by.id('field_available'));
    productTypeSelect = element(by.id('field_productType'));
    personSelect = element(by.id('field_person'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setQuantityInitInput(quantityInit) {
        await this.quantityInitInput.sendKeys(quantityInit);
    }

    async getQuantityInitInput() {
        return this.quantityInitInput.getAttribute('value');
    }

    async setQuantityRemainingInput(quantityRemaining) {
        await this.quantityRemainingInput.sendKeys(quantityRemaining);
    }

    async getQuantityRemainingInput() {
        return this.quantityRemainingInput.getAttribute('value');
    }

    async setPriceUnitInput(priceUnit) {
        await this.priceUnitInput.sendKeys(priceUnit);
    }

    async getPriceUnitInput() {
        return this.priceUnitInput.getAttribute('value');
    }

    async setImageInput(image) {
        await this.imageInput.sendKeys(image);
    }

    async getImageInput() {
        return this.imageInput.getAttribute('value');
    }

    async setOnSaleDateInput(onSaleDate) {
        await this.onSaleDateInput.sendKeys(onSaleDate);
    }

    async getOnSaleDateInput() {
        return this.onSaleDateInput.getAttribute('value');
    }

    async setExpiryDateInput(expiryDate) {
        await this.expiryDateInput.sendKeys(expiryDate);
    }

    async getExpiryDateInput() {
        return this.expiryDateInput.getAttribute('value');
    }

    getBioInput() {
        return this.bioInput;
    }
    getAvailableInput() {
        return this.availableInput;
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

export class StockDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-stock-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-stock'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
