import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from '../core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Purchase } from '../shared/model/purchase.model';
import { PurchaseService } from '../entities/purchase/purchase.service';
import { ShoppingComponent } from './shopping.component';
import { ShoppingDeletePopupComponent } from './shopping-delete-dialog.component';
import { IPurchase } from '../shared/model/purchase.model';

@Injectable({ providedIn: 'root' })
export class ShoppingResolve implements Resolve<IPurchase> {
    constructor(private service: PurchaseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((purchase: HttpResponse<Purchase>) => purchase.body));
        }
        return of(new Purchase());
    }
}

export const ShoppingRoute: Routes = [
    {
        path: 'shopping',
        component: ShoppingComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchase.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ShoppingPopupRoute: Routes = [
    {
        path: 'shopping/:id/delete',
        component: ShoppingDeletePopupComponent,
        resolve: {
            purchase: ShoppingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchase.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
