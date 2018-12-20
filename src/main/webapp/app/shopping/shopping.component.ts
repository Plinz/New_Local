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
import * as moment from 'moment';
import { saveAs } from 'file-saver';
import { NavbarService } from '../layouts/navbar/navbar.service';
import { StockService } from 'app/entities/stock';

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
    btimeout: boolean;
    fintimeout: boolean;
    isOkpanier: boolean;
    isRecap: boolean;
    listBtM: any[];
    facture: any;
    factureName: string;

    constructor(
        private stockService: StockService,
        private dataUtils: JhiDataUtils,
        private purchaseService: PurchaseService,
        private cartService: CartService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private location: Location,
        private navbarService: NavbarService
    ) {
        this.isOkpanier = true;
        this.total = 0;
        this.carts = [];
        this.btimeout = false;
        this.fintimeout = true;
        this.isRecap = false;
        this.listBtM = [];
    }

    loadAll() {
        this.cartService.findByClientIsCurrentUser().subscribe(
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
        this.purchases = this.carts;
        this.confirmCreate();
        this.navbarService.clearCount();
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
            this.purchaseService.deleteSendMail(tmp).subscribe((res: any) => {
                this.facture = res.body;
                this.factureName = 'Facture_' + tmp + '.pdf';
            });
        }
        this.carts = [];
        this.isRecap = true;
    }

    download() {
        console.log(this.facture);
        saveAs(this.facture, this.factureName);
    }

    confirmCreate() {
        const d2: Moment = moment();
        this.confirmDelete();
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

    modifier(i: string) {
        const j: number = +i;

        this.stockService.getRemaning(this.carts[j].stock.id).subscribe(
            (res: HttpResponse<String>) => {
                const rem: number = +res.body;
                const a: number = +this.listBtM[j].b;

                if (rem - (a - this.carts[j].quantity) > 0) {
                    alert('ojjj');
                    this.carts[j].quantity = this.listBtM[j].b;
                    this.cartService
                        .update(this.carts[j])
                        .subscribe((rest: HttpResponse<ICart>) => this.onSaveSuccess(), (rest: HttpErrorResponse) => this.onSaveError(j));
                } else {
                    this.onSaveError(j);
                }
            },
            (res: HttpErrorResponse) => this.onSaveError(j)
        );
    }

    onSaveSuccess() {
        this.calculTotal();
    }

    onSaveError(i: number) {
        this.listBtM[i].b = this.carts[i].quantity;
        this.listBtM[i].err = true;
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
}
