import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { Cart } from './../shared/model/cart.model';
import { ICart } from '../shared/model/cart.model';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Principal, LoginModalService } from '../core';
import { UserService } from '../core/user/user.service';
import { IUser } from '../core/user/user.model';
import { CartService } from '../entities/cart/cart.service';
import { NavbarService } from '../layouts/navbar/navbar.service';

import { IStock } from 'app/shared/model/stock.model';

@Component({
    selector: 'jhi-stock-detail',
    templateUrl: './mainSearch-detail.component.html'
})
export class MainSearchDetailComponent implements OnInit {
    stock: IStock;
    qtbuy: number;

    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private userService: UserService,
        private cartService: CartService,
        private navbarService: NavbarService,
        private jhiAlertService: JhiAlertService
    ) {
        this.qtbuy = 1;
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ stock }) => {
            this.stock = stock;
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
    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    buyStock(s: IStock) {
        if (this.principal.isAuthenticated()) {
            this.userService.findByClientIsCurrentUser().subscribe(
                (res: HttpResponse<IUser>) => {
                    this.createCart(res.body, s);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        } else {
            alert(" vous n'êtes pas connecté");
        }
    }

    createCart(client: IUser, s: IStock) {
        const c: Cart = new Cart();
        c.client = client;
        c.quantity = this.qtbuy;
        c.stock = s;

        this.cartService.createCartTrigger(c).subscribe(
            (res: HttpResponse<ICart>) => {
                this.navbarService.sendIncrement();
            },
            (res: HttpErrorResponse) => {
                alert('Desolé plus de produit disponible');
            }
        );
    }
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onChangeBuy(deviceValue: number) {
        this.qtbuy = deviceValue;
    }
}
