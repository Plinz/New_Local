import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IHolding } from '../shared/model/holding.model';

@Component({
    selector: 'jhi-holding-detail',
    templateUrl: './holdingProfil-detail.component.html'
})
export class HoldingProfilDetailComponent implements OnInit {
    holding: IHolding;

    constructor(private activatedRoute: ActivatedRoute, private dataUtils: JhiDataUtils) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ holding }) => {
            this.holding = holding;
        });
    }

    previousState() {
        window.history.back();
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
}
