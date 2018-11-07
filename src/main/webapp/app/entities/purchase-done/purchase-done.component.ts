import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IPurchaseDone } from 'app/shared/model/purchase-done.model';
import { Principal } from 'app/core';
import { PurchaseDoneService } from './purchase-done.service';

@Component({
    selector: 'jhi-purchase-done',
    templateUrl: './purchase-done.component.html'
})
export class PurchaseDoneComponent implements OnInit, OnDestroy {
    purchaseDones: IPurchaseDone[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private purchaseDoneService: PurchaseDoneService,
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
            this.purchaseDoneService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IPurchaseDone[]>) => (this.purchaseDones = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.purchaseDoneService.query().subscribe(
            (res: HttpResponse<IPurchaseDone[]>) => {
                this.purchaseDones = res.body;
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
        this.registerChangeInPurchaseDones();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPurchaseDone) {
        return item.id;
    }

    registerChangeInPurchaseDones() {
        this.eventSubscriber = this.eventManager.subscribe('purchaseDoneListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
