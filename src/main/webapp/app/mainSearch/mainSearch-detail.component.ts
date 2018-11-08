import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IStock } from 'app/shared/model/stock.model';

@Component({
    selector: 'jhi-stock-detail',
    templateUrl: './mainSearch-detail.component.html'
})
export class MainSearchDetailComponent implements OnInit {
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
