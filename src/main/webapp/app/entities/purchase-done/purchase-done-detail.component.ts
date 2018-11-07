import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseDone } from 'app/shared/model/purchase-done.model';

@Component({
    selector: 'jhi-purchase-done-detail',
    templateUrl: './purchase-done-detail.component.html'
})
export class PurchaseDoneDetailComponent implements OnInit {
    purchaseDone: IPurchaseDone;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchaseDone }) => {
            this.purchaseDone = purchaseDone;
        });
    }

    previousState() {
        window.history.back();
    }
}
