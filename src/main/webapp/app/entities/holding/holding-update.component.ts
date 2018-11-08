import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IHolding } from 'app/shared/model/holding.model';
import { HoldingService } from './holding.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';
import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from 'app/entities/person';

@Component({
    selector: 'jhi-holding-update',
    templateUrl: './holding-update.component.html'
})
export class HoldingUpdateComponent implements OnInit {
    holding: IHolding;
    isSaving: boolean;

    locations: ILocation[];

    people: IPerson[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private holdingService: HoldingService,
        private locationService: LocationService,
        private personService: PersonService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ holding }) => {
            this.holding = holding;
        });
        this.locationService.query().subscribe(
            (res: HttpResponse<ILocation[]>) => {
                this.locations = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.personService.query().subscribe(
            (res: HttpResponse<IPerson[]>) => {
                this.people = res.body;
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
        this.dataUtils.clearInputImage(this.holding, this.elementRef, field, fieldContentType, idInput);
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

    trackLocationById(index: number, item: ILocation) {
        return item.id;
    }

    trackPersonById(index: number, item: IPerson) {
        return item.id;
    }
}
