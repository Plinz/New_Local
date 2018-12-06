import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from '../core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ShoppingComponent } from './shopping.component';
import { ShoppingDeletePopupComponent } from './shopping-delete-dialog.component';
import { ICart } from 'app/shared/model/cart.model';
import { CartService } from '../entities/cart/cart.service';
import { Cart } from 'app/shared/model/cart.model';

@Injectable({ providedIn: 'root' })
export class ShoppingResolve implements Resolve<ICart> {
    constructor(private service: CartService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((cart: HttpResponse<Cart>) => cart.body));
        }
        return of(new Cart());
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
            cart: ShoppingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.cart.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
