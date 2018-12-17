import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IHolding } from '../shared/model/holding.model';
import { HoldingService } from '../entities/holding/holding.service';
import { IImage } from '../shared/model/image.model';
import { ImageService } from '../entities/image';
import { ILocation } from '../shared/model/location.model';
import { LocationService } from '../entities/location';
import { IUser, UserService } from '../core';

@Component({
    selector: 'jhi-holding-update',
    templateUrl: './holdingProfil-update.component.html'
})
export class HoldingProfilUpdateComponent implements OnInit {
    holding: IHolding;
    isSaving: boolean;

    images: IImage[];

    locations: ILocation[];
    location: ILocation;

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
        this.activatedRoute.data.subscribe(({ location }) => {
            this.location = location;
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
        if (this.location.id !== undefined) {
            this.subscribeToSaveResponseD(this.locationService.update(this.location));
        } else {
            this.subscribeToSaveResponseD(this.locationService.create(this.location));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IHolding>>) {
        result.subscribe((res: HttpResponse<IHolding>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private subscribeToSaveResponseD(result: Observable<HttpResponse<ILocation>>) {
        result.subscribe((res: HttpResponse<ILocation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
