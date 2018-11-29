import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Warehouse } from 'app/shared/model/warehouse.model';
import { WarehouseService } from './warehouse.service';
import { WarehouseComponent } from './warehouse.component';
import { WarehouseDetailComponent } from './warehouse-detail.component';
import { WarehouseUpdateComponent } from './warehouse-update.component';
import { WarehouseDeletePopupComponent } from './warehouse-delete-dialog.component';
import { IWarehouse } from 'app/shared/model/warehouse.model';

@Injectable({ providedIn: 'root' })
export class WarehouseResolve implements Resolve<IWarehouse> {
    constructor(private service: WarehouseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((warehouse: HttpResponse<Warehouse>) => warehouse.body));
        }
        return of(new Warehouse());
    }
}

export const warehouseRoute: Routes = [
    {
        path: 'warehouse',
        component: WarehouseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'newLocalApp.warehouse.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'warehouse/:id/view',
        component: WarehouseDetailComponent,
        resolve: {
            warehouse: WarehouseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.warehouse.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'warehouse/new',
        component: WarehouseUpdateComponent,
        resolve: {
            warehouse: WarehouseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.warehouse.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'warehouse/:id/edit',
        component: WarehouseUpdateComponent,
        resolve: {
            warehouse: WarehouseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.warehouse.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const warehousePopupRoute: Routes = [
    {
        path: 'warehouse/:id/delete',
        component: WarehouseDeletePopupComponent,
        resolve: {
            warehouse: WarehouseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.warehouse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
