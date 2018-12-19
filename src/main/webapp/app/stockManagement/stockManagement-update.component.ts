import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IStock } from '../shared/model/stock.model';
import { StockService } from 'app/entities/stock';
import { IProductType, ProductType } from '../shared/model/product-type.model';
import { ProductTypeService } from '../entities/product-type';
import { IHolding } from '../shared/model/holding.model';
import { HoldingService } from '../entities/holding';
import { IUser } from '../core';
import { ImageService } from 'app/entities/image';
import { IImage, Image } from 'app/shared/model/image.model';
import { IWarehouse } from 'app/shared/model/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';

@Component({
    selector: 'jhi-stock-update',
    templateUrl: './stockManagement-update.component.html'
})
export class StockManagementUpdateComponent implements OnInit {
    stock: IStock;
    productTypeSelectionMethod: number;
    productTypesCurrentUser: IProductType[];
    productTypesExisting: IProductType[];
    productTypeNotExisting: IProductType;
    holdings: IHolding[];
    warehouses: IWarehouse[];
    expiryDate: string;
    currentDate: string;
    stats: string[];
    image: IImage;
    categories: ICategory[];

    isSaving: boolean;
    btnValidateProductType: boolean;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private stockService: StockService,
        private productTypeService: ProductTypeService,
        private holdingService: HoldingService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute,
        private imageService: ImageService,
        private warehouseService: WarehouseService,
        private categoryService: CategoryService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.btnValidateProductType = false;
        this.productTypeNotExisting = new ProductType();
        this.image = new Image();
        this.activatedRoute.data.subscribe(({ stock }) => {
            this.stock = stock;
            this.expiryDate = this.stock.expiryDate != null ? this.stock.expiryDate.format(DATE_TIME_FORMAT) : null;
        });
        this.productTypeService.query().subscribe(
            (res: HttpResponse<IProductType[]>) => {
                this.productTypesExisting = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.productTypeService.findByCurrentUser().subscribe(
            (res: HttpResponse<IProductType[]>) => {
                this.productTypesCurrentUser = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.holdingService.findByCurrentUser().subscribe(
            (res: HttpResponse<IHolding[]>) => {
                this.holdings = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.warehouseService.query().subscribe(
            (res: HttpResponse<IWarehouse[]>) => {
                this.warehouses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        const myDate = new Date();
        if (myDate.getDate() < 10) {
            this.currentDate = `${myDate.getFullYear()}-${myDate.getMonth() + 1}-0${myDate.getDate()}`;
        } else {
            this.currentDate = `${myDate.getFullYear()}-${myDate.getMonth() + 1}-${myDate.getDate()}`;
        }
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.image, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.productTypeSelectionMethod === 2) {
            if (this.productTypeNotExisting.id !== undefined && this.productTypeNotExisting.id !== null) {
                this.productTypeService.update(this.productTypeNotExisting).subscribe(
                    (res: HttpResponse<IProductType>) => {
                        this.productTypeNotExisting = res.body;
                        this.stock.productTypeId = this.productTypeNotExisting.id;
                        this.stock.productType = this.productTypeNotExisting;
                        this.save2();
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            } else {
                this.productTypeService.create(this.productTypeNotExisting).subscribe(
                    (res: HttpResponse<IProductType>) => {
                        this.productTypeNotExisting = res.body;
                        this.stock.productTypeId = this.productTypeNotExisting.id;
                        this.stock.productType = this.productTypeNotExisting;
                        this.save2();
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            }
        } else {
            this.save2();
        }
    }

    save2() {
        if (this.image.image !== undefined && this.image.image !== null) {
            if (this.image.id !== undefined && this.image.id !== null) {
                this.imageService.update(this.image).subscribe(
                    (res: HttpResponse<IImage>) => {
                        this.image = res.body;
                        this.stock.imageId = this.image.id;
                        this.stock.image = this.image;
                        this.save3();
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            } else {
                this.image.name = 'toto';
                this.imageService.create(this.image).subscribe(
                    (res: HttpResponse<IImage>) => {
                        this.image = res.body;
                        this.stock.imageId = this.image.id;
                        this.stock.image = this.image;
                        this.save3();
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            }
        } else {
            this.stock.imageId = 1;
            this.save3();
        }
    }

    save3() {
        this.stock.onSaleDate = moment(new Date(), DATE_TIME_FORMAT);
        this.stock.expiryDate = this.expiryDate != null ? moment(this.expiryDate, DATE_TIME_FORMAT) : null;
        this.stock.quantityRemaining = this.stock.quantityInit;
        this.stock.available = false;
        if (this.stock.id !== undefined && this.stock.id !== null) {
            this.subscribeToSaveResponse(this.stockService.update(this.stock));
        } else {
            this.subscribeToSaveResponse(this.stockService.create(this.stock));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IStock>>) {
        result.subscribe((res: HttpResponse<IStock>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackProductTypeById(index: number, item: IProductType) {
        return item.id;
    }

    trackHoldingById(index: number, item: IHolding) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    clicBtnValidateProductType() {
        this.btnValidateProductType = true;
        alert('ok:' + this.productTypeSelectionMethod);
        if (this.productTypeSelectionMethod === 2) {
            this.stats = null;
            alert('oui');
        } else {
            // Initialize Params Object
            let parameters = new HttpParams();
            alert('non');
            // Begin assigning parameters
            parameters = parameters.append('productTypeId', this.stock.productType.id.toString());
            parameters = parameters.append('bio', String(this.stock.bio));

            this.stockService.statsStock(parameters).subscribe(
                (res: HttpResponse<string[]>) => {
                    this.stats = res.body;

                    if (this.stats !== undefined && this.stats !== null) {
                        for (let i = 0; i < this.stats.length; i++) {
                            switch (i) {
                                case 0:
                                    this.stats[i] = `Prix de vente le plus bas : ${this.stats[i]} €`;
                                    break;
                                case 1:
                                    this.stats[i] = `Prix de vente moyen : ${this.stats[i]} €`;
                                    break;
                                case 2:
                                    this.stats[i] = `Prix de vente médian : ${this.stats[i]} €`;
                                    break;
                                case 3:
                                    this.stats[i] = `Prix de vente le plus haut : ${this.stats[i]} €`;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }

    onChangeProductTypeSelectionMethod(productTypeSelectionMethod: string) {
        const tmp: number = +productTypeSelectionMethod;
        this.productTypeSelectionMethod = tmp;
    }

    onChangeProductTypeCurrentUser(i: string) {
        const tmp: number = +i;
        this.stock.productTypeId = this.productTypesCurrentUser[tmp].id;
        this.stock.productType = this.productTypesCurrentUser[tmp];
    }

    onChangeProductTypeExisting(i: string) {
        const tmp: number = +i;
        this.stock.productTypeId = this.productTypesExisting[tmp].id;
        this.stock.productType = this.productTypesExisting[tmp];
    }

    onChangeCategory(i: string) {
        const tmp: number = +i;
        this.productTypeNotExisting.categoryId = this.categories[tmp].id;
    }

    onChangeHolding(i: string) {
        const tmp: number = +i;

        this.stock.holdingId = this.holdings[tmp].id;
        this.stock.holding = this.holdings[tmp];

        // récupère l'entrepôt le plus proche du holding sélectionné
        let distWarehouse: number;
        let distWarehouseMin: number;
        this.warehouses.forEach((value: IWarehouse) => {
            distWarehouse = this.distance(value);
            if (distWarehouseMin === undefined || distWarehouseMin === null) {
                distWarehouseMin = distWarehouse;
                this.stock.warehouseId = value.id;
                this.stock.warehouse = value;
            } else if (distWarehouse < distWarehouseMin) {
                distWarehouseMin = distWarehouse;
                this.stock.warehouseId = value.id;
                this.stock.warehouse = value;
            }
        });
    }

    distance(warehouse: IWarehouse) {
        const loc = this.stock.holding.location;
        let dist = -1;
        if (loc !== null && loc !== undefined) {
            const lon1 = warehouse.location.lon;
            const lat1 = warehouse.location.lat;
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

    deg2rad(deg: number) {
        return deg * Math.PI / 180.0;
    }

    rad2deg(rad: number) {
        return rad * 180 / Math.PI;
    }
}
