import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IPurchasePending } from 'app/shared/model/purchase-pending.model';
import { Principal } from 'app/core';
import { PurchasePendingService } from './purchase-pending.service';

@Component({
    selector: 'jhi-purchase-pending',
    templateUrl: './purchase-pending.component.html'
})
export class PurchasePendingComponent implements OnInit, OnDestroy {
    purchasePendings: IPurchasePending[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private purchasePendingService: PurchasePendingService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.purchasePendingService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IPurchasePending[]>) => (this.purchasePendings = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.purchasePendingService.query().subscribe(
            (res: HttpResponse<IPurchasePending[]>) => {
                this.purchasePendings = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInPurchasePendings();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPurchasePending) {
        return item.id;
    }

    registerChangeInPurchasePendings() {
        this.eventSubscriber = this.eventManager.subscribe('purchasePendingListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
