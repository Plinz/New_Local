import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHolding } from 'app/shared/model/holding.model';

@Component({
    selector: 'jhi-holding-detail',
    templateUrl: './holding-detail.component.html'
})
export class HoldingDetailComponent implements OnInit {
    holding: IHolding;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ holding }) => {
            this.holding = holding;
        });
    }

    previousState() {
        window.history.back();
    }
}
