import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IHolding } from 'app/shared/model/holding.model';
import { HoldingService } from './holding.service';
import { IImage } from 'app/shared/model/image.model';
import { ImageService } from 'app/entities/image';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-holding-update',
    templateUrl: './holding-update.component.html'
})
export class HoldingUpdateComponent implements OnInit {
    holding: IHolding;
    isSaving: boolean;

    images: IImage[];

    locations: ILocation[];

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private holdingService: HoldingService,
        private imageService: ImageService,
        private locationService: LocationService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ holding }) => {
            this.holding = holding;
        });
        this.imageService.query({ 'holdingId.specified': 'false' }).subscribe(
            (res: HttpResponse<IImage[]>) => {
                if (!this.holding.imageId) {
                    this.images = res.body;
                } else {
                    this.imageService.find(this.holding.imageId).subscribe(
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
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.holding.id !== undefined) {
            this.subscribeToSaveResponse(this.holdingService.update(this.holding));
        } else {
            this.subscribeToSaveResponse(this.holdingService.create(this.holding));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IHolding>>) {
        result.subscribe((res: HttpResponse<IHolding>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
