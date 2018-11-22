import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IStock } from '../shared/model/stock.model';
import { Principal } from '../core';

import { ITEMS_PER_PAGE } from '../shared';
import { StockService } from '../entities/stock/stock.service';

import { CategoryService } from '../entities/category/category.service';
import { ICategory } from '../shared/model/category.model';

@Component({
    selector: 'jhi-stock',
    templateUrl: './stockManagement.component.html'
})
export class StockManagementComponent implements OnInit, OnDestroy {
    currentAccount: any;
    stocks: IStock[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    count: number;
    today: any;
    optionCategory: number;
    categories: ICategory[];
    bAllcheckbox: boolean;
    list: any[];

    constructor(
        private stockService: StockService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager,
        private categoryService: CategoryService
    ) {
        this.stocks = null;
        this.count = 1;
        this.optionCategory = -1;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.stockService
                .search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IStock[]>) => this.paginateStocks(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.stockService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IStock[]>) => this.paginateStocks(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/stockManagement'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            '/stock',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            '/stockManagement',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.loadCategory();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInStocks();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IStock) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInStocks() {
        this.eventSubscriber = this.eventManager.subscribe('stockListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateStocks(data: IStock[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.stocks = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onClickMe(i: number) {
        this.count = i;
    }

    checkDate(d: any) {
        this.today = Date.now();
        const d1 = new Date(this.today);
        const d2 = new Date(d);
        if (d1.getTime() < d2.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    onglet1(b: any, d: any, o: number) {
        if (b && this.count === 1 && this.checkDate(d) && this.checkoption(o)) {
            return true;
        } else {
            return false;
        }
    }

    onglet2(b: any, o: number) {
        if (!b && this.count === 2 && this.checkoption(o)) {
            return true;
        } else {
            return false;
        }
    }

    onglet3(b: any, d: any, o: number) {
        if (b && this.count === 3 && !this.checkDate(d) && this.checkoption(o)) {
            return true;
        } else {
            return false;
        }
    }

    option(x: number) {
        this.optionCategory = x;
    }

    checkoption(o: number) {
        if (o === this.optionCategory || this.optionCategory === -1) {
            return true;
        } else {
            return false;
        }
    }

    loadCategory() {
        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    // Test
    checkboxcopie(bval: boolean) {
        const a: any[] = [];
        for (const j of this.stocks) {
            a.push({ id: j.id, bol: bval });
        }
        this.list = a;
    }

    checkboxcheck(id: any) {
        let i = 0;
        while (i < this.list.length && id !== this.list[i].id) {
            i++;
        }
        if (i !== this.list.length) {
            this.list[i].bol = !this.list[i].bol;
        }
    }

    checkboxallcheck(bval: boolean) {
        for (const j of this.list) {
            j.bol = bval;
        }
    }

    checkboxonClickMe() {
        if (this.bAllcheckbox) {
            this.bAllcheckbox = false;
            this.checkboxallcheck(false);
        } else {
            this.bAllcheckbox = true;
            this.checkboxallcheck(true);
        }
    }

    checkboxsuppr() {
        const updatedArray = [];
        let i = 0;
        for (const j of this.list) {
            if (j.bol === true) {
                this.stocks.splice(i, 1);
                i--;
            } else {
                updatedArray.push(j);
            }
            i++;
        }
        this.list = updatedArray;
    }
}
