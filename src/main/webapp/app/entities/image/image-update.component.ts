import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IImage } from 'app/shared/model/image.model';
import { ImageService } from './image.service';
import { IStock } from 'app/shared/model/stock.model';
import { StockService } from 'app/entities/stock';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';
import { IProductType } from 'app/shared/model/product-type.model';
import { ProductTypeService } from 'app/entities/product-type';
import { IHolding } from 'app/shared/model/holding.model';
import { HoldingService } from 'app/entities/holding';
import { IWarehouse } from 'app/shared/model/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse';

@Component({
    selector: 'jhi-image-update',
    templateUrl: './image-update.component.html'
})
export class ImageUpdateComponent implements OnInit {
    image: IImage;
    isSaving: boolean;

    stocks: IStock[];

    categories: ICategory[];

    producttypes: IProductType[];

    holdings: IHolding[];

    warehouses: IWarehouse[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private imageService: ImageService,
        private stockService: StockService,
        private categoryService: CategoryService,
        private productTypeService: ProductTypeService,
        private holdingService: HoldingService,
        private warehouseService: WarehouseService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ image }) => {
            this.image = image;
        });
        this.stockService.query().subscribe(
            (res: HttpResponse<IStock[]>) => {
                this.stocks = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.productTypeService.query().subscribe(
            (res: HttpResponse<IProductType[]>) => {
                this.producttypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.holdingService.query().subscribe(
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
        if (this.image.id !== undefined) {
            this.subscribeToSaveResponse(this.imageService.update(this.image));
        } else {
            this.subscribeToSaveResponse(this.imageService.create(this.image));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IImage>>) {
        result.subscribe((res: HttpResponse<IImage>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackStockById(index: number, item: IStock) {
        return item.id;
    }

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }

    trackProductTypeById(index: number, item: IProductType) {
        return item.id;
    }

    trackHoldingById(index: number, item: IHolding) {
        return item.id;
    }

    trackWarehouseById(index: number, item: IWarehouse) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
