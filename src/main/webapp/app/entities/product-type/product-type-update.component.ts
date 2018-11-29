import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IProductType } from 'app/shared/model/product-type.model';
import { ProductTypeService } from './product-type.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';
import { IImage } from 'app/shared/model/image.model';
import { ImageService } from 'app/entities/image';

@Component({
    selector: 'jhi-product-type-update',
    templateUrl: './product-type-update.component.html'
})
export class ProductTypeUpdateComponent implements OnInit {
    productType: IProductType;
    isSaving: boolean;

    categories: ICategory[];

    images: IImage[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private productTypeService: ProductTypeService,
        private categoryService: CategoryService,
        private imageService: ImageService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ productType }) => {
            this.productType = productType;
        });
        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.imageService.query().subscribe(
            (res: HttpResponse<IImage[]>) => {
                this.images = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.productType.id !== undefined) {
            this.subscribeToSaveResponse(this.productTypeService.update(this.productType));
        } else {
            this.subscribeToSaveResponse(this.productTypeService.create(this.productType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProductType>>) {
        result.subscribe((res: HttpResponse<IProductType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }

    trackImageById(index: number, item: IImage) {
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
