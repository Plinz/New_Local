import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { JhiEventManager } from 'ng-jhipster';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IStock } from '../shared/model/stock.model';
import { StockService } from 'app/entities/stock';
import { IImage, Image } from 'app/shared/model/image.model';
import { ImageService } from '../entities/image/image.service';
import { LoginModalService, Principal, Account } from 'app/core';
import { HttpParams } from '@angular/common/http';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    stockBio: IStock;
    stockNewProduit: IStock;
    stockBestPurchase: IStock;
    stockGrade: IStock;
    image: IImage[];

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private stockService: StockService,
        private jhiAlertService: JhiAlertService,
        private imageService: ImageService
    ) {
        this.stockBio = { name: 'coucou' };
        this.stockNewProduit = { name: 'coucou' };
        this.stockBestPurchase = { name: 'coucou' };
        this.stockGrade = { name: 'coucou' };
    }

    ngOnInit() {
        let params = new HttpParams();
        params = params.set('name.contains', 'carousel');
        this.imageService.filterquery(params).subscribe(
            (res: HttpResponse<IImage[]>) => {
                this.image = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );

        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.stockService.findProduitBio().subscribe(
            (res: HttpResponse<IStock>) => {
                this.stockBio = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.stockService.findNewProduit().subscribe(
            (res: HttpResponse<IStock>) => {
                this.stockNewProduit = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.stockService.findBestPurchase().subscribe(
            (res: HttpResponse<IStock>) => {
                this.stockBestPurchase = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.stockService.findGrade().subscribe(
            (res: HttpResponse<IStock>) => {
                this.stockGrade = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}
