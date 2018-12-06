import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IHolding } from 'app/shared/model/holding.model';

@Component({
    selector: 'jhi-holding-detail',
    templateUrl: './holding-detail.component.html'
})
export class HoldingDetailComponent implements OnInit {
    holding: IHolding;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ holding }) => {
            this.holding = holding;
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
