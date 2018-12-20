import { Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { UserLocationComponent } from './userLocation.component';
import { UserNewLocationComponent } from './userLocation-update.component';
import { StockDetailComponent } from 'app/entities/stock';
import { StockResolve } from 'app/entities/stock';
import { from } from 'rxjs';
import { LocationResolve } from 'app/entities/location/location.route';
import { HomeComponent } from '../home/home.component';

export const userLocationRoute: Routes = [
    {
        path: 'userLocation',
        component: UserLocationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'newLocalApp.userLocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'userLocation/userNewLocation',
        component: UserNewLocationComponent,
        resolve: {
            location: LocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.userLocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: '',
        component: HomeComponent,
        data: {
            authorities: [],
            pageTitle: 'home.title'
        }
    }
];
