import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IPurchase } from '../shared/model/purchase.model';
import { Principal } from '../core';
import { PurchaseService } from '../entities/purchase/purchase.service';

@Component({
    selector: 'jhi-purchase',
    templateUrl: './shopping.component.html'
})
export class ShoppingComponent implements OnInit, OnDestroy {
    purchases: IPurchase[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    total: number;
    btimeout: boolean;
    fintimeout: boolean;

    constructor(
        private purchaseService: PurchaseService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
        this.total = 0;
        this.purchases = [];
        this.btimeout = false;
        this.fintimeout = true;
    }

    loadAll() {
        this.purchaseService.query().subscribe(
            (res: HttpResponse<IPurchase[]>) => {
                this.purchases = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInPurchases();
        this.calculTotal();
        this.timeout();
    }

    timeout() {
        setTimeout(() => {
            if (this.fintimeout) {
                for (const k of this.purchases) {
                    this.confirmDelete(k.id);
                }
                this.btimeout = true;
                this.purchases = [];
            }
        }, 10000);
    }

    endTimeout() {
        this.fintimeout = false;
    }

    calculTotal() {
        for (const k of this.purchases) {
            this.total = this.total + k.quantity * k.stock.priceUnit;
        }
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

    confirmDelete(id: number) {
        this.purchaseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'purchaseListModification',
                content: 'Deleted an purchase'
            });
        });
    }
}
