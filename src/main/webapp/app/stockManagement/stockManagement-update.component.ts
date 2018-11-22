import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from '../shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IStock } from '../shared/model/stock.model';
import { StockService } from '../entities/stock/stock.service';
import { IProductType } from '../shared/model/product-type.model';
import { ProductTypeService } from '../entities/product-type';
import { IHolding } from '../shared/model/holding.model';
import { HoldingService } from '../entities/holding';
import { IUser, UserService } from '../core';

@Component({
    selector: 'jhi-stock-update',
    templateUrl: './stockManagement-update.component.html'
})
export class StockManagementUpdateComponent implements OnInit {
    stock: IStock;
    isSaving: boolean;

    productTypes: IProductType[];

    holdings: IHolding[];

    users: IUser[];
    // onSaleDate: string;
    expiryDate: string;
    currentDate: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private stockService: StockService,
        private productTypeService: ProductTypeService,
        private holdingService: HoldingService,
        // private userService: UserService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ stock }) => {
            this.stock = stock;
            // this.onSaleDate = this.stock.onSaleDate != null ? this.stock.onSaleDate.format(DATE_TIME_FORMAT) : null;
            this.expiryDate = this.stock.expiryDate != null ? this.stock.expiryDate.format(DATE_TIME_FORMAT) : null;
        });
        this.productTypeService.query().subscribe(
            (res: HttpResponse<IProductType[]>) => {
                this.productTypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        /*this.holdingService.findByCurrentUser().subscribe(
            (res: HttpResponse<IHolding[]>) => {
                this.holdings = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );*/
        /*this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );*/
        const myDate = new Date();
        if (myDate.getDate() < 10) {
            this.currentDate = `${myDate.getFullYear()}-${myDate.getMonth() + 1}-0${myDate.getDate()}`;
        } else {
            this.currentDate = `${myDate.getFullYear()}-${myDate.getMonth() + 1}-${myDate.getDate()}`;
        }
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.stock, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        // this.stock.onSaleDate = this.onSaleDate != null ? moment(this.onSaleDate, DATE_TIME_FORMAT) : null;
        this.stock.onSaleDate = moment(new Date(), DATE_TIME_FORMAT);
        this.stock.expiryDate = this.expiryDate != null ? moment(this.expiryDate, DATE_TIME_FORMAT) : null;
        this.stock.quantityRemaining = this.stock.quantityInit;
        if (this.stock.id !== undefined) {
            this.subscribeToSaveResponse(this.stockService.update(this.stock));
        } else {
            this.subscribeToSaveResponse(this.stockService.create(this.stock));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IStock>>) {
        result.subscribe((res: HttpResponse<IStock>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackProductTypeById(index: number, item: IProductType) {
        return item.id;
    }

    trackHoldingById(index: number, item: IHolding) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
