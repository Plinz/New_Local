import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Principal } from '../core';
import { CartService } from '../entities/cart/cart.service';
import { Location } from '@angular/common';
import { ICart } from '../shared/model/cart.model';

@Component({
    selector: 'jhi-purchase',
    templateUrl: './shopping.component.html'
})
export class ShoppingComponent implements OnInit, OnDestroy {
    carts: ICart[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    total: number;
    btimeout: boolean;
    fintimeout: boolean;
    isOkpanier: boolean;

    constructor(
        private cartService: CartService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private location: Location
    ) {
        this.isOkpanier = true;
        this.total = 0;
        this.carts = [];
        this.btimeout = false;
        this.fintimeout = true;
    }

    loadAll() {
        // this.purchaseService.query().toPromise().then()subscribe()
        this.cartService.query().subscribe(
            (res: HttpResponse<ICart[]>) => {
                this.carts = res.body;
                this.currentSearch = '';
                this.calculTotal();
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
    }

    timeout() {
        setTimeout(() => {
            if (this.fintimeout) {
                /* for (const k of this.purchases) {
                    this.confirmDelete(k.id);
                }
                this.purchases = []; */
                this.btimeout = true;
            }
        }, 3000);
    }

    startTimeout() {
        this.fintimeout = true;
        this.timeout();
        this.isOkpanier = false;
    }

    endTimeout() {
        this.fintimeout = false;
        // suppr
        for (const k of this.carts) {
            this.confirmDelete(k.id);
        }
        this.carts = [];
    }

    abandonner() {
        this.fintimeout = false;
        this.isOkpanier = true;
    }

    returnCard() {
        this.isOkpanier = true;
        this.btimeout = false;
        this.fintimeout = false;
    }

    calculTotal() {
        this.total = 0;
        for (const k of this.carts) {
            this.total = k.quantity * k.stock.priceUnit + this.total;
        }
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICart) {
        return item.id;
    }

    registerChangeInPurchases() {
        this.eventSubscriber = this.eventManager.subscribe('cartListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    confirmDelete(id: number) {
        this.cartService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cartListModification',
                content: 'Deleted an cart'
            });
        });
    }

    backcliked() {
        this.location.back();
    }
}
