import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PurchaseDone } from 'app/shared/model/purchase-done.model';
import { PurchaseDoneService } from './purchase-done.service';
import { PurchaseDoneComponent } from './purchase-done.component';
import { PurchaseDoneDetailComponent } from './purchase-done-detail.component';
import { PurchaseDoneUpdateComponent } from './purchase-done-update.component';
import { PurchaseDoneDeletePopupComponent } from './purchase-done-delete-dialog.component';
import { IPurchaseDone } from 'app/shared/model/purchase-done.model';

@Injectable({ providedIn: 'root' })
export class PurchaseDoneResolve implements Resolve<IPurchaseDone> {
    constructor(private service: PurchaseDoneService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((purchaseDone: HttpResponse<PurchaseDone>) => purchaseDone.body));
        }
        return of(new PurchaseDone());
    }
}

export const purchaseDoneRoute: Routes = [
    {
        path: 'purchase-done',
        component: PurchaseDoneComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchaseDone.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-done/:id/view',
        component: PurchaseDoneDetailComponent,
        resolve: {
            purchaseDone: PurchaseDoneResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchaseDone.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-done/new',
        component: PurchaseDoneUpdateComponent,
        resolve: {
            purchaseDone: PurchaseDoneResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchaseDone.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-done/:id/edit',
        component: PurchaseDoneUpdateComponent,
        resolve: {
            purchaseDone: PurchaseDoneResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchaseDone.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const purchaseDonePopupRoute: Routes = [
    {
        path: 'purchase-done/:id/delete',
        component: PurchaseDoneDeletePopupComponent,
        resolve: {
            purchaseDone: PurchaseDoneResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchaseDone.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
