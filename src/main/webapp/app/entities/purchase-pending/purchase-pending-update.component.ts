import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IPurchasePending } from 'app/shared/model/purchase-pending.model';
import { PurchasePendingService } from './purchase-pending.service';
import { IStock } from 'app/shared/model/stock.model';
import { StockService } from 'app/entities/stock';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-purchase-pending-update',
    templateUrl: './purchase-pending-update.component.html'
})
export class PurchasePendingUpdateComponent implements OnInit {
    purchasePending: IPurchasePending;
    isSaving: boolean;

    stocks: IStock[];

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private purchasePendingService: PurchasePendingService,
        private stockService: StockService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ purchasePending }) => {
            this.purchasePending = purchasePending;
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
        if (this.purchasePending.id !== undefined) {
            this.subscribeToSaveResponse(this.purchasePendingService.update(this.purchasePending));
        } else {
            this.subscribeToSaveResponse(this.purchasePendingService.create(this.purchasePending));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPurchasePending>>) {
        result.subscribe((res: HttpResponse<IPurchasePending>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
