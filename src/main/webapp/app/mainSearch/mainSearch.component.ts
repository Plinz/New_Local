import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { IStock } from 'app/shared/model/stock.model';
import { Principal, LoginModalService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { StockService } from '../entities/stock/stock.service';

import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';

import { ILocation, Location } from 'app/shared/model/location.model';

import { MainService } from '../layouts/main/main.service';

import { LocationService } from 'app/entities/location';

@Component({
    selector: 'jhi-stock',
    templateUrl: './mainSearch.component.html'
})
export class MainSearchComponent implements OnInit, OnDestroy {
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
    selected: number;
    cat: string;
    modalRef: NgbModalRef;
    categories: ICategory[];
    optioncat: string;

    constructor(
        private stockService: StockService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager,
        private categoryService: CategoryService,
        private mainService: MainService,
        private locationService: LocationService,
        private loginModalService: LoginModalService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.cat = null;
        this.optioncat = 'Catégorie';
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });

        this.activatedRoute.queryParams.subscribe(params => {
            this.currentSearch = params['search'];
        });

        this.activatedRoute.queryParams.subscribe(params => {
            this.cat = params['cat'];
            if (this.cat != null) {
                this.loadStockCat(this.cat);
                this.optioncat = this.cat;
            }
        });
    }

    deg2rad(deg: number) {
        return deg * Math.PI / 180.0;
    }
    rad2deg(rad: number) {
        return rad * 180 / Math.PI;
    }

    hasLocation() {
        return this.mainService.getLocation() != null;
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    isLocationFromBase() {
        return this.mainService.isLocationFromBase();
    }

    setLocationForUser() {
        if (this.principal.isAuthenticated()) {
            this.principal.identity(true).then(account => {
                console.log('Account:' + account);
                const location = this.mainService.getLocation();
                location.userId = account.id;
                this.locationService.create(location);
                this.mainService.setLocation(location, true);
            });
        }
    }

    askLocation() {
        if (!this.hasLocation()) {
            if (this.principal.isAuthenticated()) {
                this.locationService.findByCurrentUser().subscribe(
                    (res: HttpResponse<ILocation>) => {
                        if (res.body != null) {
                            this.mainService.setLocation(res.body, true);
                        } else {
                            if (window.navigator.geolocation) {
                                window.navigator.geolocation.getCurrentPosition(
                                    position => {
                                        const location = new Location();
                                        location.lat = position.coords.latitude;
                                        location.lon = position.coords.longitude;
                                        this.mainService.setLocation(location, false);
                                    },
                                    error => {}
                                );
                            } else {
                                this.onError('Geolocation not supported in this browser');
                            }
                        }
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            } else {
                if (window.navigator.geolocation) {
                    window.navigator.geolocation.getCurrentPosition(
                        position => {
                            const location = new Location();
                            location.lat = position.coords.latitude;
                            location.lon = position.coords.longitude;
                            this.mainService.setLocation(location, false);
                            console.log('NOT Authenticated and location:' + location);
                        },
                        error => {}
                    );
                } else {
                    this.onError('Geolocation not supported in this browser');
                }
            }
        }
        return this.mainService.getLocation() != null;
    }

    distance(stock: IStock) {
        const loc = this.mainService.getLocation();
        let dist = -1;
        if (loc != null) {
            const lon1 = stock.warehouse.location.lon;
            const lat1 = stock.warehouse.location.lat;
            const lon2 = loc.lon;
            const lat2 = loc.lat;
            const theta = lon1 - lon2;
            dist =
                Math.sin(this.deg2rad(lat1)) * Math.sin(this.deg2rad(lat2)) +
                Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) * Math.cos(this.deg2rad(theta));
            dist = Math.acos(dist);
            dist = this.rad2deg(dist);
            dist = dist * 60 * 1.1515 * 1.609344;
        }
        return parseInt(dist + '', 0);
    }

    distanceSuccess(stock: IStock) {
        const dist: number = this.distance(stock);
        return dist >= 0 && dist <= 15;
    }

    distanceWarning(stock: IStock) {
        const dist: number = this.distance(stock);
        return dist > 15 && dist <= 30;
    }

    distanceDanger(stock: IStock) {
        const dist: number = this.distance(stock);
        return dist > 30;
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

    loadStockCat(cat: string) {
        this.stockService.getStockCat(cat).subscribe(
            (res: HttpResponse<IStock[]>) => {
                this.stocks = res.body;
            },
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
        this.router.navigate(['/mainSearch'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        // this.loadAll();
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
        // this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            '/stock',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        // this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInStocks();
        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.eventManager.subscribe('authenticationSuccess', msg => {
            this.locationService.findByCurrentUser().subscribe(
                (res: HttpResponse<ILocation>) => {
                    if (res.body != null) {
                        this.mainService.setLocation(res.body, true);
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        });
        this.eventManager.subscribe('logoutSuccess', msg => {
            this.mainService.setLocation(null, null);
        });
        if (this.mainService.getLocation() == null && this.principal.isAuthenticated()) {
            this.locationService.findByCurrentUser().subscribe(
                (res: HttpResponse<ILocation>) => {
                    if (res.body != null) {
                        this.mainService.setLocation(res.body, true);
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }

    ngOnDestroy() {
        // this.eventManager.destroy(this.eventSubscriber);
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
        // this.eventSubscriber = this.eventManager.subscribe('stockListModification', response => this.loadAll());
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
        console.log(data);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }

    option(str: string) {
        this.loadStockCat(str);
    }
}
