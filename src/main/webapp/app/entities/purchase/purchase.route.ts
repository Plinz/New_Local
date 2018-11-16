import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Purchase } from 'app/shared/model/purchase.model';
import { PurchaseService } from './purchase.service';
import { PurchaseComponent } from './purchase.component';
import { PurchaseDetailComponent } from './purchase-detail.component';
import { PurchaseUpdateComponent } from './purchase-update.component';
import { PurchaseDeletePopupComponent } from './purchase-delete-dialog.component';
import { IPurchase } from 'app/shared/model/purchase.model';

@Injectable({ providedIn: 'root' })
export class PurchaseResolve implements Resolve<IPurchase> {
    constructor(private service: PurchaseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((purchase: HttpResponse<Purchase>) => purchase.body));
        }
        return of(new Purchase());
    }
}

export const purchaseRoute: Routes = [
    {
        path: 'purchase',
        component: PurchaseComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchase.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase/:id/view',
        component: PurchaseDetailComponent,
        resolve: {
            purchase: PurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchase.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase/new',
        component: PurchaseUpdateComponent,
        resolve: {
            purchase: PurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchase.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase/:id/edit',
        component: PurchaseUpdateComponent,
        resolve: {
            purchase: PurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchase.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const purchasePopupRoute: Routes = [
    {
        path: 'purchase/:id/delete',
        component: PurchaseDeletePopupComponent,
        resolve: {
            purchase: PurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchase.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
