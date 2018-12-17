import { Injectable } from '@angular/core';
import { ILocation } from '../../shared/model/location.model';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { Principal } from 'app/core';
import { LocationService } from 'app/entities/location';

@Injectable({ providedIn: 'root' })
export class MainService {
    location: ILocation = null;
    fromBase: Boolean = null;

    constructor(private principal: Principal, private locationService: LocationService, private jhiAlertService: JhiAlertService) {
        if (this.principal.isAuthenticated()) {
            this.locationService.findByCurrentUser().subscribe(
                (res: HttpResponse<ILocation>) => {
                    if (res.body != null) {
                        this.setLocation(res.body, true);
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }

    setLocation(location: ILocation, fromBase: Boolean) {
        this.location = location;
        this.fromBase = fromBase;
    }

    isLocationFromBase(): Boolean {
        return this.fromBase;
    }

    getLocation(): ILocation {
        return this.location;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
