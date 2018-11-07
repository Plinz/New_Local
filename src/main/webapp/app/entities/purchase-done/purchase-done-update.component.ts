import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IPurchaseDone } from 'app/shared/model/purchase-done.model';
import { PurchaseDoneService } from './purchase-done.service';
import { IStock } from 'app/shared/model/stock.model';
import { StockService } from 'app/entities/stock';
import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from 'app/entities/person';

@Component({
    selector: 'jhi-purchase-done-update',
    templateUrl: './purchase-done-update.component.html'
})
export class PurchaseDoneUpdateComponent implements OnInit {
    purchaseDone: IPurchaseDone;
    isSaving: boolean;

    stocks: IStock[];

    people: IPerson[];
    saleDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private purchaseDoneService: PurchaseDoneService,
        private stockService: StockService,
        private personService: PersonService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ purchaseDone }) => {
            this.purchaseDone = purchaseDone;
            this.saleDate = this.purchaseDone.saleDate != null ? this.purchaseDone.saleDate.format(DATE_TIME_FORMAT) : null;
        });
        this.stockService.query().subscribe(
            (res: HttpResponse<IStock[]>) => {
                this.stocks = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.personService.query().subscribe(
            (res: HttpResponse<IPerson[]>) => {
                this.people = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.purchaseDone.saleDate = this.saleDate != null ? moment(this.saleDate, DATE_TIME_FORMAT) : null;
        if (this.purchaseDone.id !== undefined) {
            this.subscribeToSaveResponse(this.purchaseDoneService.update(this.purchaseDone));
        } else {
            this.subscribeToSaveResponse(this.purchaseDoneService.create(this.purchaseDone));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseDone>>) {
        result.subscribe((res: HttpResponse<IPurchaseDone>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPersonById(index: number, item: IPerson) {
        return item.id;
    }
}
