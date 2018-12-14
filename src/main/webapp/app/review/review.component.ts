import { Component, OnInit, OnDestroy } from '@angular/core';
import { IPurchase } from '../shared/model/purchase.model';
import { PurchaseService } from '../entities/purchase/purchase.service';
import { IStock } from '../shared/model/stock.model';
import { StockService } from '../entities/stock/stock.service';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Moment } from 'moment';
// import moment = require('moment');
import * as moment from 'moment';

@Component({
    selector: 'jhi-review',
    templateUrl: './review.component.html',
    styles: []
})
export class ReviewComponent implements OnInit {
    purchases: IPurchase[];
    stocks: IStock[];
    currentSearch: string;
    // Graphique
    count: number;
    lineChartData: Array<any>;
    lineChartLabels: Array<any>;
    lineChartOptions: any;
    lineChartColors: Array<any>;
    lineChartLegend: boolean;
    lineChartType: string;
    mean: number;
    total: number;
    percentage: number;
    bOpt = false;

    constructor(private purchaseService: PurchaseService, private stockService: StockService, private jhiAlertService: JhiAlertService) {
        this.purchases = [];
        this.stocks = [];
        this.currentSearch = '';
        // Graphique
        this.count = 1;
        this.lineChartLegend = true;
        this.lineChartType = 'line';
        this.lineChartData = [];
        this.lineChartLabels = [];
        this.lineChartOptions = { responsive: true };

        this.lineChartColors = [
            {
                // dark grey
                backgroundColor: 'rgba(77,83,96,0.2)',
                borderColor: '#007bff',
                pointBackgroundColor: 'rgba(77,83,96,1)',
                pointBorderColor: '#fff',
                pointHoverBackgroundColor: '#fff',
                pointHoverBorderColor: 'rgba(77,83,96,1)'
            }
        ];
        // Calcul
        this.mean = 0;
        this.total = 0;
        this.percentage = 0;
        // other
        this.bOpt = false;
        this.lineChartLabels = ['Erreur', 'chargement', 'en', 'cours'];
        this.lineChartData = [{ data: [0, 0, 0, 0], label: 'Erreur' }];
        this.lineChartOptions = { responsive: true };
    }

    ngOnInit() {
        this.loadAll();
    }

    loadAll() {
        // Stock => bilan
        this.stockService.findBySellerIsCurrentUser().subscribe(
            (res: HttpResponse<IStock[]>) => {
                this.stocks = res.body;
                this.currentSearch = '';
                // Purchase => Synthèse
                this.runAsynStock();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadPurchase(nom: string, id: number) {
        this.purchaseService.getIdStock(id).subscribe(
            (res: HttpResponse<IStock[]>) => {
                this.purchases = res.body;
                this.currentSearch = '';
                this.updateGraph(nom, id);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    runAsynStock() {
        // if (this.stocks.length > 0) {
        //     this.loadPurchase(this.stocks[0].name, this.stocks[0].id);
        // }
        // Total
        let quantity = 0;
        let remaining = 0;
        for (const i of this.stocks) {
            this.total = this.total + (i.quantityInit - i.quantityRemaining) * i.priceUnit;
            quantity = quantity + i.quantityInit;
            remaining = remaining + i.quantityRemaining;
        }
        this.percentage = Math.floor(remaining / quantity * 100);
        // Average
        this.mean = this.total / this.stocks.length;
    }

    updateGraph(nom: string, id: number) {
        const dataP: number[] = [];
        let qtTotal = 0;

        this.lineChartLabels.length = 0;
        if (this.purchases.length > 0) {
            dataP.push(0);
            const deb: Moment = moment(this.purchases[0].stock.onSaleDate);
            this.lineChartLabels.push(deb.format('DD/MM/YYYY'));
        }

        for (const i of this.purchases) {
            qtTotal = qtTotal + i.quantity;
            dataP.push(qtTotal);
            this.lineChartLabels.push(i.saleDate.format('DD/MM/YYYY'));
        }
        // Fin
        dataP.push(qtTotal);
        const d: Moment = moment();
        this.lineChartLabels.push(d.format('DD/MM/YYYY'));
        // Update
        this.lineChartData = [{ data: dataP, label: nom + ':' + id }];
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onClickMe(i: number) {
        this.count = i;
        this.bOpt = false;
    }

    // events
    chartClicked(e: any): void {
        console.log(e);
    }

    chartHovered(e: any): void {
        console.log(e);
    }

    onChangeOpt(id: number) {
        this.bOpt = false;
        this.loadPurchase(this.stocks[id].name, this.stocks[id].id);
        this.bOpt = true;
    }
}
