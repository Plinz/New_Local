import { IUser } from '../core/user/user.model';
import { ICart } from '../shared/model/cart.model';
import { Cart } from './../shared/model/cart.model';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IStock } from '../shared/model/stock.model';
import { Principal, LoginModalService } from '../core';
import { ITEMS_PER_PAGE } from '../shared';
import { StockService } from '../entities/stock/stock.service';
import { ICategory } from '../shared/model/category.model';
import { CategoryService } from '../entities/category';
import { ILocation, Location } from '../shared/model/location.model';
import { MainService } from '../layouts/main/main.service';
import { LocationService } from '../entities/location';
import { NavbarService } from '../layouts/navbar/navbar.service';
import { CartService } from '../entities/cart/cart.service';
import { UserService } from '../core/user/user.service';
import { HttpParams } from '@angular/common/http';
import { IWarehouse } from '../shared/model/warehouse.model';
import { IHolding } from '../shared/model/holding.model';
import { IProductType } from '../shared/model/product-type.model';
import { ProductTypeService } from '../entities/product-type/product-type.service';
import { ViewEncapsulation } from '@angular/core';

import { MatSnackBar, MatSnackBarConfig, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from '@angular/material';

@Component({
    selector: 'jhi-stock',
    templateUrl: './mainSearch.component.html',
    encapsulation: ViewEncapsulation.None
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
    products: IProductType[];
    optioncat: string;
    qtbuy: number;
    bclik: boolean;
    prixMini: number;
    prixMax: number;
    seller: IUser[];
    warehouses: IWarehouse[];
    holdings: IHolding[];
    listCat: any[];
    listProd: any[];
    filterOptionWare: number;
    filterOptionHold: number;
    filterSearch: string;
    pageSize: number;
    bnon: boolean;
    message: string;
    actionButtonLabel: string;
    action: boolean;
    setAutoHide: boolean;
    autoHide: number;
    horizontalPosition: MatSnackBarHorizontalPosition;
    verticalPosition: MatSnackBarVerticalPosition;
    addExtraClass: boolean;
    prixMaxInit: number;

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
        private loginModalService: LoginModalService,
        private navbarService: NavbarService,
        private cartService: CartService,
        private userService: UserService,
        private productTypeService: ProductTypeService,
        public snackBar: MatSnackBar
    ) {
        this.pageSize = 20;
        this.prixMini = 1;
        this.prixMax = 9;
        this.prixMaxInit = 9;
        this.filterSearch = '';
        this.bclik = false;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.cat = null;
        this.currentSearch = null;
        this.qtbuy = 500;
        this.filterOptionHold = -1;
        this.filterOptionWare = -1;
        this.optioncat = 'Catégorie';
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });

        this.activatedRoute.queryParams.subscribe(params => {
            this.currentSearch = params['search'];
            if (this.currentSearch != null) {
                this.loadAll();
            }
        });

        this.activatedRoute.queryParams.subscribe(params => {
            this.cat = params['cat'];
            if (this.cat != null) {
                this.loadStockCat(this.cat);
                this.optioncat = this.cat;
            }
        });

        if (this.currentSearch === null && this.cat != null) {
            this.loadStockCat('');
        }
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
                const location = this.mainService.getLocation();
                location.userId = account.id;
                console.log('ID=' + account.id);
                this.locationService.create(location).subscribe();
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
            let params = new HttpParams();
            params = params.set('page', `${this.page - 1}`).set('size', `${this.pageSize}`);
            params = params.set('quantityRemaining.greaterThan', '0');
            params = params.set('available.equals', 'true');
            params = params.set('query', this.currentSearch);

            this.stockService
                .filtersearch(params)
                .subscribe(
                    (res: HttpResponse<IStock[]>) => this.paginateStocks(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );

            // this.stockService
            //     .search({
            //         page: this.page - 1,
            //         query: this.currentSearch,
            //         size: this.itemsPerPage,
            //         sort: this.sort()
            //     })
            //     .subscribe(
            //         (res: HttpResponse<IStock[]>) => this.paginateStocks(res.body, res.headers),
            //         (res: HttpErrorResponse) => this.onError(res.message)
            //     );
            return;
        }
        this.loadStockCat('');
        // this.stockService
        //     .query({
        //         page: this.page - 1,
        //         size: this.itemsPerPage,
        //         sort: this.sort()
        //     })
        //     .subscribe(
        //         (res: HttpResponse<IStock[]>) => this.paginateStocks(res.body, res.headers),
        //         (res: HttpErrorResponse) => this.onError(res.message)
        //     );
    }

    loadStockCat(cat: string) {
        // this.stockService.getStockCat(cat).subscribe(
        //     (res: HttpResponse<IStock[]>) => {
        //         this.stocks = res.body;
        //     },
        //     (res: HttpErrorResponse) => this.onError(res.message)
        // );
        let params = new HttpParams();
        params = params.set('page', `${this.page - 1}`).set('size', `${this.pageSize}`);
        params = params.set('quantityRemaining.greaterThan', '0');
        params = params.set('available.equals', 'true');
        params = params.set('categoryName.contains', cat);

        this.stockService.filter(params).subscribe(
            (res: HttpResponse<IStock[]>) => {
                this.stocks = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadStockWarehouse() {
        this.stockService.allWarehouse().subscribe(
            (res: HttpResponse<IWarehouse[]>) => {
                this.warehouses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadStockHolding() {
        this.stockService.allHolding().subscribe(
            (res: HttpResponse<IHolding[]>) => {
                this.holdings = res.body;
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
            '/stock',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.bnon = true;
        if (this.cat == null || this.cat === undefined) {
            // this.loadAll();
            this.loadStockCat('');
        }

        this.loadStockWarehouse();
        this.loadStockHolding();

        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInStocks();

        // Mise à jour des catégories
        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
                this.copieCat();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        // Mise à jour du prix max
        this.stockService.getPrixMax().subscribe(
            (res: HttpResponse<String>) => {
                const tmp: number = +res.body;
                this.prixMaxInit = tmp;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );

        // Mise à jour des produits
        this.productTypeService.query().subscribe(
            (res: HttpResponse<IProductType[]>) => {
                this.products = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        // Mise à jour des Vendeurs
        this.stockService.allSeller().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.seller = res.body;
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

    onChangeBuy(deviceValue: string) {
        const tmp: number = +deviceValue;
        this.qtbuy = tmp;
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
                this.openSnackbar(s.name);
            },
            (res: HttpErrorResponse) => {
                alert('Desolé plus de produit disponible');
            }
        );
    }

    openNav() {
        this.bclik = !this.bclik;
        this.prixMini = 0;
        this.prixMax = this.prixMaxInit;
        this.filterSearch = '';
        this.filterOptionWare = -1;
        this.filterOptionHold = -1;
    }

    closeNav() {
        this.bclik = false;
        this.prixMini = 0;
        this.prixMax = this.prixMaxInit;
        this.filterSearch = '';
        this.filterOptionWare = -1;
        this.filterOptionHold = -1;
    }

    copieCat() {
        const a: any[] = [];
        for (const j of this.categories) {
            a.push({ name: j.name, bol: false });
        }
        this.listCat = a;
    }

    checkboxCat(i: number) {
        this.listCat[i].bol = !this.listCat[i].bol;
    }

    checkboxProd(i: number) {
        this.listProd[i].bol = !this.listProd[i].bol;
    }

    filter() {
        let params = new HttpParams();
        params = params.set('page', `${this.page - 1}`).set('size', `${this.pageSize}`);
        params = params.set('priceUnit.lessOrEqualThan', `${this.prixMax}`);
        params = params.set('priceUnit.greaterOrEqualThan', `${this.prixMini}`);
        params = params.set('quantityRemaining.greaterThan', '0');
        params = params.set('available.equals', 'true');

        if (this.filterOptionWare !== -1) {
            params = params.set('warehouseId.equals', `${this.filterOptionWare}`);
        }
        if (this.filterOptionHold !== -1) {
            params = params.set('holdingId.equals', `${this.filterOptionHold}`);
        }
        if (this.filterSearch !== '') {
            params = params.set('name.contains', this.filterSearch);
        }

        const tabCat: string[] = [];
        for (const i of this.listCat) {
            if (i.bol === true) {
                tabCat.push(i.name);
            }
        }
        if (tabCat != null && tabCat.length > 0) {
            params = params.append('categoryName.in', tabCat.join(', '));
        }

        this.stockService.filter(params).subscribe(
            (res: HttpResponse<IStock[]>) => {
                this.stocks = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    onChangeWare(deviceValue: string) {
        const tmp: number = +deviceValue;
        this.filterOptionWare = tmp;
    }

    onChangeHold(deviceValue: string) {
        const tmp: number = +deviceValue;
        this.filterOptionHold = tmp;
    }

    openSnackbar(produit: string) {
        // SnackBar
        this.message = 'Ajout du produit: ' + produit;
        this.actionButtonLabel = 'Enlever';
        this.action = true;
        this.setAutoHide = true;
        this.autoHide = 3000;
        this.horizontalPosition = 'right';
        this.verticalPosition = 'top';
        this.addExtraClass = false;

        // this.snackBar.open('Message archived', 'Undo', {duration: 3000});
        const config: MatSnackBarConfig = new MatSnackBarConfig();
        config.verticalPosition = this.verticalPosition;
        config.horizontalPosition = this.horizontalPosition;
        config.duration = this.setAutoHide ? this.autoHide : 0;
        // config.extraClasses = this.addExtraClass ? ['test'] : undefined;
        this.snackBar.open(this.message, this.action ? this.actionButtonLabel : undefined, config);
    }

    nonLocation() {
        this.bnon = false;
    }
}
