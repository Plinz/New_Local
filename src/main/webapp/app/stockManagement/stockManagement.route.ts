import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from '../core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Stock } from '../shared/model/stock.model';
import { StockService } from '../entities/stock/stock.service';
import { StockManagementComponent } from './stockManagement.component';
import { StockManagementDetailComponent } from './stockManagement-detail.component';
import { StockManagementUpdateComponent } from './stockManagement-update.component';
import { StockManagementDeletePopupComponent } from './stockManagement-delete-dialog.component';
import { IStock } from '../shared/model/stock.model';

@Injectable({ providedIn: 'root' })
export class StockManagementResolve implements Resolve<IStock> {
    constructor(private service: StockService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((stock: HttpResponse<Stock>) => stock.body));
        }
        return of(new Stock());
    }
}

export const stockManagementRoute: Routes = [
    {
        path: 'stockManagement',
        component: StockManagementComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'newLocalApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'stockManagement/:id/view',
        component: StockManagementDetailComponent,
        resolve: {
            stock: StockManagementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'stockManagement/new',
        component: StockManagementUpdateComponent,
        resolve: {
            stock: StockManagementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'stockManagement/:id/edit',
        component: StockManagementUpdateComponent,
        resolve: {
            stock: StockManagementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const stockManagementPopupRoute: Routes = [
    {
        path: 'stockManagement/:id/delete',
        component: StockManagementDeletePopupComponent,
        resolve: {
            stock: StockManagementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
