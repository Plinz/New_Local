import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';

import { JhiLanguageHelper, Principal } from 'app/core';
import { LocationService } from 'app/entities/location';
import { ILocation, Location } from '../../shared/model/location.model';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html'
})
export class JhiMainComponent implements OnInit {
    location: ILocation;

    constructor(
        private principal: Principal,
        private jhiLanguageHelper: JhiLanguageHelper,
        private router: Router,
        private locationService: LocationService,
        private jhiAlertService: JhiAlertService
    ) {}

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'newLocalApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    ngOnInit() {
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });
        if (this.principal.isAuthenticated()) {
            this.locationService.findByCurrentUser().subscribe(
                (res: HttpResponse<ILocation>) => {
                    this.location = res.body;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
        if (this.location == null && window.navigator.geolocation) {
            window.navigator.geolocation.getCurrentPosition(
                position => {
                    this.location = new Location();
                    this.location.lat = position.coords.latitude;
                    this.location.lon = position.coords.longitude;
                },
                error => {
                    this.onError(error.message);
                }
            );
        } else {
            this.onError('Geolocation not supported in this browser');
        }
    }
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
