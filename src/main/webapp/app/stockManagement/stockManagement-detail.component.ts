import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IStock } from '../shared/model/stock.model';

@Component({
    selector: 'jhi-stock-detail',
    templateUrl: './stockManagement-detail.component.html'
})
export class StockManagementDetailComponent implements OnInit {
    stock: IStock;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ stock }) => {
            this.stock = stock;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
