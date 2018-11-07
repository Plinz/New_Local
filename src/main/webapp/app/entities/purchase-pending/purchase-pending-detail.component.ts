import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchasePending } from 'app/shared/model/purchase-pending.model';

@Component({
    selector: 'jhi-purchase-pending-detail',
    templateUrl: './purchase-pending-detail.component.html'
})
export class PurchasePendingDetailComponent implements OnInit {
    purchasePending: IPurchasePending;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchasePending }) => {
            this.purchasePending = purchasePending;
        });
    }

    previousState() {
        window.history.back();
    }
}
