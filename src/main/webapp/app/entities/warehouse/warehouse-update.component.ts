import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWarehouse } from 'app/shared/model/warehouse.model';
import { WarehouseService } from './warehouse.service';
import { IImage } from 'app/shared/model/image.model';
import { ImageService } from 'app/entities/image';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';

@Component({
    selector: 'jhi-warehouse-update',
    templateUrl: './warehouse-update.component.html'
})
export class WarehouseUpdateComponent implements OnInit {
    warehouse: IWarehouse;
    isSaving: boolean;

    images: IImage[];

    locations: ILocation[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private warehouseService: WarehouseService,
        private imageService: ImageService,
        private locationService: LocationService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ warehouse }) => {
            this.warehouse = warehouse;
        });
        this.imageService.query({ 'warehouseId.specified': 'false' }).subscribe(
            (res: HttpResponse<IImage[]>) => {
                if (!this.warehouse.imageId) {
                    this.images = res.body;
                } else {
                    this.imageService.find(this.warehouse.imageId).subscribe(
                        (subRes: HttpResponse<IImage>) => {
                            this.images = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.locationService.query().subscribe(
            (res: HttpResponse<ILocation[]>) => {
                this.locations = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.warehouse.id !== undefined) {
            this.subscribeToSaveResponse(this.warehouseService.update(this.warehouse));
        } else {
            this.subscribeToSaveResponse(this.warehouseService.create(this.warehouse));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWarehouse>>) {
        result.subscribe((res: HttpResponse<IWarehouse>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackImageById(index: number, item: IImage) {
        return item.id;
    }

    trackLocationById(index: number, item: ILocation) {
        return item.id;
    }
}
