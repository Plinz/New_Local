import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IPurchase } from 'app/shared/model/purchase.model';
import { Principal } from 'app/core';
import { PurchaseService } from './purchase.service';

@Component({
    selector: 'jhi-purchase',
    templateUrl: './purchase.component.html'
})
export class PurchaseComponent implements OnInit, OnDestroy {
    purchases: IPurchase[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private purchaseService: PurchaseService,
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
            this.purchaseService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IPurchase[]>) => (this.purchases = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.purchaseService.query().subscribe(
            (res: HttpResponse<IPurchase[]>) => {
                this.purchases = res.body;
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
        this.registerChangeInPurchases();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPurchase) {
        return item.id;
    }

    registerChangeInPurchases() {
        this.eventSubscriber = this.eventManager.subscribe('purchaseListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
