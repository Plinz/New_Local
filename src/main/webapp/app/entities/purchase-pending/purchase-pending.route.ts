import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PurchasePending } from 'app/shared/model/purchase-pending.model';
import { PurchasePendingService } from './purchase-pending.service';
import { PurchasePendingComponent } from './purchase-pending.component';
import { PurchasePendingDetailComponent } from './purchase-pending-detail.component';
import { PurchasePendingUpdateComponent } from './purchase-pending-update.component';
import { PurchasePendingDeletePopupComponent } from './purchase-pending-delete-dialog.component';
import { IPurchasePending } from 'app/shared/model/purchase-pending.model';

@Injectable({ providedIn: 'root' })
export class PurchasePendingResolve implements Resolve<IPurchasePending> {
    constructor(private service: PurchasePendingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((purchasePending: HttpResponse<PurchasePending>) => purchasePending.body));
        }
        return of(new PurchasePending());
    }
}

export const purchasePendingRoute: Routes = [
    {
        path: 'purchase-pending',
        component: PurchasePendingComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchasePending.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-pending/:id/view',
        component: PurchasePendingDetailComponent,
        resolve: {
            purchasePending: PurchasePendingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchasePending.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-pending/new',
        component: PurchasePendingUpdateComponent,
        resolve: {
            purchasePending: PurchasePendingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchasePending.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-pending/:id/edit',
        component: PurchasePendingUpdateComponent,
        resolve: {
            purchasePending: PurchasePendingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchasePending.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const purchasePendingPopupRoute: Routes = [
    {
        path: 'purchase-pending/:id/delete',
        component: PurchasePendingDeletePopupComponent,
        resolve: {
            purchasePending: PurchasePendingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchasePending.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
