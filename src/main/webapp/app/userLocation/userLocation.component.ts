import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { ILocation } from 'app/shared/model/location.model';
import { Principal, AccountService } from 'app/core';
import { IUser, UserService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { LocationService } from 'app/entities/location/location.service';
import { isModuleDeclaration } from 'typescript';

@Component({
    selector: 'jhi-userlocation',
    templateUrl: './userLocation.component.html'
})
export class UserLocationComponent implements OnInit, OnDestroy {
    settingsAccount: any;
    location: ILocation = null;
    newLocation: ILocation;
    isSaving: boolean;
    testForm: boolean;

    constructor(
        private account: AccountService,
        private principal: Principal,
        private jhiAlertService: JhiAlertService,
        private locationService: LocationService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}
    ngOnInit() {
        this.isSaving = false;
        this.principal.identity().then(account => {
            this.settingsAccount = this.copyAccount(account);
        });

        this.locationService.findByCurrentUser().subscribe(
            (res: HttpResponse<ILocation>) => {
                this.newLocation = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    save() {
        this.isSaving = true;
        if (this.newLocation.id !== undefined) {
            this.newLocation.lat = null;
            this.newLocation.lon = null;
            this.subscribeToSaveResponse(this.locationService.update(this.newLocation));
        } else {
            this.subscribeToSaveResponse(this.locationService.create(this.newLocation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ILocation>>) {
        result.subscribe((res: HttpResponse<ILocation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    form() {
        this.testForm = true;
    }
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
    previousState() {
        window.history.back();
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    locationInit() {
        (this.newLocation.id = null), (this.newLocation.city = '');
        (this.newLocation.country = ''),
            (this.newLocation.zip = ''),
            (this.newLocation.address = ''),
            (this.newLocation.lon = null),
            (this.newLocation.lat = null),
            (this.newLocation.userId = null),
            (this.newLocation.user = undefined);
    }
    copyAccount(account) {
        return {
            id: account.id,
            activated: account.activated,
            email: account.email,
            firstName: account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login,
            imageUrl: account.imageUrl,
            authorite: account.authorities
        };
    }
    ngOnDestroy() {}
}
