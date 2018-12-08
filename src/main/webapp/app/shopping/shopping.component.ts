import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { Principal } from '../core';
import { CartService } from '../entities/cart/cart.service';
import { Location } from '@angular/common';
import { ICart } from '../shared/model/cart.model';
import { IPurchase } from '../shared/model/purchase.model';
import { PurchaseService } from '../entities/purchase/purchase.service';
import { Moment } from 'moment';
import moment = require('moment');

@Component({
    selector: 'jhi-purchase',
    templateUrl: './shopping.component.html'
})
export class ShoppingComponent implements OnInit, OnDestroy {
    purchases: IPurchase[];
    carts: ICart[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    total: number;
    totalRecap: number;
    btimeout: boolean;
    fintimeout: boolean;
    isOkpanier: boolean;
    isRecap: boolean;
    listBtM: any[];

    constructor(
        private dataUtils: JhiDataUtils,
        private purchaseService: PurchaseService,
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
        this.isRecap = false;
        this.totalRecap = 0;
        this.listBtM = [];
    }

    loadAll() {
        this.cartService.query().subscribe(
            (res: HttpResponse<ICart[]>) => {
                this.carts = res.body;
                this.currentSearch = '';
                this.calculTotal();
                this.copieListBtnModifier();
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
        // Créatation
        this.purchases = this.carts;
        this.confirmCreate();
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

    confirmDelete() {
        // Suppression et send mail
        if (this.purchases.length > 0) {
            const tmp = this.purchases[0].client.id;
            this.purchaseService.deleteSendMail(tmp).subscribe(response => alert('ok suppr'), () => alert('erreur suppr'));
        }
        this.carts = [];
        this.isRecap = true;

        /*this.cartService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cartListModification',
                content: 'Deleted an cart'
            });
        });*/
    }

    confirmCreate() {
        for (const k of this.carts) {
            const tmp: IPurchase = null;
            tmp.id = null;
            tmp.quantity = k.quantity;
            tmp.withdraw = false;
            tmp.client = k.client;
            tmp.saleDate = k.stock.expiryDate;
            tmp.stock = k.stock;

            this.purchaseService.create(tmp).subscribe(
                response => {
                    alert('ok create \n' + +'id:' + k.id + '\n' + 'q:' + k.quantity);
                    this.confirmDelete();
                },
                () => alert('erreur create')
            );
        }
    }

    backcliked() {
        this.location.back();
    }

    nada(e) {
        e.preventDefault();
    }

    copieListBtnModifier() {
        const a: any[] = [];
        for (const j of this.carts) {
            a.push({ id: j.id, b: j.quantity, err: false });
        }
        this.listBtM = a;
    }

    btncheck(qcart: number, qlist: number) {
        return qcart === qlist;
    }

    modifier(i: number) {
        const tmp: number = this.carts[i].quantity;

        this.carts[i].quantity = this.listBtM[i].b;
        this.cartService
            .update(this.carts[i])
            .subscribe((res: HttpResponse<ICart>) => this.onSaveSuccess(i), (res: HttpErrorResponse) => this.onSaveError(tmp));
    }

    onSaveSuccess(i: number) {
        this.calculTotal();
    }

    onSaveError(i: number) {
        this.carts[i].quantity = i;
        this.listBtM[i].err = true;
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
}
