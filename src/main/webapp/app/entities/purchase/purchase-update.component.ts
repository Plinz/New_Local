import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IPurchase } from 'app/shared/model/purchase.model';
import { PurchaseService } from './purchase.service';
import { IStock } from 'app/shared/model/stock.model';
import { StockService } from 'app/entities/stock';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-purchase-update',
    templateUrl: './purchase-update.component.html'
})
export class PurchaseUpdateComponent implements OnInit {
    purchase: IPurchase;
    isSaving: boolean;

    stocks: IStock[];

    users: IUser[];
    saleDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private purchaseService: PurchaseService,
        private stockService: StockService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ purchase }) => {
            this.purchase = purchase;
            this.saleDate = this.purchase.saleDate != null ? this.purchase.saleDate.format(DATE_TIME_FORMAT) : null;
        });
        this.stockService.query().subscribe(
            (res: HttpResponse<IStock[]>) => {
                this.stocks = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.purchase.saleDate = this.saleDate != null ? moment(this.saleDate, DATE_TIME_FORMAT) : null;
        if (this.purchase.id !== undefined) {
            this.subscribeToSaveResponse(this.purchaseService.update(this.purchase));
        } else {
            this.subscribeToSaveResponse(this.purchaseService.create(this.purchase));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPurchase>>) {
        result.subscribe((res: HttpResponse<IPurchase>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackStockById(index: number, item: IStock) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
